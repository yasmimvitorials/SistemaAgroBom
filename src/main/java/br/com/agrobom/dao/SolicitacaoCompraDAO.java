package br.com.agrobom.dao;

import br.com.agrobom.model.SolicitacaoCompra;
import java.util.List;
import java.util.Map;

public interface SolicitacaoCompraDAO {
	
   void inserir(SolicitacaoCompra solicitacao);

   SolicitacaoCompra buscarPorNumero(int numeroSolicitacao);

   List<SolicitacaoCompra> listarPorMesAno(int mes, int ano);

   List<SolicitacaoCompra> listarTodos();

   /**
    * Encerra uma solicitação (muda situação para ENCERRADA
    * e registra a data de entrega).
    */
   void encerrar(int numeroSolicitacao);

   Map<String, Double> relatorioPorMes();

}
