package com.bradleyboxer.corndog;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * The corndog for the Corndog Crunch game. The name is generalized to "Creature" in case of alternate use.
 * @author Bradley Boxer
 *
 */
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

	public void remove() {
		isAlive = false;
		setIcon(base);
		Main.activeCreature = null;
	}
	
	public void revive() {
		isAlive = true;
	}
	
}
