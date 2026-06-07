package br.com.agrobom.service;

import br.com.agrobom.dao.ProdutoDAO;
import br.com.agrobom.model.Produto;

import java.util.List;

public class ProdutoService {

	
	private ProdutoDAO produtoDAO;

    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void cadastrar(Produto produto) {
        if (produto.getQuantMinima() <= 0) {
            throw new RuntimeException("A quantidade mínima do produto deve ser maior que zero.");
        }
        produtoDAO.inserir(produto);
    }

    public Produto buscarPorCodigo(int codigo) {
        Produto produto = produtoDAO.buscarPorCodigo(codigo);
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado com o código: " + codigo);
        }
        return produto;
    }

    public boolean temEstoqueSuficiente(int codigoProduto, int quantidadeDesejada) {
        Produto produto = produtoDAO.buscarPorCodigo(codigoProduto);
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado: " + codigoProduto);
        }
        return produto.getQuantExistente() >= quantidadeDesejada;
    }

    public List<Produto> listarTodos() {
        return produtoDAO.listarTodos();
    }

    /**
     * Lista produtos com estoque abaixo do mínimo ideal.
     * Usado para alertar o balconista e disparar solicitações de compra.
     */
    public List<Produto> listarEmEstoqueCritico() {
        return produtoDAO.listarEmEstoqueCritico();
    }

    public void atualizar(Produto produto) {
        if (produtoDAO.buscarPorCodigo(produto.getCodigo()) == null) {
            throw new RuntimeException("Produto não encontrado para atualização: " + produto.getCodigo());
        }
        produtoDAO.atualizar(produto);
    }

    public void remover(int codigo) {
        if (produtoDAO.buscarPorCodigo(codigo) == null) {
            throw new RuntimeException("Produto não encontrado: " + codigo);
        }
        try {
            produtoDAO.deletar(codigo);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Referential integrity")) {
                throw new RuntimeException("Este produto está vinculado a pedidos ou solicitações e não pode ser removido.");
            }
            throw e;
        }
    }

    public void associarFornecedor(int codigoProduto, String cnpjFornecedor) {
        if (produtoDAO.buscarPorCodigo(codigoProduto) == null) {
            throw new RuntimeException("Produto não encontrado: " + codigoProduto);
        }
        produtoDAO.associarFornecedor(codigoProduto, cnpjFornecedor);
    }

    public void desassociarFornecedor(int codigoProduto, String cnpjFornecedor) {
        produtoDAO.desassociarFornecedor(codigoProduto, cnpjFornecedor);
    }
}

