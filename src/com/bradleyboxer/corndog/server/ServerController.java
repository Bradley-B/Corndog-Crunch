package com.bradleyboxer.corndog.server;

import java.util.ArrayList;
import java.util.Collections;

import com.bradleyboxer.corndog.highscores.Score;
import com.bradleyboxer.corndog.highscores.ScoreComparator;

public class ServerController extends Thread {

	public ArrayList<Score> scores = new ArrayList<Score>();
	public boolean gameRunning = false;
	public int timeRemaining = 10;
	
	public void run() {
		while(!Thread.interrupted()) {
			if(!gameRunning) {
				boolean readyToStart = getAllReady();
				
				if(readyToStart) {
					runCountdown();
				}
			} else {
				try{Thread.sleep(250);} catch(InterruptedException e) {}
			}
		}
	}
	
	public void runGame() {
		sendMessageToClients("start");
		sendMessageToClients("Started!");
		gameRunning = true;
		try {Thread.sleep(9000);} catch(InterruptedException e) {};
		gameRunning = false;
		timeRemaining = 10;
		collectScoresFromClients();
		
		sendMessageToClients("clear");
		
		String finalScoreString = "";
		for(Score s : scores) {
			finalScoreString = finalScoreString+s.getName()+": "+s.getScore()+"\n";
		}
		
		System.out.println("--------------SCORES REPORT-------------");
		System.out.println("Scores counted! Results: \n"+finalScoreString);
		sendMessageToClients(finalScoreString);
		scores.clear();
	
	}
	
	public void runCountdown() {
		try {
			if(timeRemaining==0) {
				runGame();
			} else {
				timeRemaining--;
				sendMessageToClients("Starting in "+String.valueOf(timeRemaining) + " seconds");
				Thread.sleep(500);
			}
		
		} catch (InterruptedException e) {}
		
	}
	
	public void collectScoresFromClients() {
		ScoreComparator comparator = new ScoreComparator();

		for(ServerClientManager cm : Server.sa.getClients()) {
			scores.add(cm.getScore());
			Collections.sort(scores, comparator);
		}
	}

	
	public boolean getAllReady() { //check the state of all clients to see if they are ready
		ArrayList<ServerClientManager> clients = Server.sa.getClients();
	
		if(clients.size()==0) {
			return false;
		}
	
		for(ServerClientManager cm : clients) { //check clients. If one is not ready, return false  
			if(cm!=null && (!cm.getReadyState() || gameRunning)) { //returns true if not ready or game running
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