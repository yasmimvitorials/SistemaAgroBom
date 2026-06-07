package br.com.agrobom.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoCompra {

	private int numeroSolicitacao;
	private LocalDate dataEmissao;
	private LocalDate dataEntrega;
	private String situacao;
	private double valorTotal;
	private Fornecedor fornecedor;
	private List<ItemSolicitacao> itens = new ArrayList<>();
	

	public SolicitacaoCompra(int numeroSolicitacao, LocalDate dataEmissao, LocalDate dataEntrega, String situacao,
			double valorTotal, Fornecedor fornecedor) {
		super();
		this.numeroSolicitacao = numeroSolicitacao;
		this.dataEmissao = dataEmissao;
		this.dataEntrega = dataEntrega;
		this.situacao = situacao;
		this.valorTotal = valorTotal;
		this.fornecedor = fornecedor;
	}
	
	public SolicitacaoCompra() {
		this.itens = new ArrayList<>();
	}
	
	@Override
    public String toString() {

        DateTimeFormatter formatoBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return "======= Solicitação de Compra =======" +
                "Solicitação n°: " + numeroSolicitacao + "\n" +
                "Fornecedor: " + fornecedor + "\n" +
                "Data da solicitação: " + dataEmissao.format(formatoBR) + "\n" +
                "--------------------------------\n" +
                "Itens: " + itens + "\n" +
                "--------------------------------\n" +
                "Resumo Financeiro:\n" +
                "Valor total: " + valorTotal + "\n" +
                "--------------------------------\n" +
                "Detalhes:\n" +
                "Data de entrega: " + dataEntrega.format(formatoBR) + "\n" +
                "Situação: " + situacao + "\n" +
                "==================================";
    }

	public int getNumeroSolicitacao() {
		return numeroSolicitacao;
	}

	public void setNumeroSolicitacao(int numeroSolicitacao) {
		this.numeroSolicitacao = numeroSolicitacao;
	}

	public LocalDate getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(LocalDate dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List<ItemSolicitacao> getItens() {
		return itens;
	}

	public void setItens(List<ItemSolicitacao> itens) {
		this.itens = itens;
	}

}
