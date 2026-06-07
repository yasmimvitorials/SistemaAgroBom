package br.com.agrobom.model;

public class ItemSolicitacao {

	private int numeroSolicitacao;
	private int quantSolicitada;
	private double precoUnitario;
	private Produto produto;

	public ItemSolicitacao(int numeroSolicitacao, int quantSolicitada, Produto produto, double precoUnitario) {
		super();
		this.numeroSolicitacao = numeroSolicitacao;
		this.quantSolicitada = quantSolicitada;
		this.setProduto(produto);
		this.precoUnitario = precoUnitario;
	}

	public ItemSolicitacao() {

	}
	
	@Override
	public String toString() {
		return "ItemSolicitacao [numeroSolicitacao=" + numeroSolicitacao + ", quantSolicitada=" + quantSolicitada
				+ ", precoUnitario=" + precoUnitario + ", produto=" + produto + "]";
	}
	
	public int getQuantSolicitada() {
		return quantSolicitada;
	}

	public void setQuantSolicitada(int quantSolicitada) {
		this.quantSolicitada = quantSolicitada;
	}

	public int getNumeroSolicitacao() {
		return numeroSolicitacao;
	}

	public void setNumeroSolicitacao(int numeroSolicitacao) {
		this.numeroSolicitacao = numeroSolicitacao;
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
