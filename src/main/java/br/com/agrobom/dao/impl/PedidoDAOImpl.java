package br.com.agrobom.dao.impl;

import br.com.agrobom.dao.PedidoDAO;
import br.com.agrobom.model.Cliente;
import br.com.agrobom.model.ItemPedido;
import br.com.agrobom.model.Pedido;
import br.com.agrobom.model.Produto;
import br.com.agrobom.util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoDAOImpl implements PedidoDAO {
	@Override
    public void inserir(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (valor_desconto, valor_total, data_pedido, cpf_cliente) " +
                           "VALUES (?, ?, ?, ?)";
        String sqlItem   = "INSERT INTO contem (numero_pedido, codigo, quant_pedida, preco_unitario) " +
                           "VALUES (?, ?, ?, ?)";
        String sqlEstoque = "UPDATE produto SET quant_existente = quant_existente - ? WHERE codigo = ?";

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); 

            PreparedStatement psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setDouble(1, pedido.getValorDesconto());
            psPedido.setDouble(2, pedido.getValorTotal());
            psPedido.setDate(3, Date.valueOf(pedido.getDataPedido()));
            psPedido.setString(4, pedido.getCliente().getCpf());
            psPedido.executeUpdate();

            // Recupera o número do pedido gerado
            ResultSet rs = psPedido.getGeneratedKeys();
            if (rs.next()) {
                pedido.setNumeroPedido(rs.getInt(1));
            }

            // 2. Insere cada item e atualiza o estoque
            PreparedStatement psItem    = conn.prepareStatement(sqlItem);
            PreparedStatement psEstoque = conn.prepareStatement(sqlEstoque);

            for (ItemPedido item : pedido.getItens()) {
                psItem.setInt(1, pedido.getNumeroPedido());
                psItem.setInt(2, item.getProduto().getCodigo());
                psItem.setInt(3, item.getQuantPedida());
                psItem.setDouble(4, item.getPrecoUnitario());
                psItem.executeUpdate();

                psEstoque.setInt(1, item.getQuantPedida());
                psEstoque.setInt(2, item.getProduto().getCodigo());
                psEstoque.executeUpdate();
            }

            conn.commit(); // Confirma tudo
            System.out.println("Pedido nº " + pedido.getNumeroPedido() + " registrado com sucesso!");

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Desfaz tudo se der erro
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            throw new RuntimeException("Erro ao inserir pedido: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar autoCommit: " + e.getMessage());
            }
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public Pedido buscarPorNumero(int numeroPedido) {
        String sql = "SELECT p.*, c.nome AS nome_cliente, c.telefone, c.endereco " +
                     "FROM pedido p INNER JOIN cliente c ON p.cpf_cliente = c.cpf " +
                     "WHERE p.numero_pedido = ?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, numeroPedido);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Pedido pedido = mapearPedido(rs);
                pedido.setItens(buscarItensDoPedido(conn, numeroPedido));
                return pedido;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public List<Pedido> listarPorMesAno(int mes, int ano) {
        // Relatório 2: pedidos de um mês específico com dados do cliente e itens
        String sql = "SELECT p.*, c.nome AS nome_cliente, c.telefone, c.endereco " +
                     "FROM pedido p INNER JOIN cliente c ON p.cpf_cliente = c.cpf " +
                     "WHERE MONTH(p.data_pedido) = ? AND YEAR(p.data_pedido) = ? " +
                     "ORDER BY p.data_pedido";
        Connection conn = null;
        List<Pedido> pedidos = new ArrayList<>();
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, mes);
            ps.setInt(2, ano);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pedido pedido = mapearPedido(rs);
                pedido.setItens(buscarItensDoPedido(conn, pedido.getNumeroPedido()));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos por mês/ano: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return pedidos;
    }

    @Override
    public List<Pedido> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        // Relatório 3: pedidos num intervalo de datas com valor total e desconto
        String sql = "SELECT p.*, c.nome AS nome_cliente, c.telefone, c.endereco " +
                     "FROM pedido p INNER JOIN cliente c ON p.cpf_cliente = c.cpf " +
                     "WHERE p.data_pedido BETWEEN ? AND ? ORDER BY p.data_pedido";
        Connection conn = null;
        List<Pedido> pedidos = new ArrayList<>();
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos por período: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return pedidos;
    }

    @Override
    public List<Pedido> listarTodos() {
        String sql = "SELECT p.*, c.nome AS nome_cliente, c.telefone, c.endereco " +
                     "FROM pedido p INNER JOIN cliente c ON p.cpf_cliente = c.cpf " +
                     "ORDER BY p.data_pedido DESC";
        Connection conn = null;
        List<Pedido> pedidos = new ArrayList<>();
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return pedidos;
    }

    @Override
    public Map<String, Double> relatorioPorMes() {
        // Relatório 6: volume de pedidos em dinheiro mês a mês nos últimos 12 meses
        String sql = "SELECT MONTH(data_pedido) AS mes, YEAR(data_pedido) AS ano, " +
                     "SUM(valor_total) AS total " +
                     "FROM pedido " +
                     "WHERE data_pedido >= DATEADD('MONTH', -12, CURRENT_DATE()) " +
                     "GROUP BY ano, mes ORDER BY ano, mes";
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
            throw new RuntimeException("Erro ao gerar relatório de pedidos por mês: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return relatorio;
    }

    // Busca os itens de um pedido específico (reutilizado internamente)
    private List<ItemPedido> buscarItensDoPedido(Connection conn, int numeroPedido) throws SQLException {
        String sql = "SELECT c.*, p.descricao, p.unidade_medida " +
                     "FROM contem c INNER JOIN produto p ON c.codigo = p.codigo " +
                     "WHERE c.numero_pedido = ?";
        List<ItemPedido> itens = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, numeroPedido);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ItemPedido item = new ItemPedido();
            Produto produto = new Produto();
            produto.setCodigo(rs.getInt("codigo"));
            produto.setDescricao(rs.getString("descricao"));
            produto.setUnidadeMedida(rs.getString("unidade_medida"));
            item.setProduto(produto);
            item.setQuantPedida(rs.getInt("quant_pedida"));
            item.setPrecoUnitario(rs.getDouble("preco_unitario"));
            item.setNumeroPedido(rs.getInt("numero_pedido"));
            itens.add(item);
        }
        return itens;
    }

    // Mapeia uma linha do ResultSet para um objeto Pedido com Cliente associado
    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setNumeroPedido(rs.getInt("numero_pedido"));
        p.setValorDesconto(rs.getDouble("valor_desconto"));
        p.setValorTotal(rs.getDouble("valor_total"));
        p.setDataPedido(rs.getDate("data_pedido").toLocalDate());

        Cliente cliente = new Cliente();
        cliente.setCpf(rs.getString("cpf_cliente"));
        cliente.setNome(rs.getString("nome_cliente"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setEndereco(rs.getString("endereco"));
        p.setCliente(cliente);

        return p;
    }
}
