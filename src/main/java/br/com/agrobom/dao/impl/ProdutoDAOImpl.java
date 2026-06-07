package br.com.agrobom.dao.impl;

import br.com.agrobom.dao.ProdutoDAO;
import br.com.agrobom.model.Produto;
import br.com.agrobom.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAOImpl implements ProdutoDAO {
	@Override
	public void inserir(Produto produto) {
		String sql = "INSERT INTO produto (descricao, quant_existente, quant_minima, unidade_medida) "
				+ "VALUES (?, ?, ?, ?)";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, produto.getDescricao());
			ps.setInt(2, produto.getQuantExistente());
			ps.setInt(3, produto.getQuantMinima());
			ps.setString(4, produto.getUnidadeMedida());
			ps.executeUpdate();

			// Recupera o código gerado automaticamente pelo AUTO_INCREMENT
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				produto.setCodigo(rs.getInt(1));
			}
			System.out.println("Produto cadastrado com sucesso! Código: " + produto.getCodigo());
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir produto: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
	}

	@Override
	public Produto buscarPorCodigo(int codigo) {
		String sql = "SELECT * FROM produto WHERE codigo = ?";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, codigo);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return mapearProduto(rs);
			}
			return null;
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao buscar produto: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
	}

	@Override
	public List<Produto> listarTodos() {
		String sql = "SELECT * FROM produto ORDER BY descricao";
		Connection conn = null;
		List<Produto> produtos = new ArrayList<>();
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				produtos.add(mapearProduto(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
		return produtos;
	}

	@Override
	public void atualizar(Produto produto) {
		String sql = "UPDATE produto SET descricao = ?, quant_existente = ?, "
				+ "quant_minima = ?, unidade_medida = ? WHERE codigo = ?";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, produto.getDescricao());
			ps.setInt(2, produto.getQuantExistente());
			ps.setInt(3, produto.getQuantMinima());
			ps.setString(4, produto.getUnidadeMedida());
			ps.setInt(5, produto.getCodigo());
			ps.executeUpdate();
			System.out.println("Produto atualizado com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
	}

	@Override
	public void deletar(int codigo) {
		String sql = "DELETE FROM produto WHERE codigo = ?";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, codigo);
			ps.executeUpdate();
			System.out.println("Produto removido com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar produto: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
	}

	@Override
	public List<Produto> listarEmEstoqueCritico() {
		// Relatório 1: produtos com estoque abaixo da quantidade mínima
		String sql = "SELECT * FROM produto WHERE quant_existente < quant_minima ORDER BY descricao";
		Connection conn = null;
		List<Produto> produtos = new ArrayList<>();
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				produtos.add(mapearProduto(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar produtos em estoque crítico: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
		return produtos;
	}

	@Override
	public void associarFornecedor(int codigoProduto, String cnpjFornecedor) {
		String sql = "INSERT INTO fornecido_por (codigo, cnpj) VALUES (?, ?)";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, codigoProduto);
			ps.setString(2, cnpjFornecedor);
			ps.executeUpdate();
			System.out.println("Fornecedor associado ao produto com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao associar fornecedor ao produto: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
	}

	@Override
	public void desassociarFornecedor(int codigoProduto, String cnpjFornecedor) {
		String sql = "DELETE FROM fornecido_por WHERE codigo = ? AND cnpj = ?";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, codigoProduto);
			ps.setString(2, cnpjFornecedor);
			ps.executeUpdate();
			System.out.println("Fornecedor desassociado do produto com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao desassociar fornecedor: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
	}

	// Mapeia uma linha do ResultSet para um objeto Produto
	private Produto mapearProduto(ResultSet rs) throws SQLException {
		Produto p = new Produto();
		p.setCodigo(rs.getInt("codigo"));
		p.setDescricao(rs.getString("descricao"));
		p.setQuantExistente(rs.getInt("quant_existente"));
		p.setQuantMinima(rs.getInt("quant_minima"));
		p.setUnidadeMedida(rs.getString("unidade_medida"));
		return p;
	}
}
