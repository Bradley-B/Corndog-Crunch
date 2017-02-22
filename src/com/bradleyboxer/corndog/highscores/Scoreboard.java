package com.bradleyboxer.corndog.highscores;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.bradleyboxer.corndog.Main;

import java.util.*;
import java.awt.GridLayout;
import java.io.*;

/**
 * Displays and updates a scoreboard read from a file. Uses example code from http://forum.codecall.net/topic/50071-making-a-simple-high-score-system/.
 * @author Bradley Boxer
 *
 */
public class Scoreboard extends JFrame{
	
	private static JLabel[] scoreData = {new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel()};
	private static JLabel[] scoreLabels = {new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel()};
	private static final long serialVersionUID = 1L;
    private ArrayList<Score> scores = new ArrayList<Score>(); // An arraylist of the type "score" we will use to work with the scores inside the class
    private static final String HIGHSCORE_FILE = "scores.dat";  // The name of the file where the highscores will be saved
    ObjectOutputStream outputStream = null;  //Initialising an in and outputStream for working with the file
    ObjectInputStream inputStream = null;
    int lowestBestScore = 0;
    
	public Scoreboard() {
		this.setLayout(new GridLayout(10, 2));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(300, 300);
		this.setVisible(false);
		this.setAlwaysOnTop(true);
		this.setTitle("Scoreboard");
		this.setLocationRelativeTo(null);
		
		populate2();
		Main.lowestBestScore = scores.get(9).getScore();
	}
	
	public int repopulate() {
		for(int i=0;i<10;i++) {
			scores = getScores(); //filling the scores-arraylist
			scoreLabels[i].setText(String.valueOf(i+1) + ".     " + scores.get(i).getName());
			scoreData[i].setText(String.valueOf(scores.get(i).getScore()));
			lowestBestScore = scores.get(9).getScore();
		}
		return lowestBestScore;
	}
	
	public void populate2() {
		try {
			populate();
		}
		catch (IndexOutOfBoundsException e) {
			for(int i=0;i<10;i++) {
				addScore("N/A", 0);	
			}
			populate();
		}
	}
	
	public void populate() throws IndexOutOfBoundsException {
		for(int i=0;i<10;i++) {
			scores = getScores(); //filling the scores-arraylist
			scoreLabels[i].setText(String.valueOf(i+1) + ".        " + scores.get(i).getName());
			scoreData[i].setText(String.valueOf(scores.get(i).getScore()));
			scoreLabels[i].setHorizontalAlignment(JLabel.CENTER);
			scoreData[i].setHorizontalAlignment(JLabel.CENTER);
			this.add(scoreLabels[i]);
			this.add(scoreData[i]);
		}
	}
	
	public ArrayList<Score> getScores() {
        loadScoreFile();
        sort();
        return scores;
    }
	
	private void sort() {
        ScoreComparator comparator = new ScoreComparator();
        Collections.sort(scores, comparator);
	}
	
	public void addScore(String name, int score) {
        loadScoreFile();
        scores.add(new Score(name, score));
        updateScoreFile();
	}
	
	@SuppressWarnings("unchecked")
	public void loadScoreFile() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
            scores = (ArrayList<Score>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            //System.out.println("FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("CNF Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("IO Error: " + e.getMessage());
            }
        }
	}
	
	public void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
	}
	
	public String getHighscoreString() {
        String highscoreString = "";
        int max = 10;

        ArrayList<Score> scores;
        scores = getScores();

        int i = 0;
        int x = scores.size();
        if (x > max) {
            x = max;
        }
        while (i < x) {
            highscoreString += (i + 1) + ".\t" + scores.get(i).getName() + "\t\t" + scores.get(i).getScore() + "\n";
            i++;
        }
        return highscoreString;
	}
	
}
