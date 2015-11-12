import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class HomeClient extends JFrame{
	static int windowWidth = 500;
	static int windowHeight = 80;
    static JButton startButton;
	JButton gameSetting;
    static ObjectInputStream ois;
    JPanel mainPanel;
    static LinkedList wordList = new LinkedList();
    static JLabel label;
    
    
    static int fallSpeed;
    static boolean caseSensitivity;
    
    
    
	public HomeClient(){
		super("Client Home");
		createMainPanel();
		this.add(mainPanel, BorderLayout.CENTER);
		
	}
	


	private void createMainPanel() {
		mainPanel = new JPanel();
		label = new JLabel("Please wait for server to set the game up");
		label.setFont(new Font("Menlo",Font.PLAIN,12));
		
		startButton = new JButton("Start");
		startButton.setEnabled(false);
		startButton.setFont(new Font("Menlo",Font.PLAIN,12));
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					GameClient.serverScore = 0;
					GameClient.clientScore = 0;
					GameClient game = GameClient.createAndShowGUI(wordList);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
			
		});
		mainPanel.add(label);
		mainPanel.add(startButton);
		
	}


	public static HomeClient createAndShowGUI() throws UnknownHostException, IOException{
		HomeClient frame = new HomeClient();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth,windowHeight); // set the size of GUI
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        return frame;
	}
	public static void main(String[] args) throws UnknownHostException, IOException{
		GameClient.setServerName("temp");
		GameClient.setClientName("temp");
		createAndShowGUI();
	}


	
}