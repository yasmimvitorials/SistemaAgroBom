package br.com.agrobom.dao;


import br.com.agrobom.model.Fornecedor;
import java.util.List;

public interface FornecedorDAO {
	
    void inserir(Fornecedor fornecedor);

    Fornecedor buscarPorCnpj(String cnpj);

    List<Fornecedor> listarTodos();

    void atualizar(Fornecedor fornecedor);

    /**
     * Remove um fornecedor pelo CNPJ.
     * Regra de negócio: só pode excluir se não fornecer nenhum produto ativo.
     */
    void deletar(String cnpj);

    List<Fornecedor> listarPorProduto(int codigoProduto);

}
