import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class GameServer extends JFrame{
	static int windowWidth = 1000;
	static int windowHeight = 700;
	JPanel gamePanel;
	static JPanel optionPanel;
	static JPanel infoPanel;
	static JLabel serverNameLabel;
	static JLabel clientNameLabel;
	static JLabel serverScoreLabel;
	static JLabel clientScoreLabel;
	JLabel timerLabel;
	static boolean caseSensitivity = false;
	JFrame popUpFrame;
	static LinkedList wordList = new LinkedList();
	LinkedList welcomeList = new LinkedList();
    String[] color = {"red", "black", "white","grey","green","yellow","orange","purple","pink"};
    String inputWord;
	static String serverName;
	static String clientName;
    static JTextField inputField;
    static JButton startButton;
    static boolean gameStarted = false;
    static Thread t1;
    static int serverScore = 0;
    static int clientScore = 0;
    boolean clientIsReady = false;
    static int fallSpeed;
    static ActionListener al; 
    static int timeLeft;
    static javax.swing.Timer t;
    final int gameDuration = 30000;
    static Thread t2;
    
	public GameServer(){
		super("=================Rainy Word V3.0=================");
		createGamePanel();
		createOptionPanel();
		createInfoPanel();
		this.add(infoPanel, BorderLayout.NORTH);
		this.add(gamePanel,BorderLayout.CENTER);
		this.add(optionPanel,BorderLayout.SOUTH);
		t2 = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					String s = "";
					if(caseSensitivity){
						s = inputField.getText();
					}else{
						s = inputField.getText().toLowerCase();
					}
					
					if(!s.equals("")){
						LinkedListItr itr = wordList.first();
						while(!itr.isPastEnd()){
							Word w = itr.current.element;
							String temp = "";
							if(caseSensitivity){
								temp = w.word;
							}else{
								temp = w.word.toLowerCase();
							}
							if(temp.startsWith(s)){
								itr.current.element.changeColor(Color.RED);
							}
							itr.advance();
						}
					}
					
				}
				
			}
	    	
	    });
	}

	private void createInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setSize(new Dimension(1000,300));
		infoPanel.setBackground(Color.GRAY);
		infoPanel.add(serverNameLabel);
		serverScoreLabel = new JLabel("       Score: " + serverScore+"           ");
		infoPanel.add(serverScoreLabel);
		infoPanel.add(clientNameLabel);
		clientScoreLabel = new JLabel("       Score: " + clientScore+"           ");
		infoPanel.add(clientScoreLabel);
		
	}
	private void createGamePanel() {
		gamePanel = new GamePanel();
		gamePanel.setSize(new Dimension(1000,300));
		gamePanel.setBackground(Color.BLACK);
//		LinkedListItr itr1 = wordList.zeroth();
		LinkedListItr itr2 = welcomeList.zeroth();
//		int temp = 5;
//		for(int i = 0; i < color.length; i++){
//			wordList.insert(new Word(temp*-200,color[i]), itr1);
//			temp++;
//		}
		welcomeList.insert(new Word(170,-180,"-------"+"WHILE PLAYING, TYPE \"attack\" to attack your opponent."+"---------",fallSpeed), itr2);
		welcomeList.insert(new Word(170,-150,"-------------------"+clientName+" has started the game"+"-------------------",fallSpeed), itr2);
		welcomeList.insert(new Word(170,170,"▒█▀▀█ ░█▀▀█ ▀█▀ ▒█▄░▒█ ▒█░░▒█ 　 ▒█░░▒█ ▒█▀▀▀█ ▒█▀▀█ ▒█▀▀▄",fallSpeed), itr2);
		welcomeList.insert(new Word(170,190,"▒█▄▄▀ ▒█▄▄█ ▒█░ ▒█▒█▒█ ▒█▄▄▄█ 　 ▒█▒█▒█ ▒█░░▒█ ▒█▄▄▀ ▒█░▒█",fallSpeed), itr2);
		welcomeList.insert(new Word(170,210,"▒█░▒█ ▒█░▒█ ▄█▄ ▒█░░▀█ ░░▒█░░ 　 ▒█▄▀▄█ ▒█▄▄▄█ ▒█░▒█ ▒█▄▄▀",fallSpeed), itr2);
		welcomeList.insert(new Word(170,320,"---------------------------------------------------------",fallSpeed), itr2);
		
		repaint();
	}
	
	private void createOptionPanel() {
		optionPanel = new JPanel();
		optionPanel.setSize(new Dimension(1000,200));
		optionPanel.setBackground(Color.GRAY);
		startButton = new JButton("READY");
		
		
		t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				while(gameStarted){
					repaint();
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					if(timeLeft == 0){
//						
//						createPopUpFrame();
//						gameStarted = false;
//						repaint();
//						popUpFrame.setVisible(true);
//					}
				}
				
			}
			
		});
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				startButton.setEnabled(false);
				inputField.setText("Waiting for " + clientName + " to be ready.");
				try {
					Server.oos.writeObject("server ready");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
		});
		optionPanel.add(startButton);
		inputField = new JTextField();
		inputField.setPreferredSize(new Dimension(500,20));
		inputField.setBackground(Color.white);
		inputField.setForeground(Color.black);
		inputField.setFont(new Font("Menlo",Font.PLAIN,12));
		inputField.setText("ARE YOU READY ?");
		inputField.setEnabled(false);
		inputField.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String inputWord = "";
				if(caseSensitivity){
					inputWord = inputField.getText();
				}else{
					inputWord = inputField.getText().toLowerCase();
				}
				if(inputWord.equals("3474375000526")){
					inputWord = "eraser";
				} 
				if (inputWord.equals("4548718727810")){
					inputWord = "blue pen";
				}
				if (inputWord.equals("4548718960989")){
					inputWord = "red pen";
				}
				if (inputWord.equals("4902505409967")){
					inputWord = "red magic pen";
				}
				if (inputWord.equals("4902505409950")){
					inputWord = "black magic pen";
				}
				if (inputWord.equals("4902505409974")){
					inputWord = "blue magic pen";
				}
				if (inputWord.equals("4902505409981")){
					inputWord = "green magic pen";
				}
				if (inputWord.equals("4902505410031")){
					inputWord = "orange magic pen";
				}
				if (inputWord.equals("4891595098499")){
					inputWord = "purple folder";
				}
				if (inputWord.equals("15-05-12")){
					inputWord = "box";
				}
				inputField.setText("");
				System.out.println(inputWord);
				LinkedListItr itr1 = wordList.first();
				while(!itr1.isPastEnd()){
					String temp = "";
					if(caseSensitivity){
						temp = itr1.current.element.word;
					}else{
						temp = itr1.current.element.word.toLowerCase();
					}
					//toppu edit
					if(inputWord.equals("attack")){
						try {
							Server.oos.writeObject("attack");
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					//toppu edit
					if(inputWord.equals(temp)){
						playSound("src/correct.wav");
						System.out.println("correct");
						wordList.remove(itr1);
						try {
							Server.oos.writeObject(itr1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							addServerScore();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					}
					itr1.advance();
					if(itr1.isPastEnd()){
						playSound("src/wrong.wav");
					}
				}
				
			}
			
		});
		optionPanel.add(inputField);
		
		createTimerLabel();
		optionPanel.add(timerLabel);
	
		
	}
	private void createTimerLabel() {
	    final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("mm : ss");
	    timerLabel = new JLabel(sdf.format(new Date(gameDuration)),JLabel.CENTER);
	    int x = 0;
	    al = new ActionListener(){	
	      int xTime = gameDuration - 1000;
	      public void actionPerformed(ActionEvent ae){
	        timerLabel.setText(sdf.format(new Date(xTime)));
	        if(xTime == 0){
	        	t.stop();
	        	createPopUpFrame();
				gameStarted = false;
				repaint();
				popUpFrame.setVisible(true);
	        }
	        xTime -= 1000;}
	    };
	    
	}
	public static void startTimer(){
		t = new javax.swing.Timer(1000, al);
		t.start();
	}

	public static void addClientScore(){
		clientScore = clientScore + 1;
		clientScoreLabel.setText("       Score: " + clientScore+"           ");
	}
	private void addServerScore() throws IOException{
		serverScore = serverScore + 1;
		serverScoreLabel.setText("       Score: " + serverScore+"           ");
		Server.oos.writeObject("addServerScore");
	}

	public static void playSound(String s) {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(s));
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}

	private void createPopUpFrame() {
		popUpFrame = new JFrame();
		popUpFrame.setSize(600, 150);
		popUpFrame.setResizable(false);
		popUpFrame.setLocationRelativeTo(null);
		popUpFrame.setLayout(new GridLayout(3, 1));
		String s = "";
		if(serverScore > clientScore){
			s = "         Congratulation ! The winner is " + serverName;
		}else if (serverScore < clientScore){
			s = "         Awww... The winner is " + clientName;
		}else{
			s = "         It's a tie";
		}
		JLabel winLabel = new JLabel(s);
		JLabel scoreLabel = new JLabel("               Score:  You " + serverScore + " - " + clientScore + " " + clientName+"               ");
		popUpFrame.add(winLabel);
		popUpFrame.add(scoreLabel);
		JButton closeButton = new JButton("OK");
		closeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
			
		});
		JButton resetButton = new JButton("Play Again");
		resetButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				popUpFrame.dispose();
				try {
					dispose();
					HomeServer home = HomeServer.createAndShowGUI();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		closeButton.setSize(new Dimension(80,40));
		popUpFrame.add(closeButton);
		popUpFrame.add(resetButton);
		popUpFrame.setVisible(false);
		
	}
	
	
	
	public static GameServer createAndShowGUI(LinkedList ll) throws IOException{
		GameServer frame = new GameServer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth,windowHeight); // set the size of GUI
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        wordList = ll;
		
        return frame;
	}


	class GamePanel extends JPanel{
		boolean firstTime = true;
		
		public void paint(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
			if(firstTime){
				g2.setColor(Color.green);
				g2.setFont(new Font("Menlo",Font.PLAIN,20)); 
				g2.drawString("▒█▀▀█ ░█▀▀█ ▀█▀ ▒█▄░▒█ ▒█░░▒█ 　 ▒█░░▒█ ▒█▀▀▀█ ▒█▀▀█ ▒█▀▀▄", 170, 170);
				g2.drawString("▒█▄▄▀ ▒█▄▄█ ▒█░ ▒█▒█▒█ ▒█▄▄▄█ 　 ▒█▒█▒█ ▒█░░▒█ ▒█▄▄▀ ▒█░▒█ ", 170, 190);
				g2.drawString("▒█░▒█ ▒█░▒█ ▄█▄ ▒█░░▀█ ░░▒█░░ 　 ▒█▄▀▄█ ▒█▄▄▄█ ▒█░▒█ ▒█▄▄▀", 170, 210);
				g2.drawString("---------------------------------------------------------", 170, 320);
				
				firstTime = false;
			}else{
				g2.setColor(Color.BLACK);
				g2.drawRect(0, 0, windowWidth, windowHeight);
		        LinkedListItr itr1 = wordList.first();
		        LinkedListItr itr2 = welcomeList.first();
		        while(!itr2.isPastEnd()){
		        	itr2.current.element.paint(g2);
		        	itr2.advance();
		        }
		        while(!itr1.isPastEnd()){
		        	itr1.current.element.paint(g2);
		        	itr1.current.element.changeColor(Color.GREEN);
		        	itr1.advance();
		        }
		        update();
			}
				
		}
		
		
		


		public void update(){
			LinkedListItr itr1 = wordList.first();
			LinkedListItr itr2 = welcomeList.first();
	        while(!itr1.isPastEnd()){
	        	itr1.current.element.update();
	        	if(itr1.current.element.getYLocation() > windowHeight){
	        		wordList.remove(itr1);
	        		playSound("src/wrong.wav");
	        	}
	        	itr1.advance();
	        }
	        while(!itr2.isPastEnd()){
	        	itr2.current.element.update();
	        	if(itr2.current.element.getYLocation() > windowHeight){
	        		wordList.remove(itr2);
	        	}
	        	itr2.advance();
	        }
		}
	}

	
	public static void setServerName(String s){
		if(s == null){
			s = "";
		}
		serverName = s;
		serverNameLabel = new JLabel("Server Name: " + serverName+"           ");	
		serverNameLabel.setForeground(Color.WHITE);
	}
	
	public static void setClientName(String s){
		if(s == null){
			s = "";
		}
		clientName = s;
		clientNameLabel = new JLabel("Client Name: " + clientName+"           ");
		clientNameLabel.setForeground(Color.WHITE);
	}
	
	

}