package br.com.agrobom.dao;

import br.com.agrobom.model.Pedido;
import java.time.LocalDate;
import java.util.List;


public interface PedidoDAO {
	
    void inserir(Pedido pedido);

    Pedido buscarPorNumero(int numeroPedido);

    List<Pedido> listarPorMesAno(int mes, int ano);

    List<Pedido> listarPorPeriodo(LocalDate inicio, LocalDate fim);

    List<Pedido> listarTodos();

    java.util.Map<String, Double> relatorioPorMes();
}
