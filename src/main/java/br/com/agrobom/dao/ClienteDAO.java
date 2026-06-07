package br.com.agrobom.dao;

import br.com.agrobom.model.Cliente;
import java.util.List;

public interface ClienteDAO {
	
	void inserir(Cliente cliente);

	Cliente buscarPorCpf(String cpf);

	List<Cliente> listarTodos();

	void atualizar(Cliente cliente);

	void deletar(String cpf);

}
