package br.com.agrobom.controller;

import br.com.agrobom.model.Produto;
import br.com.agrobom.service.ProdutoService;

import java.util.List;

public class ProdutoController {

	private ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	public void cadastrar(Produto produto) {
		produtoService.cadastrar(produto);
	}

	public Produto buscarPorCodigo(int codigo) {
		return produtoService.buscarPorCodigo(codigo);
	}

	public List<Produto> listarTodos() {
		return produtoService.listarTodos();
	}

	public List<Produto> listarEmEstoqueCritico() {
		return produtoService.listarEmEstoqueCritico();
	}

	public void atualizar(Produto produto) {
		produtoService.atualizar(produto);
	}

	public void remover(int codigo) {
		produtoService.remover(codigo);
	}

	public void associarFornecedor(int codigoProduto, String cnpjFornecedor) {
		produtoService.associarFornecedor(codigoProduto, cnpjFornecedor);
	}

	public void desassociarFornecedor(int codigoProduto, String cnpjFornecedor) {
		produtoService.desassociarFornecedor(codigoProduto, cnpjFornecedor);
	}
}
