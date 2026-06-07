package br.com.agrobom.view;

import br.com.agrobom.controller.FornecedorController;

import br.com.agrobom.controller.ProdutoController;
import br.com.agrobom.model.Produto;

import java.util.List;

public class ProdutoView extends ViewBase{
	private ProdutoController produtoController;
    private FornecedorController fornecedorController;

    public ProdutoView(ProdutoController produtoController, FornecedorController fornecedorController) {
        this.produtoController    = produtoController;
        this.fornecedorController = fornecedorController;
    }

    public void exibir() {
        int opcao = -1;
        do {
            System.out.println("\n=== PRODUTOS ===");
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Buscar por código");
            System.out.println("3. Listar todos");
            System.out.println("4. Atualizar produto");
            System.out.println("5. Remover produto");
            System.out.println("6. Associar fornecedor");
            System.out.println("7. Desassociar fornecedor");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> buscar();
                case 3 -> listar();
                case 4 -> atualizar();
                case 5 -> remover();
                case 6 -> associarFornecedor();
                case 7 -> desassociarFornecedor();
                case 0 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("\n-- Cadastrar Produto --");
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();
        System.out.print("Quantidade em estoque: ");
        int quantExistente = lerInt();
        System.out.print("Quantidade mínima: ");
        int quantMinima = lerInt();
        System.out.print("Unidade de medida (kg, litro, etc): ");
        String unidade = scanner.nextLine().trim();
        try {
            produtoController.cadastrar(new Produto(0, descricao, quantExistente, quantMinima, unidade));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscar() {
        System.out.print("\nCódigo do produto: ");
        int codigo = lerInt();
        try {
            System.out.println(produtoController.buscarPorCodigo(codigo));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listar() {
        List<Produto> produtos = produtoController.listarTodos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.println("\n-- Lista de Produtos --");
        produtos.forEach(System.out::println);
    }

    private void atualizar() {
        System.out.print("\nCódigo do produto a atualizar: ");
        int codigo = lerInt();
        try {
            Produto produto = produtoController.buscarPorCodigo(codigo);
            System.out.println("Produto encontrado: " + produto.getDescricao());
            System.out.print("Nova descrição (Enter para manter): ");
            String descricao = scanner.nextLine().trim();
            System.out.print("Nova quantidade em estoque (Enter para manter): ");
            String qtdStr = scanner.nextLine().trim();
            System.out.print("Nova quantidade mínima (Enter para manter): ");
            String minStr = scanner.nextLine().trim();
            System.out.print("Nova unidade de medida (Enter para manter): ");
            String unidade = scanner.nextLine().trim();

            if (!descricao.isEmpty()) produto.setDescricao(descricao);
            if (!qtdStr.isEmpty())    produto.setQuantExistente(Integer.parseInt(qtdStr));
            if (!minStr.isEmpty())    produto.setQuantMinima(Integer.parseInt(minStr));
            if (!unidade.isEmpty())   produto.setUnidadeMedida(unidade);

            produtoController.atualizar(produto);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void remover() {
        System.out.print("\nCódigo do produto a remover: ");
        int codigo = lerInt();
        try {
            produtoController.remover(codigo);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void associarFornecedor() {
        System.out.print("\nCódigo do produto: ");
        int codigo = lerInt();
        System.out.print("CNPJ do fornecedor: ");
        String cnpj = scanner.nextLine().trim();
        try {
            produtoController.associarFornecedor(codigo, cnpj);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void desassociarFornecedor() {
        System.out.print("\nCódigo do produto: ");
        int codigo = lerInt();
        System.out.print("CNPJ do fornecedor: ");
        String cnpj = scanner.nextLine().trim();
        try {
            produtoController.desassociarFornecedor(codigo, cnpj);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
