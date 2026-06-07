package br.com.agrobom.model;

public class ItemPedido {

	private int quantPedida;
	private int numeroPedido;
	private double precoUnitario;
	private Produto produto;

	public ItemPedido(int quantPedida, Produto produto, int numeroPedido, double precoUnitario) {
		super();
		this.quantPedida = quantPedida;
		this.setProduto(produto);
		this.numeroPedido = numeroPedido;
		this.precoUnitario = precoUnitario;
	}

	public ItemPedido() {

	}
	
	@Override
	public String toString() {
		return "ItemPedido [quantPedida=" + quantPedida + ", numeroPedido=" + numeroPedido + ", precoUnitario="
				+ precoUnitario + ", produto=" + produto + "]";
	}

	public int getQuantPedida() {
		return quantPedida;
	}

	public void setQuantPedida(int quantPedida) {
		this.quantPedida = quantPedida;
	}

	public int getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public double getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

}
