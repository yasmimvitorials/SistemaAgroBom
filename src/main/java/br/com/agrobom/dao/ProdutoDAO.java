package br.com.agrobom.dao;

import br.com.agrobom.model.Produto;
import java.util.List;

public interface ProdutoDAO {

    void inserir(Produto produto);

    Produto buscarPorCodigo(int codigo);

    List<Produto> listarTodos();

    void atualizar(Produto produto);

    void deletar(int codigo);

    /**
     * Retorna produtos com estoque abaixo da quantidade mínima.
     * Usado para disparar solicitações de compra.
     */
    List<Produto> listarEmEstoqueCritico();

    void associarFornecedor(int codigoProduto, String cnpjFornecedor);

    void desassociarFornecedor(int codigoProduto, String cnpjFornecedor);
}
