package br.com.agrobom.service;

import br.com.agrobom.dao.ClienteDAO;
import br.com.agrobom.model.Cliente;

import java.util.List;

public class ClienteService {

	private ClienteDAO clienteDAO;

	public ClienteService(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	public void cadastrar(Cliente cliente) {
		if (cliente.getCpf() == null || cliente.getCpf().length() != 11) {
	        throw new RuntimeException("CPF inválido! O CPF deve ter exatamente 11 dígitos.");
	    }
		
		if (clienteDAO.buscarPorCpf(cliente.getCpf()) != null) {
			throw new RuntimeException("Já existe um cliente cadastrado com o CPF: " + cliente.getCpf());
		}
		clienteDAO.inserir(cliente);
	}

	public Cliente buscarPorCpf(String cpf) {
		Cliente cliente = clienteDAO.buscarPorCpf(cpf);
		if (cliente == null) {
			throw new RuntimeException("Cliente não encontrado com o CPF: " + cpf);
		}
		return cliente;
	}

	/**
	 * Verifica se um cliente existe pelo CPF. Usado pelo PedidoService antes de
	 * registrar um pedido.
	 */
	public boolean existe(String cpf) {
		return clienteDAO.buscarPorCpf(cpf) != null;
	}

	public List<Cliente> listarTodos() {
		return clienteDAO.listarTodos();
	}

	public void atualizar(Cliente cliente) {
		if (clienteDAO.buscarPorCpf(cliente.getCpf()) == null) {
			throw new RuntimeException("Cliente não encontrado para atualização: " + cliente.getCpf());
		}
		clienteDAO.atualizar(cliente);
	}

	public void remover(String cpf) {
		if (clienteDAO.buscarPorCpf(cpf) == null) {
			throw new RuntimeException("Cliente não encontrado: " + cpf);
		}
		try {
			clienteDAO.deletar(cpf);
		} catch (RuntimeException e) {
			if (e.getMessage().contains("Referential integrity")) {
				throw new RuntimeException("Este cliente possui pedidos vinculados e não pode ser removido.");
			}
			throw e;
		}

	}
}
