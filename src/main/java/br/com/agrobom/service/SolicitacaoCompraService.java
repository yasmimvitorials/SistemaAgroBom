package br.com.agrobom.service;

import br.com.agrobom.dao.FornecedorDAO;
import br.com.agrobom.dao.ProdutoDAO;
import br.com.agrobom.dao.SolicitacaoCompraDAO;
import br.com.agrobom.model.ItemSolicitacao;
import br.com.agrobom.model.Produto;
import br.com.agrobom.model.SolicitacaoCompra;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SolicitacaoCompraService {
	private SolicitacaoCompraDAO solicitacaoDAO;
	private FornecedorDAO fornecedorDAO;
	private ProdutoDAO produtoDAO;

	public SolicitacaoCompraService(SolicitacaoCompraDAO solicitacaoDAO, FornecedorDAO fornecedorDAO,
			ProdutoDAO produtoDAO) {
		this.solicitacaoDAO = solicitacaoDAO;
		this.fornecedorDAO = fornecedorDAO;
		this.produtoDAO = produtoDAO;
	}

	public SolicitacaoCompra realizarSolicitacao(SolicitacaoCompra solicitacao) {
		if (fornecedorDAO.buscarPorCnpj(solicitacao.getFornecedor().getCnpj()) == null) {
			throw new RuntimeException("Fornecedor não cadastrado: " + solicitacao.getFornecedor().getCnpj());
		}

		// verifica se o fornecedor fornece cada produto solicitado
		List<br.com.agrobom.model.Fornecedor> fornecedoresDoProduto;
		for (ItemSolicitacao item : solicitacao.getItens()) {
			Produto produto = produtoDAO.buscarPorCodigo(item.getProduto().getCodigo());
			if (produto == null) {
				throw new RuntimeException("Produto não encontrado: código " + item.getProduto().getCodigo());
			}
			fornecedoresDoProduto = fornecedorDAO.listarPorProduto(item.getProduto().getCodigo());
			boolean fornecedorForneceProduto = fornecedoresDoProduto.stream()
					.anyMatch(f -> f.getCnpj().equals(solicitacao.getFornecedor().getCnpj()));
			if (!fornecedorForneceProduto) {
				throw new RuntimeException("O fornecedor não está associado ao produto: " + produto.getDescricao());
			}
			item.setProduto(produto);

		}

		solicitacao.setSituacao("Aberto");
		solicitacao.setDataEmissao(LocalDate.now());
		solicitacao.setDataEntrega(null);

		double valorTotal = solicitacao.getItens().stream()
				.mapToDouble(i -> i.getPrecoUnitario() * i.getQuantSolicitada()).sum();
		solicitacao.setValorTotal(valorTotal);

		solicitacaoDAO.inserir(solicitacao);
		return solicitacao;
	}

	public void encerrarSolicitacao(int numeroSolicitacao) {
		SolicitacaoCompra solic = solicitacaoDAO.buscarPorNumero(numeroSolicitacao);
		if (solic == null) {
			throw new RuntimeException("Solicitação não encontrada: nº " + numeroSolicitacao);
		}
		if (solic.getSituacao().equalsIgnoreCase("Encerrado")) {
			throw new RuntimeException("Solicitação nº " + numeroSolicitacao + " já está encerrada.");
		}
		solicitacaoDAO.encerrar(numeroSolicitacao);
	}

	public List<SolicitacaoCompra> listarPorMesAno(int mes, int ano) {
		List<SolicitacaoCompra> solicitacoes = solicitacaoDAO.listarPorMesAno(mes, ano);
		if (solicitacoes.isEmpty()) {
			System.out.println("Nenhuma solicitação encontrada para " + mes + "/" + ano);
		}
		return solicitacoes;
	}

	public List<SolicitacaoCompra> listarTodos() {
		return solicitacaoDAO.listarTodos();
	}

	public Map<String, Double> relatorioPorMes() {
		return solicitacaoDAO.relatorioPorMes();
	}
}
