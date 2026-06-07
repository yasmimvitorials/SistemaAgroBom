package br.com.agrobom.dao.impl;

import br.com.agrobom.dao.SolicitacaoCompraDAO;
import br.com.agrobom.model.Fornecedor;
import br.com.agrobom.model.ItemSolicitacao;
import br.com.agrobom.model.Produto;
import br.com.agrobom.model.SolicitacaoCompra;
import br.com.agrobom.util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SolicitacaoCompraDAOImpl implements SolicitacaoCompraDAO {

	@Override
	public void inserir(SolicitacaoCompra solicitacao) {
		String sqlSolic = "INSERT INTO solicitacao_compra "
				+ "(data_emissao, data_entrega, situacao, valor_total, cnpj_fornecedor) " + "VALUES (?, ?, ?, ?, ?)";
		String sqlItem = "INSERT INTO item_solicitacao "
				+ "(numero_solicitacao, codigo, quant_solicitada, preco_unitario) " + "VALUES (?, ?, ?, ?)";

		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false); // Transação

			PreparedStatement psSolic = conn.prepareStatement(sqlSolic, Statement.RETURN_GENERATED_KEYS);
			psSolic.setDate(1, Date.valueOf(solicitacao.getDataEmissao()));
			// data_entrega pode ser nula (solicitação ainda em aberto)
			if (solicitacao.getDataEntrega() != null) {
				psSolic.setDate(2, Date.valueOf(solicitacao.getDataEntrega()));
			} else {
				psSolic.setNull(2, Types.DATE);
			}
			psSolic.setString(3, solicitacao.getSituacao());
			psSolic.setDouble(4, solicitacao.getValorTotal());
			psSolic.setString(5, solicitacao.getFornecedor().getCnpj());
			psSolic.executeUpdate();

			// Recupera o número gerado pelo AUTO_INCREMENT
			ResultSet rs = psSolic.getGeneratedKeys();
			if (rs.next()) {
				solicitacao.setNumeroSolicitacao(rs.getInt(1));
			}

			// Insere cada item da solicitação
			PreparedStatement psItem = conn.prepareStatement(sqlItem);
			for (ItemSolicitacao item : solicitacao.getItens()) {
				psItem.setInt(1, solicitacao.getNumeroSolicitacao());
				psItem.setInt(2, item.getProduto().getCodigo());
				psItem.setInt(3, item.getQuantSolicitada());
				psItem.setDouble(4, item.getPrecoUnitario());
				psItem.executeUpdate();
			}

			conn.commit();
			System.out.println("Solicitação nº " + solicitacao.getNumeroSolicitacao() + " registrada com sucesso!");

		} catch (SQLException e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				System.err.println("Erro ao fazer rollback: " + ex.getMessage());
			}
			throw new RuntimeException("Erro ao inserir solicitação: " + e.getMessage(), e);
		} finally {
			try {
				if (conn != null)
					conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.err.println("Erro ao restaurar autoCommit: " + e.getMessage());
			}
			ConnectionFactory.closeConnection(conn);
		}
	}

	@Override
	public SolicitacaoCompra buscarPorNumero(int numeroSolicitacao) {
		String sql = "SELECT s.*, f.nome AS nome_fornecedor, f.telefone, f.endereco " + "FROM solicitacao_compra s "
				+ "INNER JOIN fornecedor f ON s.cnpj_fornecedor = f.cnpj " + "WHERE s.numero_solicitacao = ?";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, numeroSolicitacao);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				SolicitacaoCompra solic = mapearSolicitacao(rs);
				solic.setItens(buscarItensDaSolicitacao(conn, numeroSolicitacao));
				return solic;
			}
			return null;
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao buscar solicitação: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
	}

	@Override
	public List<SolicitacaoCompra> listarPorMesAno(int mes, int ano) {
		// Relatório 5: solicitações de compra mês a mês
		String sql = "SELECT s.*, f.nome AS nome_fornecedor, f.telefone, f.endereco " + "FROM solicitacao_compra s "
				+ "INNER JOIN fornecedor f ON s.cnpj_fornecedor = f.cnpj "
				+ "WHERE MONTH(s.data_emissao) = ? AND YEAR(s.data_emissao) = ? " + "ORDER BY s.data_emissao";
		Connection conn = null;
		List<SolicitacaoCompra> solicitacoes = new ArrayList<>();
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, mes);
			ps.setInt(2, ano);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				SolicitacaoCompra solic = mapearSolicitacao(rs);
				solic.setItens(buscarItensDaSolicitacao(conn, solic.getNumeroSolicitacao()));
				solicitacoes.add(solic);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar solicitações por mês/ano: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
		return solicitacoes;
	}

	@Override
	public List<SolicitacaoCompra> listarTodos() {
		String sql = "SELECT s.*, f.nome AS nome_fornecedor, f.telefone, f.endereco " + "FROM solicitacao_compra s "
				+ "INNER JOIN fornecedor f ON s.cnpj_fornecedor = f.cnpj " + "ORDER BY s.data_emissao DESC";
		Connection conn = null;
		List<SolicitacaoCompra> solicitacoes = new ArrayList<>();
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				solicitacoes.add(mapearSolicitacao(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar solicitações: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
		return solicitacoes;
	}

	@Override
	public void encerrar(int numeroSolicitacao) {
		// Quando o material chega, registra a entrega e encerra a solicitação
		String sql = "UPDATE solicitacao_compra SET situacao = 'Encerrado', data_entrega = ? "
				+ "WHERE numero_solicitacao = ?";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, Date.valueOf(LocalDate.now()));
			ps.setInt(2, numeroSolicitacao);
			ps.executeUpdate();
			System.out.println("Solicitação nº " + numeroSolicitacao + " encerrada com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao encerrar solicitação: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
	}

	@Override
	public Map<String, Double> relatorioPorMes() {
		// Relatório 6: volume de solicitações em dinheiro mês a mês nos últimos 12
		// meses
		String sql = "SELECT MONTH(data_emissao) AS mes, YEAR(data_emissao) AS ano, " + "SUM(valor_total) AS total "
				+ "FROM solicitacao_compra " + "WHERE data_emissao >= DATEADD('MONTH', -12, CURRENT_DATE()) "
				+ "GROUP BY ano, mes ORDER BY ano, mes";
		Connection conn = null;
		Map<String, Double> relatorio = new LinkedHashMap<>();
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String chave = String.format("%02d/%d", rs.getInt("mes"), rs.getInt("ano"));
				relatorio.put(chave, rs.getDouble("total"));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao gerar relatório de solicitações por mês: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
		return relatorio;
	}

	// Busca os itens de uma solicitação (reutilizado internamente)
	private List<ItemSolicitacao> buscarItensDaSolicitacao(Connection conn, int numeroSolicitacao) throws SQLException {
		String sql = "SELECT i.*, p.descricao, p.unidade_medida "
				+ "FROM item_solicitacao i INNER JOIN produto p ON i.codigo = p.codigo "
				+ "WHERE i.numero_solicitacao = ?";
		List<ItemSolicitacao> itens = new ArrayList<>();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, numeroSolicitacao);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ItemSolicitacao item = new ItemSolicitacao();
			Produto produto = new Produto();
			produto.setCodigo(rs.getInt("codigo"));
			produto.setDescricao(rs.getString("descricao"));
			produto.setUnidadeMedida(rs.getString("unidade_medida"));
			item.setProduto(produto);
			item.setQuantSolicitada(rs.getInt("quant_solicitada"));
			item.setPrecoUnitario(rs.getDouble("preco_unitario"));
			item.setNumeroSolicitacao(rs.getInt("numero_solicitacao"));
			itens.add(item);
		}
		return itens;
	}

	// Mapeia uma linha do ResultSet para um objeto SolicitacaoCompra com Fornecedor
	// associado
	private SolicitacaoCompra mapearSolicitacao(ResultSet rs) throws SQLException {
		SolicitacaoCompra s = new SolicitacaoCompra();
		s.setNumeroSolicitacao(rs.getInt("numero_solicitacao"));
		s.setDataEmissao(rs.getDate("data_emissao").toLocalDate());

		// data_entrega pode ser nula se ainda estiver em aberto
		Date dataEntrega = rs.getDate("data_entrega");
		s.setDataEntrega(dataEntrega != null ? dataEntrega.toLocalDate() : null);

		s.setSituacao(rs.getString("situacao"));
		s.setValorTotal(rs.getDouble("valor_total"));

		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setCnpj(rs.getString("cnpj_fornecedor"));
		fornecedor.setNome(rs.getString("nome_fornecedor"));
		fornecedor.setTelefone(rs.getString("telefone"));
		fornecedor.setEndereco(rs.getString("endereco"));
		s.setFornecedor(fornecedor);

		return s;
	}
}
