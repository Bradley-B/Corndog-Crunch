package com.bradleyboxer.corndog;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.bradleyboxer.corndog.highscores.Scoreboard;

/**
 * Main class of the Corndog Crunch game.
 * @author Bradley Boxer
 *
 */
public class Main extends JFrame { 
	private static final long serialVersionUID = 1L;
    public static AudioClip end = Applet.newAudioClip((URL) Creature.class.getResource("/resources/sound7.wav")); 
    public static AudioClip error = Applet.newAudioClip((URL) Main.class.getResource("/resources/error.wav"));
	public static ImageIcon[] cI = {cImg(0), cImg(1), cImg(2), cImg(3), cImg(4), cImg(5), cImg(6), cImg(7), cImg(8)};
	public static Creature[] creatures = {new Creature(), new Creature(), new Creature(), new Creature(), new Creature(), new Creature(), new Creature(), new Creature(), new Creature()};
	public static JPanel[] panels = {new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel()};
	//panel 0-1-2 contain game buttons. Panel 3 is a label panel. Panel 4 contains panels 0-1-2. 
	public static Creature activeCreature;
	public static Random rand = new Random();
	public static JButton startGame = new JButton();
	public static JButton scoreboardBtn = new JButton();
	public static JButton multiplayerBtn = new JButton();
	public static JLabel timerLabel = new JLabel();
	public static double timerTime = 0;
	public static int score;
	public static Main game;
	public static boolean on;
	public static boolean firstRun;
	public static int lowestBestScore = 0;
	public static Scoreboard scoreboard = new Scoreboard();
	public static MultiplayerWindow multiplayerWindow = new MultiplayerWindow();
	public static String[] names = new String[] {"Bobby Teenager", "Dean Kamen", "Owen Busler", "Dank Memer", "Harambe", "Lauren Dahl", "Sarah Nasson"};
	
	public Main() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Corndog Crunch - The Game");
		this.setSize(600, 625);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		
		firstRun = true;
		
		int addCount=0;
		for(int y=0;y<3;y++) {				//fill creatures into creature slots
			for(int x=0;x<3;x++) {
				creatures[addCount].setOpaque(false);
				creatures[addCount].setContentAreaFilled(false);
				creatures[addCount].setBorderPainted(true);
				//creatures[addCount].setIcon(cI[addCount]);
				panels[y].add(creatures[addCount]);
				creatures[addCount].addActionListener(new Clicked());
				creatures[addCount].setIcon(Creature.base);
				addCount++;
			}
		}
		
		panels[4].add(panels[0]); //add buttons to panels and panels to panels
		panels[4].add(panels[1]);
		panels[4].add(panels[2]);	
		panels[3].add(timerLabel);
		panels[3].add(startGame);
		panels[3].add(scoreboardBtn);
		panels[3].add(multiplayerBtn);
		this.add(panels[4], BorderLayout.CENTER);
		this.add(panels[3], BorderLayout.NORTH);
		
		panels[0].setLayout(new GridLayout(1,3));
		panels[1].setLayout(new GridLayout(1,3));
		panels[2].setLayout(new GridLayout(1,3));
		panels[3].setLayout(new GridLayout(1, 3));
		panels[4].setLayout(new GridLayout(3, 3));
		
		scoreboardBtn.addActionListener(new Clicked());
		startGame.addActionListener(new Clicked());
		multiplayerBtn.addActionListener(new Clicked());
		
		startGame.setText("Start Game");
		scoreboardBtn.setText("Scoreboard");
		multiplayerBtn.setText("Multiplayer");
		timerLabel.setText("Time: 0");
		
		timerLabel.setHorizontalAlignment(JLabel.CENTER);
	}

	public static void main(String[] args) throws InterruptedException {
		game = new Main();
		
		while(true) { //main loop
			if(on) {
				singlePlayer();
			} else if(MultiplayerWindow.on) {
				multiplayer();
			}
			Thread.sleep(10);
		}
		
	}
	
	public static void multiplayer() {
		if(tick()) {
			end.play();
			stop();
		}
	}
	
	public static void singlePlayer() {
		if(tick()) { //game ended if true
			end.play();
			if(score>lowestBestScore) {
				String playerName = (String) JOptionPane.showInputDialog(game, "Enter your name for input into the scoreboard!", "Your score is " + String.valueOf(score), JOptionPane.PLAIN_MESSAGE, null, null, "Computer");
				if(playerName!=null) {scoreboard.addScore(playerName, score);}
				lowestBestScore = scoreboard.repopulate();
			}
			playAgain();
		}
	}
	
	public static boolean tick() {
		timerTime = timerTime + 0.01;
		String timeElapsed = String.format("%.2f", timerTime);
		timerLabel.setText("Time: " + timeElapsed);
		
		return timeElapsed.equals("8.00");
	}
	
	public static void playAgain() {
		boolean playAgain = (JOptionPane.showConfirmDialog(game, ("Your score is " + score), "Play again?", JOptionPane.YES_NO_OPTION) == 0);
		stop();
		
		if(playAgain) {
			start();
		}
	}
	
	/**
	 * starts the program with optionally showing a dialog window
	 */
	public static boolean start() {
		
		stop();

		boolean ready = (JOptionPane.showConfirmDialog(game, "Ready?", "Let's Play Corndog Crunch!", JOptionPane.YES_NO_OPTION) == 0);
			
		if(!ready) {
			return false;
		} 
		
		placeNewCreature();
		on = true;
		return true;
	}
	
	/**
	 * stops the program 
	 */
	public static void stop() {
		on = false;
		timerTime = 0;
		timerLabel.setText("Time: 0");
		
		if(!MultiplayerWindow.socket1.isClosed()) {
			MultiplayerWindow.setMultiplayerGame(false);
			MultiplayerWindow.sendMessageToServer("SCORE_REPORT "+score+"  ");
		}
		
		score = 0;
		
		if(activeCreature!=null) {
			activeCreature.remove();
		}
		
	}
	
	/**
	 * places a new creature
	 */
	public static void placeNewCreature() {
		activeCreature = creatures[rand.nextInt(9)];
		activeCreature.setIcon(cI[rand.nextInt(9)]);
		activeCreature.revive();
	}
	
	private class Clicked implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==activeCreature) {
				activeCreature.kill();
				placeNewCreature();
				score++;
			}
			else if(e.getSource()==startGame) {
				if(!on) {
					firstRun = false;
					start();
				} else {
					error.stop(); error.play();
				}
			}
			else if(e.getSource()==scoreboardBtn) {
				if(!on) {
					stop();
					scoreboard.setVisible(true);
				} else {
					error.stop(); error.play();
				}
			} 
			else if(e.getSource()==multiplayerBtn) {
				if(!on) {
					multiplayerWindow.setVisible(true);
				} else {
					error.stop(); error.play();
				}
			}
		}
	}
	
	private static ImageIcon cImg(int resourceNumber) {
		return (new ImageIcon(Main.class.getResource("/resources/c" + String.valueOf(resourceNumber) + ".png")));
	}
}