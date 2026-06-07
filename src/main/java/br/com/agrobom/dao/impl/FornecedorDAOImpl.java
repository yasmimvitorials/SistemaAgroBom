package br.com.agrobom.dao.impl;

import br.com.agrobom.dao.FornecedorDAO;
import br.com.agrobom.model.Fornecedor;
import br.com.agrobom.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAOImpl implements FornecedorDAO {
	@Override
    public void inserir(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedor (cnpj, nome, telefone, endereco) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fornecedor.getCnpj());
            ps.setString(2, fornecedor.getNome());
            ps.setString(3, fornecedor.getTelefone());
            ps.setString(4, fornecedor.getEndereco());
            ps.executeUpdate();
            System.out.println("Fornecedor cadastrado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir fornecedor: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public Fornecedor buscarPorCnpj(String cnpj) {
        String sql = "SELECT * FROM fornecedor WHERE cnpj = ?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cnpj);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearFornecedor(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar fornecedor: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public List<Fornecedor> listarTodos() {
        String sql = "SELECT * FROM fornecedor ORDER BY nome";
        Connection conn = null;
        List<Fornecedor> fornecedores = new ArrayList<>();
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fornecedores.add(mapearFornecedor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar fornecedores: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return fornecedores;
    }

    @Override
    public void atualizar(Fornecedor fornecedor) {
        String sql = "UPDATE fornecedor SET nome = ?, telefone = ?, endereco = ? WHERE cnpj = ?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getTelefone());
            ps.setString(3, fornecedor.getEndereco());
            ps.setString(4, fornecedor.getCnpj());
            ps.executeUpdate();
            System.out.println("Fornecedor atualizado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar fornecedor: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public void deletar(String cnpj) {
        String sql = "DELETE FROM fornecedor WHERE cnpj = ?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cnpj);
            ps.executeUpdate();
            System.out.println("Fornecedor removido com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar fornecedor: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public List<Fornecedor> listarPorProduto(int codigoProduto) {
        // Busca todos os fornecedores que fornecem um produto específico
        String sql = "SELECT f.* FROM fornecedor f " +
                     "INNER JOIN fornecido_por fp ON f.cnpj = fp.cnpj " +
                     "WHERE fp.codigo = ? ORDER BY f.nome";
        Connection conn = null;
        List<Fornecedor> fornecedores = new ArrayList<>();
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, codigoProduto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fornecedores.add(mapearFornecedor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar fornecedores por produto: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return fornecedores;
    }

    // Mapeia uma linha do ResultSet para um objeto Fornecedor
    private Fornecedor mapearFornecedor(ResultSet rs) throws SQLException {
        Fornecedor f = new Fornecedor();
        f.setCnpj(rs.getString("cnpj"));
        f.setNome(rs.getString("nome"));
        f.setTelefone(rs.getString("telefone"));
        f.setEndereco(rs.getString("endereco"));
        return f;
    }
}