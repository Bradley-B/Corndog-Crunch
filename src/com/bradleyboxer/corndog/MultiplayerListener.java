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
				processCommand(line);
			} catch (IOException e) {}
		}
	}
	
	public void processCommand(String command) {
		if(command.compareTo("start")==0) {
			Main.initialTime = System.currentTimeMillis();
			Main.placeNewCreature();
			MultiplayerWindow.setMultiplayerGame(true);
			MultiplayerWindow.setUnready();
		} else if(command.contains("MP_SCORE_REPORT")) {
			MultiplayerWindow.console.setText("\n"+command.substring(15,command.length()).trim());
			textProgress = 10;
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
}
