package br.com.agrobom.controller;

import br.com.agrobom.model.Fornecedor;
import br.com.agrobom.service.FornecedorService;

import java.util.List;


public class FornecedorController {
	
	private FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    public void cadastrar(Fornecedor fornecedor) {
        fornecedorService.cadastrar(fornecedor);
    }

    public Fornecedor buscarPorCnpj(String cnpj) {
        return fornecedorService.buscarPorCnpj(cnpj);
    }

    public boolean existe(String cnpj) {
        return fornecedorService.existe(cnpj);
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorService.listarTodos();
    }

    public void atualizar(Fornecedor fornecedor) {
        fornecedorService.atualizar(fornecedor);
    }

    public void remover(String cnpj) {
        fornecedorService.remover(cnpj);
    }

    public List<Fornecedor> listarPorProduto(int codigoProduto) {
        return fornecedorService.listarPorProduto(codigoProduto);
    }
}

