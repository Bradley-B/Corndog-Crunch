package com.bradleyboxer.corndog.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerClientInputManager extends Thread{

	BufferedReader in = null;
	String input = null;
	Socket socket = null;
	
	public ServerClientInputManager(Socket socket) {
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO exception in ServerClientManager thread creating bufferedreader");
		}
	}
	
	public ServerClientManager findMaster() {
		for(ServerClientManager cm : Server.sa.getClients()) {
			if(socket.equals(cm.socket)) {
				return cm;
			}
		}
		return null;
	}
	
	public void processCommand(String command) {
		ServerClientManager cm = findMaster();
		
		if(command.compareTo("ready")==0) {
			cm.setReady();
		} else if(command.compareTo("unready")==0) {
			cm.setUnready();
		} else if(command.contains("SCORE_REPORT")) {
			String sscore = command.substring(12, 15);
			cm.setScore(Integer.valueOf(sscore.trim()));
		} else if(command.contains("NAME_REPORT")) {
			String name = command.substring(11, command.length());
			cm.setPlayerName(name.trim());
		}
	}
	
	public void run() {
		
		try {
			while(!socket.isClosed()) {
				
				input = in.readLine(); //thread should hang on this line until new line is found on stream
				
				System.out.println("Command recieved from " + socket.getInetAddress() + " : " + input);
	
				processCommand(input);
				
			}
		} catch(IOException e) {
			System.out.println("error in reading CIM... closing respective socket");
			ServerClientManager thisCm = findMaster();
			Server.sa.threads.remove(thisCm);
			thisCm.closeConnection();
		}
	}
	
}
