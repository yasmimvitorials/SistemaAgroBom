package br.com.agrobom.view;

import br.com.agrobom.controller.FornecedorController;
import br.com.agrobom.model.Fornecedor;

import java.util.List;

public class FornecedorView extends ViewBase {
	private FornecedorController controller;

    public FornecedorView(FornecedorController controller) {
        this.controller = controller;
    }

    public void exibir() {
        int opcao = -1;
        do {
            System.out.println("\n=== FORNECEDORES ===");
            System.out.println("1. Cadastrar fornecedor");
            System.out.println("2. Buscar por CNPJ");
            System.out.println("3. Listar todos");
            System.out.println("4. Atualizar fornecedor");
            System.out.println("5. Remover fornecedor");
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
        System.out.println("\n-- Cadastrar Fornecedor --");
        System.out.print("CNPJ (00.000.000/0000-00): ");
        String cnpj = scanner.nextLine().trim();
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine().trim();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine().trim();
        try {
            controller.cadastrar(new Fornecedor(cnpj, nome, telefone, endereco));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void buscar() {
        System.out.print("\nInforme o CNPJ: ");
        String cnpj = scanner.nextLine().trim();
        try {
            System.out.println(controller.buscarPorCnpj(cnpj));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listar() {
        List<Fornecedor> fornecedores = controller.listarTodos();
        if (fornecedores.isEmpty()) {
            System.out.println("Nenhum fornecedor cadastrado.");
            return;
        }
        System.out.println("\n-- Lista de Fornecedores --");
        fornecedores.forEach(System.out::println);
    }

    private void atualizar() {
        System.out.print("\nCNPJ do fornecedor a atualizar: ");
        String cnpj = scanner.nextLine().trim();
        try {
            Fornecedor fornecedor = controller.buscarPorCnpj(cnpj);
            System.out.println("Fornecedor encontrado: " + fornecedor.getNome());
            System.out.print("Novo nome: ");
            String nome = scanner.nextLine().trim();
            System.out.print("Novo telefone: ");
            String telefone = scanner.nextLine().trim();
            System.out.print("Novo endereço: ");
            String endereco = scanner.nextLine().trim();

            if (!nome.isEmpty())     fornecedor.setNome(nome);
            if (!telefone.isEmpty()) fornecedor.setTelefone(telefone);
            if (!endereco.isEmpty()) fornecedor.setEndereco(endereco);

            controller.atualizar(fornecedor);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void remover() {
        System.out.print("\nCNPJ do fornecedor a remover: ");
        String cnpj = scanner.nextLine().trim();
        try {
            controller.remover(cnpj);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
