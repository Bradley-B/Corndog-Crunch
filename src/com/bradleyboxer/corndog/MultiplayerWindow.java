package com.bradleyboxer.corndog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.bradleyboxer.corndog.server.Server;

public class MultiplayerWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	public static JPanel[] connectPanels = {new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel()};
	public static JButton connectBtn = new JButton();
	public static JTextField ipInput = new JTextField();
	public static JTextField portInput = new JTextField();	
	public static JLabel connectLbl = new JLabel();
	public static InetAddress address;
	public static Socket socket1=null;
	public static BufferedReader br=null;
	public static PrintWriter out=null;
	public static MultiplayerListener listener = null;
	public static boolean on = false;
	public static JTextArea console = new JTextArea();
	public static JScrollPane scroll = new JScrollPane(console);
	public static JButton readyBtn = new JButton();
	public static JButton unreadyBtn = new JButton();
	public static JLabel nameLbl = new JLabel();
	public static JTextField nameInput = new JTextField();
	public static JTextField chatbox = new JTextField();
	public static boolean connected = false;
	
	public MultiplayerWindow() {
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setTitle("Multiplayer");
		this.setSize(300, 460);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
		this.setLayout(new FlowLayout());
		this.setAutoRequestFocus(true);
		
		connectPanels[0].setPreferredSize(new Dimension(300, 20));
		connectPanels[1].setPreferredSize(new Dimension(300, 30));
		connectPanels[2].setPreferredSize(new Dimension(300, 40));

		ipInput.setPreferredSize(new Dimension(150, 25));
		portInput.setPreferredSize(new Dimension(60, 25));
		nameInput.setPreferredSize(new Dimension(90, 25));
		console.setPreferredSize(new Dimension(250, 200));
		chatbox.setPreferredSize(new Dimension(250, 30));
		
		chatbox.setToolTipText("I am a chatbox, type in me and hit enter to chat");
		nameInput.setToolTipText("Your Name");
		ipInput.setToolTipText("IP Address of Server");
		portInput.setToolTipText("Port of Server");
		
		nameLbl.setText("Name: ");
		ipInput.setText("");
		portInput.setText("4445");
		readyBtn.setText("Ready");
		unreadyBtn.setText("Not Ready");
		
		readyBtn.setEnabled(false);
		unreadyBtn.setEnabled(false);
		
		this.add(connectPanels[0]);
		this.add(connectPanels[1]);
		this.add(connectPanels[2]);	
		this.add(connectPanels[3]);
		this.add(connectPanels[4]);
		this.add(connectPanels[5]);
		
		connectPanels[0].add(connectLbl);
		connectPanels[1].add(ipInput);
		connectPanels[1].add(portInput);
		connectPanels[2].add(nameLbl);
		connectPanels[2].add(nameInput);
		connectPanels[2].add(connectBtn);
		connectPanels[3].add(readyBtn);
		connectPanels[3].add(unreadyBtn);
		connectPanels[4].add(scroll);
		connectPanels[5].add(chatbox);
		
		console.setLineWrap(true);
		
		console.setEditable(false);
		chatbox.setEditable(true);
		
		connectLbl.setText("Enter IP and Port of Server");
		connectBtn.setText("Connect");
		
		chatbox.addKeyListener(new Typed());
		readyBtn.addActionListener(new Clicked());
		unreadyBtn.addActionListener(new Clicked());
		connectBtn.addActionListener(new Clicked());
	}
	
	private static final Pattern IP_PATTERN = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	public static boolean validateIP(final String ip) {
	    return IP_PATTERN.matcher(ip).matches();
	}
	
	public static boolean validatePort(final String port) {
		try {
			int integerPort = Integer.decode(port);
			if(integerPort<65535 && integerPort>0) {
				return true;
			}
		} catch (NumberFormatException e) {}
		return false;
	}
	
	public static void connectToServer(String ip, String name, int port) throws IOException {
		try {
			address=InetAddress.getByName(ip);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		socket1=new Socket(address, port); 
		br= new BufferedReader(new InputStreamReader(System.in));

		out= new PrintWriter(socket1.getOutputStream());

		listener = new MultiplayerListener(socket1);
		listener.start();

		readyBtn.setEnabled(true);

		connected = true;
		sendMessageToServer("/nameReport "+name);
		System.out.println("Connected. Client address : "+address);

	}

	public static void sendMessageToServer(String message) {
		out.println(message);
		out.flush();
	}
	
	public static void setUnready() {
		readyBtn.setEnabled(true);
		unreadyBtn.setEnabled(false);
		sendMessageToServer("/unready");
	}
	
	/**
	 * turns the game on or off
	 * @param state the new state of the game
	 */
	public static void setMultiplayerGame(boolean state) {
		on = state;
	}
	
	private class Typed implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {

			if(connected && arg0.getKeyChar()=='\n' && chatbox.getText().length()>0) {
				String command = chatbox.getText();
				String basecommand = Misc.getCommand(command);
				String subcommand = Misc.getSubcommand(command);

				if(basecommand.contains("disconnect")) { //add else if statements for clientside commands here
					connected = false;
					sendMessageToServer(command);
					listener.closeConnection();
					connectBtn.setEnabled(true);
					readyBtn.setEnabled(false);
					unreadyBtn.setEnabled(false);
				} else {
					sendMessageToServer(command);
				}
				chatbox.setText("");
			}
		}
	}
	
	private class Clicked implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==connectBtn) {
				String uIp = ipInput.getText();
				String uPort = portInput.getText();
				String uName = nameInput.getText();
				
				if(validatePort(uPort) && validateIP(uIp) && uName.length()>0) {
					try {
						connectToServer(uIp, uName, Integer.decode(uPort));
						connectBtn.setEnabled(false);
					} catch (IOException ex) {
						//System.out.println("IO exception in client. This is probably not the server's fault.");
						System.out.println(ex.getMessage());
						console.setText("Connection failed.\nCheck the IP address and try again.\n"+ex.getMessage());
					}
				} else {
					console.setText("Preference values are incorrect.\nPlease correct any errors and try again.");
				} 
				
			} else if(e.getSource()==readyBtn) {
				readyBtn.setEnabled(false);
				unreadyBtn.setEnabled(true);
				sendMessageToServer("/ready");
			}else if(e.getSource()==unreadyBtn) {
				setUnready();
			}
		}
	}
}