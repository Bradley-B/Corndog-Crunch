package com.bradleyboxer.corndog;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
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
	public static JLabel timerLabel = new JLabel();
	public static double timerTime = 0;
	public static int score;
	public static Main game;
	public static boolean on;
	public static boolean firstRun = true;
	public static Scoreboard scoreboard = new Scoreboard();

	
	public Main() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Corndog Crunch - The Game");
		this.setSize(600, 625);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		
		this.add(panels[4], BorderLayout.CENTER);
		panels[4].setLayout(new GridLayout(3, 3));
		
		this.add(panels[3], BorderLayout.NORTH);
		panels[3].setLayout(new GridLayout(1, 3));
		panels[3].add(timerLabel);
		panels[3].add(startGame);
		panels[3].add(scoreboardBtn);
		
		scoreboardBtn.addActionListener(new Clicked());
		scoreboardBtn.setText("Scoreboard");
		timerLabel.setText("Time: 0");
		timerLabel.setHorizontalAlignment(JLabel.CENTER);
		
		startGame.setText("Start Game");
		startGame.addActionListener(new Clicked());
		
		for(int i=0;i<3;i++) {
			panels[4].add(panels[i]);
			panels[i].setLayout(new GridLayout(1, 3));
		}
		
		int addCount=0;
		for(int y=0;y<3;y++) {
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
	}

	public static void main(String[] args) throws InterruptedException {
		game = new Main();
		
		while(true) { //main loop
		
			if(on) {
				timerTime = timerTime + 0.01;
				String timeElapsed = String.format("%.2f", timerTime);
				timerLabel.setText("Time: " + timeElapsed);
			
				if(timeElapsed.equals("8.00")) { 
					//end.play();
					if(JOptionPane.showConfirmDialog(game, ("Your score is " + score), "Play again?", JOptionPane.YES_NO_OPTION) == 0) {
						stop(true);
						start(false);
					} else {
						stop(true);
					} 
				}
			}
			else {}
			Thread.sleep(10);
		}
		
	}
	
	/**
	 * starts the program with optionally showing a dialog window
	 */
	public static void start(boolean showDialogWindow) {
		if(showDialogWindow) {
			if(JOptionPane.showConfirmDialog(game, "Ready?", "Let's play Corndog Crunch!", JOptionPane.YES_NO_OPTION) == 0) {
				placeNewCreature();
				on = true;
			} else {
			  System.exit(0); //no 
			}
		}
		else {
			stop(false);
			placeNewCreature();
			on = true;
		}
	}
	
	/**
	 * stops the program with optionally removing the current creature
	 */
	public static void stop(boolean removeCurrentCreature) {
		on = false;
		timerTime = 0;
		timerLabel.setText("Time: 0");
		score = 0;
		if(removeCurrentCreature) {activeCreature.remove();}
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
				//end.play();
				stop(!firstRun);
				firstRun = false;
				start(true);
			}
			else if(e.getSource()==scoreboardBtn) {
				if(!on) {
					stop(false);
					scoreboard.setVisible(true);
				}
				else {error.stop(); error.play();}
			}
		}
	}

	private static ImageIcon cImg(int resourceNumber) {
		return (new ImageIcon(Main.class.getResource("/resources/c" + String.valueOf(resourceNumber) + ".png")));
	}
}