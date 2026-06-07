package br.com.agrobom.model;

public class Fornecedor {

	private String cnpj;
	private String nome;
	private String endereco;
	private String telefone;
	 
	public Fornecedor(String cnpj, String nome, String endereco, String telefone) {
		super();
		this.cnpj = cnpj;
		this.nome = nome;
		this.endereco = endereco;
		this.telefone = telefone;
	}
	
	public Fornecedor() {
		
	}

	 @Override
	    public String toString() {
	        return "===== DADOS DO FORNECEDOR =====\n" +
	                "CNPJ: " + cnpj + "\n" +
	                "Nome: " + nome + "\n" +
	                "Endereco: " + endereco + "\n" +
	                "Telefone: " + telefone + "\n" +
	                "=============================";
	    }
	 
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
