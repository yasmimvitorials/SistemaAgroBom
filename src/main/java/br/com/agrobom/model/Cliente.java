package br.com.agrobom.model;

public class Cliente {

	private String cpf;
	private String nome;
	private String endereco;
	private String telefone;

	public Cliente(String cpf, String nome, String telefone, String endereco) {
		super();
		this.cpf = cpf;
		this.nome = nome;
		this.telefone = telefone;
		this.endereco = endereco;
		
	}

	public Cliente() {

	}
	
	 @Override
	    public String toString() {
	        return "===== DADOS DO CLIENTE =====\n" +
	                "CPF: " + cpf + "\n" +
	                "Nome: " + nome + "\n" +
	                "Endereco: " + endereco + "\n" +
	                "Telefone: " + telefone + "\n" +
	                "===========================";
	    }

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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
