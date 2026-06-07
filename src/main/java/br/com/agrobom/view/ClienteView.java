package br.com.agrobom.view;

import br.com.agrobom.controller.ClienteController;
import br.com.agrobom.model.Cliente;

import java.util.List;

public class ClienteView extends ViewBase{
	private ClienteController controller;

    public ClienteView(ClienteController controller) {
        this.controller = controller;
    }

    public void exibir() {
        int opcao = -1;
        do {
            System.out.println("\n=== CLIENTES ===");
            System.out.println("1. Cadastrar cliente");
            System.out.println("2. Buscar por CPF");
            System.out.println("3. Listar todos");
            System.out.println("4. Atualizar cliente");
            System.out.println("5. Remover cliente");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> buscar();
                case 3 -> listar();
                case 4 -> atualizar();
                case 5 -> remover();
                case 0 -> {}
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("\n-- Cadastrar Cliente --");
        System.out.print("CPF (000.000.000-00): ");
        String cpf = scanner.nextLine().trim();
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine().trim();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine().trim();
        try {
            controller.cadastrar(new Cliente(cpf, nome, telefone, endereco));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscar() {
        System.out.print("\nInforme o CPF: ");
        String cpf = scanner.nextLine().trim();
        try {
            System.out.println(controller.buscarPorCpf(cpf));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listar() {
        List<Cliente> clientes = controller.listarTodos();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.println("\n-- Lista de Clientes --");
        clientes.forEach(System.out::println);
    }

    private void atualizar() {
        System.out.print("\nCPF do cliente a atualizar: ");
        String cpf = scanner.nextLine().trim();
        try {
            Cliente cliente = controller.buscarPorCpf(cpf);
            System.out.println("Cliente encontrado: " + cliente.getNome());
            System.out.print("Novo nome: ");
            String nome = scanner.nextLine().trim();
            System.out.print("Novo telefone: ");
            String telefone = scanner.nextLine().trim();
            System.out.print("Novo endereço: ");
            String endereco = scanner.nextLine().trim();

            if (!nome.isEmpty())     cliente.setNome(nome);
            if (!telefone.isEmpty()) cliente.setTelefone(telefone);
            if (!endereco.isEmpty()) cliente.setEndereco(endereco);

            controller.atualizar(cliente);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void remover() {
        System.out.print("\nCPF do cliente a remover: ");
        String cpf = scanner.nextLine().trim();
        try {
            controller.remover(cpf);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
