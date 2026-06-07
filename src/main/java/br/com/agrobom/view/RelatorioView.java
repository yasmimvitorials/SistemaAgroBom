package br.com.agrobom.view;

import br.com.agrobom.controller.FornecedorController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import br.com.agrobom.controller.PedidoController;
import br.com.agrobom.controller.ProdutoController;
import br.com.agrobom.controller.SolicitacaoCompraController;
import br.com.agrobom.model.*;

public class RelatorioView extends ViewBase {
	private ProdutoController          produtoController;
    private FornecedorController       fornecedorController;
    private PedidoController           pedidoController;
    private SolicitacaoCompraController solicitacaoController;

    public RelatorioView(ProdutoController produtoController,
                         FornecedorController fornecedorController,
                         PedidoController pedidoController,
                         SolicitacaoCompraController solicitacaoController) {
        this.produtoController     = produtoController;
        this.fornecedorController  = fornecedorController;
        this.pedidoController      = pedidoController;
        this.solicitacaoController = solicitacaoController;
    }

    public void exibir() {
        int opcao = -1;
        do {
            System.out.println("\n=== RELATÓRIOS ===");
            System.out.println("1. Posição do estoque (Rel. 1)");
            System.out.println("2. Pedidos por mês/ano (Rel. 2)");
            System.out.println("3. Pedidos por período (Rel. 3)");
            System.out.println("4. Fornecedores por produto (Rel. 4)");
            System.out.println("5. Solicitações por mês/ano (Rel. 5)");
            System.out.println("6. Volume financeiro últimos 12 meses (Rel. 6)");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1 -> relatorioEstoque();
                case 2 -> relatorioPedidosMesAno();
                case 3 -> relatorioPedidosPeriodo();
                case 4 -> relatorioFornecedoresPorProduto();
                case 5 -> relatorioSolicitacoesMesAno();
                case 6 -> relatorioVolumeFinanceiro();
                case 0 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void relatorioEstoque() {
        System.out.println("\n=== RELATÓRIO 1 — POSIÇÃO DO ESTOQUE ===");
        List<Produto> produtos = produtoController.listarTodos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        produtos.forEach(System.out::println);

        List<Produto> criticos = produtoController.listarEmEstoqueCritico();
        if (!criticos.isEmpty()) {
            System.out.println("\n Produtos em estoque crítico:");
            criticos.forEach(p -> System.out.println(
                "  - " + p.getDescricao()
                + " (existente: " + p.getQuantExistente()
                + " / mínimo: "   + p.getQuantMinima() + ")"
            ));
        }
    }

    private void relatorioPedidosMesAno() {
        System.out.println("\n=== RELATÓRIO 2 — PEDIDOS POR MÊS/ANO ===");
        System.out.print("Mês (1-12): ");
        int mes = lerInt();
        System.out.print("Ano: ");
        int ano = lerInt();
        List<Pedido> pedidos = pedidoController.listarPorMesAno(mes, ano);
        if (pedidos.isEmpty()) return;
        pedidos.forEach(System.out::println);
    }

    private void relatorioPedidosPeriodo() {
        System.out.println("\n=== RELATÓRIO 3 — PEDIDOS POR PERÍODO ===");
        System.out.print("Data início (dd/MM/yyyy): ");
        LocalDate inicio = lerData();
        System.out.print("Data fim (dd/MM/yyyy): ");
        LocalDate fim = lerData();
        try {
            List<Pedido> pedidos = pedidoController.listarPorPeriodo(inicio, fim);
            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido no período.");
                return;
            }
            System.out.println("\nNº Pedido | Valor Total  | Desconto");
            System.out.println("----------|--------------|----------");
            pedidos.forEach(p -> System.out.printf(
                "%-10d| R$ %9.2f | R$ %7.2f%n",
                p.getNumeroPedido(), p.getValorTotal(), p.getValorDesconto()
            ));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void relatorioFornecedoresPorProduto() {
        System.out.println("\n=== RELATÓRIO 4 — FORNECEDORES POR PRODUTO ===");
        System.out.print("Código do produto: ");
        int codigo = lerInt();
        try {
            List<Fornecedor> fornecedores = fornecedorController.listarPorProduto(codigo);
            System.out.println("\nFornecedores do produto código " + codigo + ":");
            fornecedores.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void relatorioSolicitacoesMesAno() {
        System.out.println("\n=== RELATÓRIO 5 — SOLICITAÇÕES POR MÊS/ANO ===");
        System.out.print("Mês (1-12): ");
        int mes = lerInt();
        System.out.print("Ano: ");
        int ano = lerInt();
        List<SolicitacaoCompra> solicitacoes = solicitacaoController.listarPorMesAno(mes, ano);
        if (solicitacoes.isEmpty()) return;
        solicitacoes.forEach(System.out::println);
    }

    private void relatorioVolumeFinanceiro() {
        System.out.println("\n=== RELATÓRIO 6 — VOLUME FINANCEIRO ÚLTIMOS 12 MESES ===");
        Map<String, Double> pedidos      = pedidoController.relatorioPorMes();
        Map<String, Double> solicitacoes = solicitacaoController.relatorioPorMes();

        Set<String> meses = new TreeSet<>();
        meses.addAll(pedidos.keySet());
        meses.addAll(solicitacoes.keySet());

        if (meses.isEmpty()) {
            System.out.println("Nenhum dado encontrado nos últimos 12 meses.");
            return;
        }

        System.out.println("\n  Mês/Ano   | Pedidos (R$)  | Solicitações (R$)");
        System.out.println("  ----------|---------------|------------------");
        for (String mes : meses) {
            double totalPedidos      = pedidos.getOrDefault(mes, 0.0);
            double totalSolicitacoes = solicitacoes.getOrDefault(mes, 0.0);
            System.out.printf("  %-10s| %13.2f | %17.2f%n", mes, totalPedidos, totalSolicitacoes);
        }
    }
}
