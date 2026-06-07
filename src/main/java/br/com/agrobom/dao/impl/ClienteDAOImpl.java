package br.com.agrobom.dao.impl;

import br.com.agrobom.dao.ClienteDAO;
import br.com.agrobom.model.Cliente;
import br.com.agrobom.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {
	@Override
    public void inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (cpf, nome, telefone, endereco) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cliente.getCpf());
            ps.setString(2, cliente.getNome());
            ps.setString(3, cliente.getTelefone());
            ps.setString(4, cliente.getEndereco());
            ps.executeUpdate();
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM cliente WHERE cpf = ?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearCliente(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        String sql = "SELECT * FROM cliente ORDER BY nome";
        Connection conn = null;
        List<Cliente> clientes = new ArrayList<>();
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
        return clientes;
    }

    @Override
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, endereco = ? WHERE cpf = ?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getEndereco());
            ps.setString(4, cliente.getCpf());
            ps.executeUpdate();
            System.out.println("Cliente atualizado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    @Override
    public void deletar(String cpf) {
        String sql = "DELETE FROM cliente WHERE cpf = ?";
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cpf);
            ps.executeUpdate();
            System.out.println("Cliente removido com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    // Mapeia uma linha do ResultSet para um objeto Cliente
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setCpf(rs.getString("cpf"));
        c.setNome(rs.getString("nome"));
        c.setTelefone(rs.getString("telefone"));
        c.setEndereco(rs.getString("endereco"));
        return c;
    }
}
