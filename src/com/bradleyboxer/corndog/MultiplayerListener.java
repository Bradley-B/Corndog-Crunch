package com.bradleyboxer.corndog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MultiplayerListener extends Thread{

	public static String line=null;
	public static BufferedReader in=null;
	public static Socket socket = null;
	public static int textProgress = 0;
	
	public MultiplayerListener(Socket socket) {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {}
		
		MultiplayerListener.socket = socket;
	}
	
	public void run() {
		while(!socket.isClosed()) {
			try {
				line = in.readLine();
				if(MultiplayerWindow.connected) {
					processCommand(line);
				}
			} catch (IOException e) {}
		}
	}
	
	public void processCommand(String command) {
		String basecommand = Misc.getCommand(command);
		String subcommand = Misc.getSubcommand(command);
		
		if(basecommand.equals("start")) {
			Main.initialTime = System.currentTimeMillis();
			Main.placeNewCreature();
			MultiplayerWindow.setMultiplayerGame(true);
			MultiplayerWindow.setUnready();
		} else if(basecommand.contains("multiplayerScoreReport")) {
			MultiplayerWindow.console.setText("\n"+subcommand.trim());
			textProgress = 10;
		} else if(basecommand.contains("clear")) {
			MultiplayerWindow.console.setText("");
		} else {
			if(textProgress>=10) {
				MultiplayerWindow.console.setText(command);
				textProgress = 0;
			} else {
				MultiplayerWindow.console.setText(MultiplayerWindow.console.getText()+"\n"+command);
				textProgress+=(int) (command.length()/36)+1;
			}
		}
	}
	
	public void closeConnection() {
		try {
			in.close();
			socket.close();
			line = "";
			textProgress = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
