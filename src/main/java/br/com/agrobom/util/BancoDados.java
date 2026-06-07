package br.com.agrobom.util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class BancoDados {

	private BancoDados() {
	}

	public static void inicializar() {
		Connection conn = null;
		try {
			conn = ConnectionFactory.getConnection();
			Statement stmt = conn.createStatement();
			criarTabelas(stmt);
			System.out.println("Banco de dados inicializado com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inicializar banco de dados: " + e.getMessage(), e);
		} finally {
			ConnectionFactory.closeConnection(conn);
		}
	}

	private static void criarTabelas(Statement stmt) throws SQLException {

		stmt.execute("""
				    CREATE TABLE IF NOT EXISTS cliente (
				        cpf       VARCHAR(14)  NOT NULL,
				        nome      VARCHAR(100) NOT NULL,
				        telefone  VARCHAR(15)  NOT NULL,
				        endereco  VARCHAR(200) NOT NULL,
				        PRIMARY KEY (cpf)
				    )
				""");

		stmt.execute("""
				    CREATE TABLE IF NOT EXISTS fornecedor (
				        cnpj      VARCHAR(18)  NOT NULL,
				        nome      VARCHAR(100) NOT NULL,
				        telefone  VARCHAR(15)  NOT NULL,
				        endereco  VARCHAR(200) NOT NULL,
				        PRIMARY KEY (cnpj)
				    )
				""");

		stmt.execute("""
				    CREATE TABLE IF NOT EXISTS produto (
				        codigo          INT          NOT NULL AUTO_INCREMENT,
				        descricao       VARCHAR(100) NOT NULL,
				        quant_existente INT          NOT NULL DEFAULT 0,
				        quant_minima    INT          NOT NULL,
				        unidade_medida  VARCHAR(20)  NOT NULL,
				        PRIMARY KEY (codigo)
				    )
				""");

		stmt.execute("""
				    CREATE TABLE IF NOT EXISTS fornecido_por (
				        codigo INT         NOT NULL,
				        cnpj   VARCHAR(18) NOT NULL,
				        PRIMARY KEY (codigo, cnpj),
				        CONSTRAINT fk_produto_fornecido   FOREIGN KEY (codigo) REFERENCES produto (codigo),
				        CONSTRAINT fk_fornecedor_fornecido FOREIGN KEY (cnpj)   REFERENCES fornecedor (cnpj)
				    )
				""");

		stmt.execute("""
				    CREATE TABLE IF NOT EXISTS pedido (
				        numero_pedido  INT            NOT NULL AUTO_INCREMENT,
				        valor_desconto DECIMAL(10, 2) DEFAULT 0.00,
				        valor_total    DECIMAL(10, 2) NOT NULL,
				        data_pedido    DATE           NOT NULL,
				        cpf_cliente    VARCHAR(14)    NOT NULL,
				        PRIMARY KEY (numero_pedido),
				        CONSTRAINT fk_cliente_pedido FOREIGN KEY (cpf_cliente) REFERENCES cliente (cpf)
				    )
				""");

		stmt.execute("""
				    CREATE TABLE IF NOT EXISTS contem (
				        numero_pedido  INT            NOT NULL,
				        codigo         INT            NOT NULL,
				        quant_pedida   INT            NOT NULL,
				        preco_unitario DECIMAL(10, 2) NOT NULL,
				        PRIMARY KEY (numero_pedido, codigo),
				        CONSTRAINT fk_pedido  FOREIGN KEY (numero_pedido) REFERENCES pedido (numero_pedido),
				        CONSTRAINT fk_produto FOREIGN KEY (codigo)        REFERENCES produto (codigo)
				    )
				""");

		stmt.execute("""
				    CREATE TABLE IF NOT EXISTS solicitacao_compra (
				        numero_solicitacao INT            NOT NULL AUTO_INCREMENT,
				        data_emissao       DATE           NOT NULL,
				        data_entrega       DATE           DEFAULT NULL,
				        situacao           VARCHAR(10)    NOT NULL DEFAULT 'Aberto',
				        valor_total        DECIMAL(10, 2) NOT NULL,
				        cnpj_fornecedor    VARCHAR(18)    NOT NULL,
				        PRIMARY KEY (numero_solicitacao),
				        CONSTRAINT fk_fornecedor_solic FOREIGN KEY (cnpj_fornecedor) REFERENCES fornecedor (cnpj)
				    )
				""");

		stmt.execute(
				"""
						    CREATE TABLE IF NOT EXISTS item_solicitacao (
						        numero_solicitacao INT            NOT NULL,
						        codigo             INT            NOT NULL,
						        quant_solicitada   INT            NOT NULL,
						        preco_unitario     DECIMAL(10, 2) NOT NULL,
						        PRIMARY KEY (numero_solicitacao, codigo),
						        CONSTRAINT fk_solicitacao   FOREIGN KEY (numero_solicitacao) REFERENCES solicitacao_compra (numero_solicitacao),
						        CONSTRAINT fk_produto_solic FOREIGN KEY (codigo)             REFERENCES produto (codigo)
						    )
						""");
	}
}
