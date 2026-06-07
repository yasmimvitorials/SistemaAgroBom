package br.com.agrobom.view;

import br.com.agrobom.controller.ClienteController;
import br.com.agrobom.controller.PedidoController;
import br.com.agrobom.model.*;

import java.util.ArrayList;
import java.util.List;

public class PedidoView extends ViewBase {
	private PedidoController   pedidoController;
    private ClienteController  clienteController;

    public PedidoView(PedidoController pedidoController, ClienteController clienteController) {
        this.pedidoController  = pedidoController;
        this.clienteController = clienteController;
    }

    public void exibir() {
        int opcao = -1;
        do {
            System.out.println("\n=== PEDIDOS ===");
            System.out.println("1. Registrar novo pedido");
            System.out.println("2. Buscar pedido por número");
            System.out.println("3. Listar todos");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1 -> registrar();
                case 2 -> buscar();
                case 3 -> listar();
                case 0 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void registrar() {
        System.out.println("\n-- Registrar Pedido --");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine().trim();
        try {
            Cliente cliente = clienteController.buscarPorCpf(cpf);
            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);

            List<ItemPedido> itens = new ArrayList<>();
            String continuar = "s";
            while (continuar.equalsIgnoreCase("s")) {
                System.out.print("Código do produto: ");
                int codigoProduto = lerInt();
                System.out.print("Quantidade: ");
                int quantidade = lerInt();
                System.out.print("Preço unitário: R$ ");
                double preco = lerDouble();

                Produto produto = new Produto();
                produto.setCodigo(codigoProduto);

                ItemPedido item = new ItemPedido();
                item.setProduto(produto);
                item.setQuantPedida(quantidade);
                item.setPrecoUnitario(preco);
                itens.add(item);

                System.out.print("Adicionar outro produto? (s/n): ");
                continuar = scanner.nextLine().trim();
            }

            System.out.print("Valor do desconto (0 para sem desconto): R$ ");
            double desconto = lerDouble();

            pedido.setItens(itens);
            pedido.setValorDesconto(desconto);

            Pedido registrado = pedidoController.realizarPedido(pedido);
            System.out.println("\nPedido registrado com sucesso!");
            System.out.println(registrado);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscar() {
        System.out.print("\nNúmero do pedido: ");
        int numero = lerInt();
        try {
            System.out.println(pedidoController.buscarPorNumero(numero));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listar() {
        List<Pedido> pedidos = pedidoController.listarTodos();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
            return;
        }
        System.out.println("\n-- Lista de Pedidos --");
        pedidos.forEach(System.out::println);
    }
}
