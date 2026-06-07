package br.com.agrobom.controller;

import br.com.agrobom.model.SolicitacaoCompra;
import br.com.agrobom.service.SolicitacaoCompraService;

import java.util.List;
import java.util.Map;

public class SolicitacaoCompraController {

	private SolicitacaoCompraService solicitacaoService;

	public SolicitacaoCompraController(SolicitacaoCompraService solicitacaoService) {
		this.solicitacaoService = solicitacaoService;
	}

	public SolicitacaoCompra realizarSolicitacao(SolicitacaoCompra solicitacao) {
		return solicitacaoService.realizarSolicitacao(solicitacao);
	}

	public void encerrarSolicitacao(int numeroSolicitacao) {
		solicitacaoService.encerrarSolicitacao(numeroSolicitacao);
	}

	public List<SolicitacaoCompra> listarTodos() {
		return solicitacaoService.listarTodos();
	}

	public List<SolicitacaoCompra> listarPorMesAno(int mes, int ano) {
		return solicitacaoService.listarPorMesAno(mes, ano);
	}

	public Map<String, Double> relatorioPorMes() {
		return solicitacaoService.relatorioPorMes();
	}
}