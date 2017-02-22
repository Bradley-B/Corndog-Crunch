package com.bradleyboxer.corndog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class MultiplayerWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	public static JPanel[] connectPanels = {new JPanel(), new JPanel(), new JPanel()};
	public static JButton connectBtn = new JButton();
	public static JTextField ipInput = new JTextField();
	public static JTextField portInput = new JTextField();	
	public static JLabel connectLbl = new JLabel();
	public static InetAddress address;
	public static Socket socket1=null;
	public static String line=null;
	public static BufferedReader br=null;
	public static BufferedReader in=null;
	public static PrintWriter out=null;

	public MultiplayerWindow() {
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setTitle("Multiplayer");
		this.setSize(300, 300);
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
		
		ipInput.setToolTipText("IP Address of Server");
		portInput.setToolTipText("Port of Server");
		
		ipInput.setText("0.0.0.0");
		portInput.setText("4579");
		
		this.add(connectPanels[0]);
		this.add(connectPanels[1]);
		this.add(connectPanels[2]);	
		connectPanels[0].add(connectLbl);
		connectPanels[1].add(ipInput);
		connectPanels[1].add(portInput);
		connectPanels[2].add(connectBtn);
		
		connectLbl.setText("Enter IP and port of server");
		connectBtn.setText("Connect");
		
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
	
	public static void connectToServer(String ip, int port) throws IOException {
		address=InetAddress.getLocalHost();

		try {
			socket1=new Socket(address, port); 
			br= new BufferedReader(new InputStreamReader(System.in));
			in=new BufferedReader(new InputStreamReader(socket1.getInputStream()));
			out= new PrintWriter(socket1.getOutputStream());
			System.out.println("Connected. Client address : "+address);
		}
		catch (IOException e){
			//e.printStackTrace();
			System.out.println("IO exception in client. This is probably not the server's fault.");
			System.out.println(e.getMessage());
		}

	}

	private class Clicked implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==connectBtn) {
				String uIp = ipInput.getText();
				String uPort = portInput.getText();
				
				if(validatePort(uPort) && validateIP(uIp)) {
					try {
						connectToServer(uIp, Integer.decode(uPort));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} else {
					System.out.println("IP MUST BE IN 0.0.0.0 FORMAT, AND PORT MUST BE AN INTEGER BETWEEN 0-65535");
				}
				
			}
		}
	}
}