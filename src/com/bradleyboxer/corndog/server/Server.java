package com.bradleyboxer.corndog.server;

public class Server {

	public static ServerAcceptor sa = null;
	public static ServerController sc = null;
	
	public static void main(String[] args) {
		sa = new ServerAcceptor(Integer.valueOf(args[0]));
		sa.start();
		
		sc = new ServerController();
		sc.start();
	}

}
