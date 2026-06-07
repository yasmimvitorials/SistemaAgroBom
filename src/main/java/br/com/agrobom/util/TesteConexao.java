package br.com.agrobom.util;

import java.sql.Connection;

public class TesteConexao {

	public static void main(String[] args) {
		try {
			Connection conn = ConnectionFactory.getConnection();
			System.out.println("Conexão realizada com sucesso!");
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
