package br.com.agrobom.controller;

import br.com.agrobom.model.Cliente;
import br.com.agrobom.service.ClienteService;

import java.util.List;

public class ClienteController {
	private ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void cadastrar(Cliente cliente) {
		clienteService.cadastrar(cliente);
	}

	public Cliente buscarPorCpf(String cpf) {
		return clienteService.buscarPorCpf(cpf);
	}

	public boolean existe(String cpf) {
		return clienteService.existe(cpf);
	}

	public List<Cliente> listarTodos() {
		return clienteService.listarTodos();
	}

	public void atualizar(Cliente cliente) {
		clienteService.atualizar(cliente);
	}

	public void remover(String cpf) {
		clienteService.remover(cpf);
	}
}
