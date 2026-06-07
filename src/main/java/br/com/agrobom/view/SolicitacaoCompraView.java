package br.com.agrobom.view;

import br.com.agrobom.controller.FornecedorController;
import br.com.agrobom.controller.SolicitacaoCompraController;
import br.com.agrobom.model.*;

import java.util.ArrayList;
import java.util.List;

public class SolicitacaoCompraView extends ViewBase{
	private SolicitacaoCompraController solicitacaoController;
    private FornecedorController        fornecedorController;

    public SolicitacaoCompraView(SolicitacaoCompraController solicitacaoController,
                                  FornecedorController fornecedorController) {
        this.solicitacaoController = solicitacaoController;
        this.fornecedorController  = fornecedorController;
    }

    public void exibir() {
        int opcao = -1;
        do {
            System.out.println("\n=== SOLICITAÇÕES DE COMPRA ===");
            System.out.println("1. Registrar solicitação");
            System.out.println("2. Encerrar solicitação");
            System.out.println("3. Listar todas");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1 -> registrar();
                case 2 -> encerrar();
                case 3 -> listar();
                case 0 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void registrar() {
        System.out.println("\n-- Registrar Solicitação de Compra --");
        System.out.print("CNPJ do fornecedor: ");
        String cnpj = scanner.nextLine().trim();
        try {
            Fornecedor fornecedor = fornecedorController.buscarPorCnpj(cnpj);
            SolicitacaoCompra solicitacao = new SolicitacaoCompra();
            solicitacao.setFornecedor(fornecedor);

            List<ItemSolicitacao> itens = new ArrayList<>();
            String continuar = "s";
            while (continuar.equalsIgnoreCase("s")) {
                System.out.print("Código do produto: ");
                int codigoProduto = lerInt();
                System.out.print("Quantidade solicitada: ");
                int quantidade = lerInt();
                System.out.print("Preço unitário: R$ ");
                double preco = lerDouble();

                Produto produto = new Produto();
                produto.setCodigo(codigoProduto);

                ItemSolicitacao item = new ItemSolicitacao();
                item.setProduto(produto);
                item.setQuantSolicitada(quantidade);
                item.setPrecoUnitario(preco);
                itens.add(item);

                System.out.print("Adicionar outro produto? (s/n): ");
                continuar = scanner.nextLine().trim();
            }

            solicitacao.setItens(itens);
            SolicitacaoCompra registrada = solicitacaoController.realizarSolicitacao(solicitacao);
            System.out.println("\nSolicitação registrada com sucesso!");
            System.out.println(registrada);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void encerrar() {
        System.out.print("\nNúmero da solicitação a encerrar: ");
        int numero = lerInt();
        try {
            solicitacaoController.encerrarSolicitacao(numero);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listar() {
        List<SolicitacaoCompra> solicitacoes = solicitacaoController.listarTodos();
        if (solicitacoes.isEmpty()) {
            System.out.println("Nenhuma solicitação registrada.");
            return;
        }
        System.out.println("\n-- Lista de Solicitações --");
        solicitacoes.forEach(System.out::println);
    }
}
