import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class Client {
	private HomeClient homeFrame;
	JFrame popUpFrame;
	public static int[] values = new int[6];
	private String clientName, serverName;
	static ObjectOutputStream oos;
	static ObjectInputStream ois;
	static Object response = null;
	static String responseText = null;
	public Client() throws NumberFormatException, ScriptException, UnknownHostException, IOException {

		// Layout GUI
		clientName = JOptionPane.showInputDialog("Please enter you name?");
		homeFrame = HomeClient.createAndShowGUI();
	}
	
	public void connectToServer() throws UnknownHostException, IOException, ClassNotFoundException{
		// Get the server address from a dialog box.
		String serverAddress = JOptionPane.showInputDialog(homeFrame,
			"Welcome "+clientName+"\nEnter IP Address of the Server:",
			"Welcome to the Rainy Word",
			JOptionPane.QUESTION_MESSAGE);

			// Make connection and initialize streams
			Socket socket = new Socket(serverAddress, 10096);

			java.io.OutputStream os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			InputStream is = socket.getInputStream();
			ois = new ObjectInputStream(is);
		
			
			oos.writeObject(clientName);
			serverName = ois.readObject().toString();
			
			GameClient.setServerName(serverName);
			GameClient.setClientName(clientName);
			while(true){
				response = ois.readObject();
				responseText = response.toString();
				System.out.println("client receive "+ responseText);
				try{
					LinkedList list = (LinkedList) response;
					HomeClient.wordList = list;
					HomeClient.startButton.setEnabled(true);
					HomeClient.label.setText("Server has set the game up");
				}catch (Exception e){
				}
				try{
					LinkedListItr itr = (LinkedListItr) response;
					String s = itr.current.element.word;
					GameClient.wordList.remove(GameClient.wordList.find(s));
				}catch (Exception e){
				}
				//
				if(response.equals("attack")){

					GameClient.inputField.setEditable(false);
					GameClient.playSound("src/cartoon001.wav");
					new Thread(new Runnable(){
					   public void run(){
					       try{
					          Thread.sleep(300);
					          GameClient.infoPanel.setBackground(Color.red);
					      	GameClient.optionPanel.setBackground(Color.red);
					      	GameClient.inputField.setText("==7== ATTACKED BY: "+GameClient.serverName+" "+"==7==");
					        Thread.sleep(300);
					          GameClient.infoPanel.setBackground(Color.orange);
					      	GameClient.optionPanel.setBackground(Color.orange);
					      	GameClient.inputField.setText("==6== ATTACKED BY: "+GameClient.serverName+" "+"==6==");
					        Thread.sleep(300);
					          GameClient.infoPanel.setBackground(Color.yellow);
					      	GameClient.optionPanel.setBackground(Color.yellow);
					      	GameClient.inputField.setText("==5== ATTACKED BY: "+GameClient.serverName+" "+"==5==");
					      	 Thread.sleep(300);
					          GameClient.infoPanel.setBackground(Color.green);
					      	GameClient.optionPanel.setBackground(Color.green);
					      	GameClient.inputField.setText("==4== ATTACKED BY: "+GameClient.serverName+" "+"==4==");
					      	 Thread.sleep(300);
					          GameClient.infoPanel.setBackground(Color.blue);
					      	GameClient.optionPanel.setBackground(Color.blue);
					      	GameClient.inputField.setText("==3== ATTACKED BY: "+GameClient.serverName+" "+"==3==");
					    	 Thread.sleep(300);
					          GameClient.infoPanel.setBackground(Color.magenta);
					      	GameClient.optionPanel.setBackground(Color.magenta);
					      	GameClient.inputField.setText("==2== ATTACKED BY: "+GameClient.serverName+" "+"==2==");
					      	 Thread.sleep(300);
					          GameClient.infoPanel.setBackground(Color.pink);
					      	GameClient.optionPanel.setBackground(Color.pink);
					      	GameClient.inputField.setText("==1== ATTACKED BY: "+GameClient.serverName+" "+"==1==");
					       }catch(InterruptedException ex){
					          ex.printStackTrace();
					       }
					       GameClient.infoPanel.setBackground(Color.gray);
					       GameClient.inputField.setText("");
					       GameClient.optionPanel.setBackground(Color.gray);
					       GameClient.inputField.setEditable(true);
					   }
					}).start();
					
					
				}
			
				//
				if(response.equals("server ready")){
					GameClient.startButton.setEnabled(true);
					GameClient.inputField.setText(serverName+" is ready. Are you ready?");
				}else if(response.equals("addServerScore")){
					GameClient.addServerScore();
				}else if (response.equals("case sensitive")){
					GameClient.caseSensitivity = true;
				}else if (responseText.startsWith("Speed_")){
					int temp = Integer.parseInt(responseText.substring(6));
					GameClient.fallSpeed = temp;
				}
				
			}

	}
	
	public static void main(String[] args) throws Exception {
		Client client = new Client();
		client.connectToServer();

	}

}
