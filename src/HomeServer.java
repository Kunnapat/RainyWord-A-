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
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.omg.CORBA.portable.OutputStream;


public class HomeServer extends JFrame{
	static int windowWidth = 500;
	static int windowHeight = 300;
    JButton startButton, gameSetting;
    JPanel topPanel, catPanel, bottomPanel;
    JComboBox<String> catBox ;
    JSlider speedSlider;
    JCheckBox caseCheckbox;
    
    static int fallingSpeed = 2;
    static boolean caseSensitivity = false;
    
    
    
    LinkedList wordList = new LinkedList();
    String[] color = {"red", "black", "white","grey","green","yellow","orange","purple","pink"};
    String[] car = {"Honda","Toyota", "Nissan","Mazda","KIA","Hyundai","MercedesBenz","Acura","Lexus","Infiniti","Suzuki","Mitsubishi","Citroen","Chevrolet","Bentley","BMW","Holden","Daewoo","RollsRoyce","Audi","Volswagen","Peugeot","Mini","McLaren","Ford",};
    String[] luxury = {"LouisVuitton","Gucci", "Rolex","Moschino","Hermes","Prada","Givenchy","Chanel","BVLGARI","GiorgioArmani","BOSS","BURBERRY","MARCJACOBS","ChristianDior","Versace","RalphLauren","Patek","Panerai","TomFord","Swarovski","Valentino","MichaelKors","Celine","Vertu","CalvinKlein"};
    String[] country = {"Bhutan","Ghana","Georgia","Finland","Cook Islands","Burkina Faso","Grenada","Fiji","Cambodia","Dominican Republic","Argentina","Cyprus","Japan","Cameroon","Guam","Cuba","Cruise Ship","Jamaica","Estonia","Kenya","Chad","Belgium","Angola","Germany","Algeria","India","Belarus","Indonesia","Faroe Islands","Bermuda","Guyana","Botswana","Egypt","Croatia","China","Denmark","Isle of Man","Cote D Ivoire","Aruba","Guinea","Chile","Czech Republic","Brazil","French Polynesia","Greece","Albania","British Virgin Islands","Costa Rica","Bulgaria","Guinea Bissau","Cayman Islands","Gambia","Bosnia &amp; Herzegovina","France","Haiti","Guernsey","Armenia","Falkland Islands","Guatemala","Kazakhstan","Antigua &amp; Barbuda","Iran","Congo","Hungary","Bolivia","Ethiopia","Bangladesh","Dominica","Honduras","Greenland","Benin","Jordan","Bahrain","Barbados","Ireland","Djibouti","Andorra","Italy","Azerbaijan","Austria","Israel","French West Indies","Hong Kong","Equatorial Guinea","Brunei","Bahamas","Jersey","Colombia","Gabon","Cape Verde","Iraq","Ecuador","Burundi","Kuwait","Anguilla","Australia","Gibraltar","Belize","Iceland","El Salvador",};
    String[] pp = {"public","static", "void","main","string","args","int","long","double","char","private","for","if","and","or","while","null","System","println","new","break","thread","runnable","action","parse"};
    String[] musictheory = {"Accelerando","Adagio", "Canon","Crescendo","Da Capo","Dolce","Forte","Fortissimo","Fugue","Major","Presto","Quarter","Rubato","Octave","Symphony","Treble","Vibrato","Sharp","Moderato","Grandioso","Decrescendo","Harmonic Major","Harmonic Minor","Mixolydian","Mezzo"};
	public HomeServer(){
		super("Server Home");
		createTopPanel();
		createCatPanel();
		createBottomPanel();
		this.add(topPanel, BorderLayout.NORTH);
		this.add(catPanel, BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);	
	}
	private void createTopPanel(){
		topPanel = new JPanel();
		startButton = new JButton("Start");
		startButton.setFont(new Font("Menlo",Font.PLAIN,12));
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					getWordList();
					Server.oos.writeObject(wordList);
					try{
						caseSensitivity = caseCheckbox.isSelected();		
					}catch(Exception e2){
						
					}
					if(caseSensitivity){
						Server.oos.writeObject("case sensitive");
						GameServer.caseSensitivity = true;
					}
					GameServer.fallSpeed = fallingSpeed;
					Server.oos.writeObject("Speed_"+fallingSpeed);
					GameServer.serverScore = 0;
					GameServer.clientScore = 0;
					GameServer game = GameServer.createAndShowGUI(wordList);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}

			private void getWordList() {
					String s = "Country";
				try{
					s = (String) catBox.getSelectedItem();
				}catch (Exception e){
					
				}
				try{
					fallingSpeed = speedSlider.getValue();
				}catch(Exception e){
					
				}
				LinkedListItr itr1 = wordList.zeroth();
				int temp = 5;
				if(s.equals("Color")){
					for(int i = 0; i < color.length; i++){
						wordList.insert(new Word(temp*-200,color[i],fallingSpeed), itr1);
						temp++;
					}
				}else if(s.equals("Car")){
					for(int i = 0; i < car.length; i++){
						wordList.insert(new Word(temp*-200,car[i],fallingSpeed), itr1);
						temp++;
					}
				}else if(s.equals("Luxury")){
					for(int i = 0; i < luxury.length; i++){
						wordList.insert(new Word(temp*-200,luxury[i],fallingSpeed), itr1);
						temp++;
					}
					
				}else if(s.equals("Country")){
					for(int i = 0; i < country.length; i++){
						wordList.insert(new Word(temp*-200,country[i],fallingSpeed), itr1);
						temp++;
					}
				}else if(s.equals("Programming")){
					for(int i = 0; i < pp.length; i++){
						wordList.insert(new Word(temp*-200,pp[i],fallingSpeed), itr1);
						temp++;
					}
				}else if(s.equals("Music")){
					for(int i = 0; i < pp.length; i++){
						wordList.insert(new Word(temp*-200,musictheory[i],fallingSpeed), itr1);
						temp++;
					}
				}
				
				
			}
			
		});
		topPanel.add(startButton);
	}
	

	private void createCatPanel() {
		catPanel = new JPanel();
		catPanel.setBackground(Color.BLACK);
		
	}
	private void createBottomPanel(){
		bottomPanel = new JPanel();
		gameSetting = new JButton("Setting");
		gameSetting.setFont(new Font("Menlo",Font.PLAIN,12));
		gameSetting.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				createAndShowSettingFrame();
				
			}

			private void createAndShowSettingFrame() {
				final JFrame settingFrame = new JFrame();
		        settingFrame.setSize(400,230); // set the size of GUI
		        settingFrame.getContentPane().setBackground(Color.BLACK);
		        settingFrame.setLocationRelativeTo(null);
		        settingFrame.setVisible(true);
		        settingFrame.setResizable(false);
		        
		        
		        JPanel mainPanel = new JPanel();
		        JPanel finPanel = new JPanel();
		        mainPanel.setBackground(Color.BLACK);
		        finPanel.setBackground(Color.BLACK);
		        
		        
		        JLabel instructionLabel = new JLabel("       Word category.");
				instructionLabel.setForeground(Color.GREEN);
				instructionLabel.setFont(new Font("Menlo",Font.PLAIN,12));
				catBox = new JComboBox<String>();
				catBox.setFont(new Font("Menlo",Font.PLAIN,12));
				catBox.setBackground(Color.GRAY);
				catBox.setForeground(Color.BLACK);
				catBox.addItem("Color");
				catBox.addItem("Car");
				catBox.addItem("Luxury");
				catBox.addItem("Country");
				catBox.addItem("Programming");
				catBox.addItem("Music");
				catBox.setForeground(Color.GREEN);
				catBox.setBackground(Color.GRAY);
				catBox.setPreferredSize(new Dimension(100,50));
				
				
				JLabel speedLabel = new JLabel("       Falling Speed");
				speedLabel.setForeground(Color.GREEN);
				speedLabel.setFont(new Font("Menlo",Font.PLAIN,12));
				speedSlider = new JSlider(JSlider.HORIZONTAL,2,4,2);
				speedSlider.setMajorTickSpacing(1);
				speedSlider.setMinorTickSpacing(1);
				speedSlider.setPreferredSize(new Dimension(100,50));
				speedSlider.setPaintTicks(true);
				speedSlider.setPaintLabels(true);
				speedSlider.setFont(new Font("Menlo",Font.PLAIN,12));
				speedSlider.setForeground(Color.GREEN);
				
				JLabel caseLabel = new JLabel("       Case Sensitivity");
				caseLabel.setForeground(Color.GREEN);
				caseLabel.setFont(new Font("Menlo",Font.PLAIN,12));
				caseCheckbox = new JCheckBox();
				
				
				
				JButton doneButton = new JButton("Finish");
				doneButton.setBackground(Color.GRAY);
				doneButton.setForeground(Color.GREEN);
				doneButton.setPreferredSize(new Dimension(80,30));
				doneButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						settingFrame.dispose();
						
					}
					
				});
				
				
				mainPanel.add(instructionLabel);
				mainPanel.add(catBox);
				mainPanel.add(speedLabel);
				mainPanel.add(speedSlider);
				mainPanel.add(caseLabel);
				mainPanel.add(caseCheckbox);
				finPanel.add(doneButton);
				settingFrame.add(mainPanel, BorderLayout.CENTER);
				settingFrame.add(finPanel, BorderLayout.SOUTH);
			}
			
		});
		bottomPanel.add(gameSetting, BorderLayout.SOUTH);
	}


	public static HomeServer createAndShowGUI() throws IOException{
		HomeServer frame = new HomeServer();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(windowWidth,windowHeight); // set the size of GUI
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        return frame;
	}
	public static void main(String[] args) throws IOException{
		GameServer.setServerName("temp");
		GameServer.setClientName("temp");
		createAndShowGUI();
	}


	
}