package com.bradleyboxer.corndog;

import javax.swing.JFrame;

/**
 * Displays and updaets a scoreboard read from a file.
 * @author Bradley Boxer
 *
 */
public class Scoreboard extends JFrame{

	private static final long serialVersionUID = 1L;

	public Scoreboard() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, 600);
		this.setVisible(false);
		this.setAlwaysOnTop(true);
		this.setTitle("Corndog Crunch - Scoreboard");
		this.setLocationRelativeTo(null);
		Main.on = false;
		populateScores();
	}
	
	public void populateScores() {
		
	}
	
}
