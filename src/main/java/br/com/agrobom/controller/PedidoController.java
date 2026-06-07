package br.com.agrobom.controller;

import br.com.agrobom.model.Pedido;
import br.com.agrobom.service.PedidoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PedidoController {
	private PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	public Pedido realizarPedido(Pedido pedido) {
		return pedidoService.realizarPedido(pedido);
	}

	public Pedido buscarPorNumero(int numeroPedido) {
		return pedidoService.buscarPorNumero(numeroPedido);
	}

	public List<Pedido> listarTodos() {
		return pedidoService.listarTodos();
	}

	public List<Pedido> listarPorMesAno(int mes, int ano) {
		return pedidoService.listarPorMesAno(mes, ano);
	}

	public List<Pedido> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
		return pedidoService.listarPorPeriodo(inicio, fim);
	}

	public Map<String, Double> relatorioPorMes() {
		return pedidoService.relatorioPorMes();
	}
}
