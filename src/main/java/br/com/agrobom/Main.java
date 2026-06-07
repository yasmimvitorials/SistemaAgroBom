package br.com.agrobom;

import br.com.agrobom.util.BancoDados;
import br.com.agrobom.view.Menu;

public class Main {
	public static void main(String[] args) {
		BancoDados.inicializar();
		new Menu().exibir();
	}
}