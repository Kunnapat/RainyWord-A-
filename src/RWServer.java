import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class RWServer extends JFrame{
	   private JTextArea output;
	   private Player players[];
	   private ServerSocket server;
	   private int currentPlayer;
	   public RWServer()
	   {
	      super( "Rainy Word Server" );
	 
	      
	      players = new Player[ 2 ];
	      currentPlayer = 0;
	  
	      // set up ServerSocket
	      try {
	         server = new ServerSocket( 5000, 2 );
	      }
	      catch( IOException e ) {
	         e.printStackTrace();
	         System.exit( 1 );
	      }
	 
	      output = new JTextArea();
	      getContentPane().add( output, BorderLayout.CENTER );
	      output.setText( "Server awaiting connections\n" );
	 
	      setSize( 300, 300 );
	      show();
	   }
	   public void execute()
	   {
	      for ( int i = 0; i < players.length; i++ ) {
	         try {
	            players[ i ] =
	               new Player( server.accept(), this, i );
	            players[ i ].start();
	         }
	         catch( IOException e ) {
	            e.printStackTrace();
	            System.exit( 1 );
	         }
	      }
	 
	      // Player X is suspended until Player O connects.
	      // Resume player X now.          
	      synchronized ( players[ 0 ] ) {
	         players[ 0 ].threadSuspended = false;   
	         players[ 0 ].notify();
	      }
	   
	   }
	   public void display( String s )
	   {
	      output.append( s + "\n" );
	   }
	   public static void main( String args[] )
	   {
	      RWServer game = new RWServer();
	 
	      game.addWindowListener( new WindowAdapter() {
	        public void windowClosing( WindowEvent e )
	            {
	               System.exit( 0 );
	            }
	         }
	      );
	 
	      game.execute();
	   }
	class Player extends Thread {
		private Socket connection;
		   private ObjectInputStream ois;
		   private ObjectOutputStream oos;
		   private RWServer control;
		   private int number;
		   protected boolean threadSuspended = true;
		   public Player( Socket s, RWServer t, int num )
		   {
		 
		      connection = s;
		       
		      try {
		         ois = new ObjectInputStream(connection.getInputStream());
		         oos = new ObjectOutputStream(connection.getOutputStream() );
		      }
		      catch( IOException e ) {
		         e.printStackTrace();
		         System.exit( 1 );
		      }
		 
		      control = t;
		      number = num;
		   }
	}
}
