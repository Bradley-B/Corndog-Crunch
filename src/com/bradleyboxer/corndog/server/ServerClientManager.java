package com.bradleyboxer.corndog.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.bradleyboxer.corndog.highscores.Score;

class ServerClientManager extends Thread{  

	boolean ready = false;
	String threadName = null;
	PrintWriter out = null;
	Socket socket = null;
	ServerClientInputManager cim = null;
	String name = "default.name";
	int score = 0;
	
	public ServerClientManager(Socket socket){
		this.socket = socket;
	}

	public Score getScore() {
		return new Score(name, score);
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setPlayerName(String name) {
		this.name = name;
	}
	
	public boolean getReadyState() {
		return ready;
	}
	
	public void setUnready() {
		ready = false;
		Server.sc.timeRemaining = 10;
	}
	
	public void setReady() {
		ready = true;
	}
	
	public ServerClientInputManager getCim() {
		return cim;
	}
	
	public void sendMessage(String message) {
		out.println(message);
		System.out.println("Responded with : " + message);
		out.flush();
	}

	public void closeConnection() {
		try{
			System.out.println("Connection closing...");
			
			Server.sa.removeClient(this);
			
			if (cim.in!=null){
				cim.in.close(); 
				System.out.println("Socket input stream closed");
			}
			if(out!=null){
				out.close();
				System.out.println("Socket output closed");
			}	
			if (socket!=null){
				socket.close();
				System.out.println("Socket closed");
			}

		}
		catch(IOException ie){
			System.out.println("Socket close error!");
		}
	}
	
	public void run() {
		cim = new ServerClientInputManager(socket);
		cim.start();
		
		try{
			out = new PrintWriter(socket.getOutputStream());	
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("IO exception in ServerClientManager thread creating output stream");
		}
	}
	
}//end class