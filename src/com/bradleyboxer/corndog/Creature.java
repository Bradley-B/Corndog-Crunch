package com.bradleyboxer.corndog;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class Creature extends JButton {
	private static final long serialVersionUID = 1L;
	public static AudioClip bite = Applet.newAudioClip((URL) Creature.class.getResource("/resources/bite.wav")); 
	boolean isAlive = true;
	public static ImageIcon base = (new ImageIcon(Main.class.getResource("/resources/default.png")));
	
	public boolean getAlive() {
		return isAlive;
	}
	
	public void kill() {
		isAlive = false;
		setIcon(base);
		bite.stop();
		bite.play();
	}

	public void revive() {
		isAlive = true;
	}
	
}
