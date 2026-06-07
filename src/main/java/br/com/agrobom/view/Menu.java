package br.com.agrobom.view;

import br.com.agrobom.controller.*;
import br.com.agrobom.dao.impl.*;
import br.com.agrobom.service.*;

public class Menu extends ViewBase {
	 private final ClienteDAOImpl           clienteDAO      = new ClienteDAOImpl();
	    private final FornecedorDAOImpl        fornecedorDAO   = new FornecedorDAOImpl();
	    private final ProdutoDAOImpl           produtoDAO      = new ProdutoDAOImpl();
	    private final PedidoDAOImpl            pedidoDAO       = new PedidoDAOImpl();
	    private final SolicitacaoCompraDAOImpl solicitacaoDAO  = new SolicitacaoCompraDAOImpl();

	    // Services
	    private final ClienteService      clienteService      = new ClienteService(clienteDAO);
	    private final FornecedorService   fornecedorService   = new FornecedorService(fornecedorDAO);
	    private final ProdutoService      produtoService      = new ProdutoService(produtoDAO);
	    private final PedidoService       pedidoService       = new PedidoService(pedidoDAO, produtoDAO, clienteDAO);
	    private final SolicitacaoCompraService solicitacaoService =
	            new SolicitacaoCompraService(solicitacaoDAO, fornecedorDAO, produtoDAO);

	    // Controllers
	    private final ClienteController      clienteController      = new ClienteController(clienteService);
	    private final FornecedorController   fornecedorController   = new FornecedorController(fornecedorService);
	    private final ProdutoController      produtoController      = new ProdutoController(produtoService);
	    private final PedidoController       pedidoController       = new PedidoController(pedidoService);
	    private final SolicitacaoCompraController solicitacaoController =
	            new SolicitacaoCompraController(solicitacaoService);

	    // Views
	    private final ClienteView      clienteView      = new ClienteView(clienteController);
	    private final FornecedorView   fornecedorView   = new FornecedorView(fornecedorController);
	    private final ProdutoView      produtoView      = new ProdutoView(produtoController, fornecedorController);
	    private final PedidoView       pedidoView       = new PedidoView(pedidoController, clienteController);
	    private final SolicitacaoCompraView solicitacaoView =
	            new SolicitacaoCompraView(solicitacaoController, fornecedorController);
	    private final RelatorioView    relatorioView    =
	            new RelatorioView(produtoController, fornecedorController, pedidoController, solicitacaoController);

	    public void exibir() {
	        int opcao = -1;
	        do {
	            System.out.println("\n╔══════════════════════════════╗");
	            System.out.println("║       SISTEMA AGROBOM        ║");
	            System.out.println("╠══════════════════════════════╣");
	            System.out.println("║  1. Clientes                 ║");
	            System.out.println("║  2. Fornecedores             ║");
	            System.out.println("║  3. Produtos                 ║");
	            System.out.println("║  4. Pedidos                  ║");
	            System.out.println("║  5. Solicitações de Compra   ║");
	            System.out.println("║  6. Relatórios               ║");
	            System.out.println("║  0. Sair                     ║");
	            System.out.println("╚══════════════════════════════╝");
	            System.out.print("Opção: ");
	            opcao = lerInt();

	            switch (opcao) {
	                case 1 -> clienteView.exibir();
	                case 2 -> fornecedorView.exibir();
	                case 3 -> produtoView.exibir();
	                case 4 -> pedidoView.exibir();
	                case 5 -> solicitacaoView.exibir();
	                case 6 -> relatorioView.exibir();
	                case 0 -> System.out.println("\nSistema encerrado. Até logo!");
	                default -> System.out.println("Opção inválida.");
	            }
	        } while (opcao != 0);
	    }
	}
