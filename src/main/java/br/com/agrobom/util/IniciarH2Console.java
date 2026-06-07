package br.com.agrobom.util;

import org.h2.tools.Server;

public class IniciarH2Console {
	public static void main(String[] args) {

		try {
			Server server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8085").start();

			System.out.println("H2 Console iniciado!");
			System.out.println(server.getURL());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}