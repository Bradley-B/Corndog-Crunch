package com.bradleyboxer.corndog.server;

import java.util.ArrayList;

import com.bradleyboxer.corndog.highscores.Score;

public class ServerController extends Thread {

	public boolean gameRunning = false;
	public int timeRemaining = 10;
	
	public void run() {
		while(!Thread.interrupted()) {
			if(!gameRunning) {
				boolean readyToStart = getAllReady();
				
				if(readyToStart) {
					runCountdown();
				}
			}
		}
	}
	
	public void runCountdown() {
		try {
			
			if(timeRemaining==0) {
				sendMessageToClients("start");
				sendMessageToClients("Started!");
				gameRunning = true;
				Thread.sleep(9000);
				gameRunning = false;
				timeRemaining = 10;
				
			} else {
				timeRemaining--;
				sendMessageToClients("Starting in "+String.valueOf(timeRemaining) + " seconds");
				Thread.sleep(500);
			}
		
		} catch (InterruptedException e) {}
		
	}
	
	public void collectScoresFromClients() {
		for(ServerClientManager cm : Server.sa.getClients()) {
			
		}
	}
	
	public boolean getAllReady() {
		ArrayList<ServerClientManager> clients = Server.sa.getClients();
	
		if(clients.size()==0) {
			return false;
		}
	
		for(ServerClientManager cm : clients) {
			if(!cm.getReadyState() || gameRunning) { //returns true if not ready or game running
				return false;
			}
		}
		
		try {Thread.sleep(500);} catch (InterruptedException e) {}
		return true;
	}
	
	public void sendMessageToClients(String message) {
		for(ServerClientManager cm : Server.sa.getClients()) {
			cm.out.println(message);
			cm.out.flush();
		}
	}
}