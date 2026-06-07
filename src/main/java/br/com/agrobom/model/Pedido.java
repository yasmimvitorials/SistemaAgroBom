package br.com.agrobom.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

	private int numeroPedido;
	private LocalDate dataPedido;
	private double valorTotal;
	private double valorDesconto;
	private Cliente cliente;
	private List<ItemPedido> itens = new ArrayList<>();

	public Pedido(int numeroPedido, LocalDate dataPedido, double valorTotal, double valorDesconto, Cliente cliente) {
		super();
		this.numeroPedido = numeroPedido;
		this.dataPedido = dataPedido;
		this.valorTotal = valorTotal;
		this.valorDesconto = valorDesconto;
		this.cliente = cliente;
	}

	public Pedido() {
		this.itens = new ArrayList<>();

	}
	
	 @Override
	    public String toString() {

	        DateTimeFormatter formatoBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	        return "========== PEDIDO N° " + numeroPedido + " ==========\n" +
	                "Data: " + dataPedido.format(formatoBR) + "\n" +
	                "Cliente: " + cliente + (cliente != null ? cliente.getNome() : "Não informado") + "\n" +
	                "------------------------------\n" +
	                "Itens: \n" + itens + "\n" +
	                "-------------------------------------------\n" +
	                "Resumo Financeiro:\n" +
	                "Subtotal: R$ " + valorTotal + "\n" +
	                "Desconto: R$ " + valorDesconto + "\n" +
	                "Total Final: R$ " + (valorTotal - valorDesconto) + "\n" +
	                "===========================================";
	    }


	public int getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public LocalDate getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDate dataPedido) {
		this.dataPedido = dataPedido;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

}
