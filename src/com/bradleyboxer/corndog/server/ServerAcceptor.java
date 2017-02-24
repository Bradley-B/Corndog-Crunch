package com.bradleyboxer.corndog.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerAcceptor extends Thread{

	public ArrayList<ServerClientManager> threads = new ArrayList<ServerClientManager>();
	int port;
	
	public ServerAcceptor(int port) {
		this.port = port;
	}
	
	public ArrayList<ServerClientManager> getClients() {
		return threads;
	}
	
	public void run() {
		Socket socket = null;
		ServerSocket serverSocket = null;
		System.out.println("Server Listening......");

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while(!serverSocket.isClosed()){
			try{
				socket = serverSocket.accept();
				System.out.println("Connection established with " + socket.getRemoteSocketAddress() + " . Opening in new thread.");
				ServerClientManager connectionToClient = new ServerClientManager(socket);
				threads.add(connectionToClient);
				connectionToClient.start();
			} catch(Exception e){
				e.printStackTrace();
				System.out.println("Connection Error");
			}
		}
	}
}