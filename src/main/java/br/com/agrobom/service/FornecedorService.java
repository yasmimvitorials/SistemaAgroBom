package br.com.agrobom.service;

import br.com.agrobom.dao.FornecedorDAO;
import br.com.agrobom.model.Fornecedor;

import java.util.List;

public class FornecedorService {
	
	private FornecedorDAO fornecedorDAO;

    public FornecedorService(FornecedorDAO fornecedorDAO) {
        this.fornecedorDAO = fornecedorDAO;
    }

    public void cadastrar(Fornecedor fornecedor) {
        if (fornecedorDAO.buscarPorCnpj(fornecedor.getCnpj()) != null) {
            throw new RuntimeException("Já existe um fornecedor cadastrado com o CNPJ: " + fornecedor.getCnpj());
        }
        fornecedorDAO.inserir(fornecedor);
    }

    public Fornecedor buscarPorCnpj(String cnpj) {
        Fornecedor fornecedor = fornecedorDAO.buscarPorCnpj(cnpj);
        if (fornecedor == null) {
            throw new RuntimeException("Fornecedor não encontrado com o CNPJ: " + cnpj);
        }
        return fornecedor;
    }

    public boolean existe(String cnpj) {
        return fornecedorDAO.buscarPorCnpj(cnpj) != null;
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorDAO.listarTodos();
    }

    public void atualizar(Fornecedor fornecedor) {
        if (fornecedorDAO.buscarPorCnpj(fornecedor.getCnpj()) == null) {
            throw new RuntimeException("Fornecedor não encontrado para atualização: " + fornecedor.getCnpj());
        }
        fornecedorDAO.atualizar(fornecedor);
    }

    /**
     * Remove um fornecedor pelo CNPJ.
     * Regra: se o fornecedor não fornecer mais nenhum produto,
     * ele deve ser excluído do cadastro.
     */
    public void remover(String cnpj) {
        if (fornecedorDAO.buscarPorCnpj(cnpj) == null) {
            throw new RuntimeException("Fornecedor não encontrado para remoção: " + cnpj);
        }
        List<Fornecedor> fornecedoresDoProduto = fornecedorDAO.listarPorProduto(0);
        boolean aindaFornece = fornecedoresDoProduto.stream()
                .anyMatch(f -> f.getCnpj().equals(cnpj));
        if (aindaFornece) {
            throw new RuntimeException(
                "Este fornecedor ainda fornece produtos ativos. Desassocie os produtos primeiro."
            );
        }
        try {
            fornecedorDAO.deletar(cnpj);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Referential integrity")) {
                throw new RuntimeException("Este fornecedor possui vínculos ativos e não pode ser removido.");
            }
            throw e;
        }
    }

    /**
     * Relatório 4: lista os fornecedores de um produto específico.
     */
    public List<Fornecedor> listarPorProduto(int codigoProduto) {
        List<Fornecedor> fornecedores = fornecedorDAO.listarPorProduto(codigoProduto);
        if (fornecedores.isEmpty()) {
            throw new RuntimeException("Nenhum fornecedor encontrado para o produto código: " + codigoProduto);
        }
        return fornecedores;
    }
}

