package br.com.agrobom.service;

import br.com.agrobom.dao.ClienteDAO;
import br.com.agrobom.dao.PedidoDAO;
import br.com.agrobom.dao.ProdutoDAO;
import br.com.agrobom.model.ItemPedido;
import br.com.agrobom.model.Pedido;
import br.com.agrobom.model.Produto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedidoService {

	private PedidoDAO pedidoDAO;
	private ProdutoDAO produtoDAO;
	private ClienteDAO clienteDAO;

	public PedidoService(PedidoDAO pedidoDAO, ProdutoDAO produtoDAO, ClienteDAO clienteDAO) {
		this.pedidoDAO = pedidoDAO;
		this.produtoDAO = produtoDAO;
		this.clienteDAO = clienteDAO;
	}

	public Pedido realizarPedido(Pedido pedido) {
		if (clienteDAO.buscarPorCpf(pedido.getCliente().getCpf()) == null) {
			throw new RuntimeException("Cliente não cadastrado. Realize o cadastro antes de fazer um pedido.");
		}

		// Regra 2: filtra apenas itens com estoque suficiente
		List<ItemPedido> itensAceitos = new ArrayList<>();
		List<String> itensRecusados = new ArrayList<>();

		for (ItemPedido item : pedido.getItens()) {
			Produto produto = produtoDAO.buscarPorCodigo(item.getProduto().getCodigo());
			if (produto == null) {
				itensRecusados.add("Produto código " + item.getProduto().getCodigo() + " não encontrado.");
				continue;
			}
			if (produto.getQuantExistente() >= item.getQuantPedida()) {
				item.setProduto(produto); 
				itensAceitos.add(item);
			} else {
				itensRecusados.add(produto.getDescricao() + " — estoque insuficiente " + "(disponível: "
						+ produto.getQuantExistente() + ", pedido: " + item.getQuantPedida() + ")");
			}
		}

		if (itensAceitos.isEmpty()) {
			throw new RuntimeException("Nenhum item do pedido pôde ser atendido por falta de estoque.");
		}

		if (!itensRecusados.isEmpty()) {
			System.out.println("\n Itens não incluídos no pedido por falta de estoque:");
			itensRecusados.forEach(msg -> System.out.println("  - " + msg));
		}

		double valorTotal = itensAceitos.stream().mapToDouble(i -> i.getPrecoUnitario() * i.getQuantPedida()).sum();

		double desconto = pedido.getValorDesconto();
		if (desconto < 0 || desconto > valorTotal) {
			throw new RuntimeException("Valor de desconto inválido: R$ " + desconto);
		}

		pedido.setItens(itensAceitos);
		pedido.setValorTotal(valorTotal - desconto);
		pedido.setDataPedido(LocalDate.now());

		pedidoDAO.inserir(pedido);
		return pedido;
	}

	public Pedido buscarPorNumero(int numeroPedido) {
		Pedido pedido = pedidoDAO.buscarPorNumero(numeroPedido);
		if (pedido == null) {
			throw new RuntimeException("Pedido não encontrado: nº " + numeroPedido);
		}
		return pedido;
	}

	public List<Pedido> listarPorMesAno(int mes, int ano) {
		List<Pedido> pedidos = pedidoDAO.listarPorMesAno(mes, ano);
		if (pedidos.isEmpty()) {
			System.out.println("Nenhum pedido encontrado para " + mes + "/" + ano);
		}
		return pedidos;
	}

	public List<Pedido> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
		if (inicio.isAfter(fim)) {
			throw new RuntimeException("Data de início não pode ser posterior à data de fim.");
		}
		return pedidoDAO.listarPorPeriodo(inicio, fim);
	}

	public List<Pedido> listarTodos() {
		return pedidoDAO.listarTodos();
	}

	public Map<String, Double> relatorioPorMes() {
		return pedidoDAO.relatorioPorMes();
	}
}
