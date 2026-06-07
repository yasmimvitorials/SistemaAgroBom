package br.com.agrobom.model;

public class Produto {

	private int codigo;
	private String descricao;
	private int quantExistente;
	private int quantMinima;
	private String unidadeMedida;

	public Produto(int codigo, String descricao, int quantExistente, int quantMinima, String quantMedida) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
		this.quantExistente = quantExistente;
		this.quantMinima = quantMinima;
		this.unidadeMedida = quantMedida;
	}
	
	public Produto() {
		
	}
	
	@Override
    public String toString() {
        return "========== PRODUTO ==========" +
                "Código: " + codigo + "\n" +
                "Descrição: " + descricao + "\n" +
                "Quantidade existente: " + quantExistente + "\n" +
                "Quantidade mínima: " + quantMinima + "\n" +
                "Valor medido: " + unidadeMedida + "\n" +
                "============================";
    }

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQuantExistente() {
		return quantExistente;
	}

	public void setQuantExistente(int quantExistente) {
		this.quantExistente = quantExistente;
	}

	public int getQuantMinima() {
		return quantMinima;
	}

	public void setQuantMinima(int quantMinima) {
		this.quantMinima = quantMinima;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadetMedida) {
		this.unidadeMedida = unidadetMedida;
	}

}
