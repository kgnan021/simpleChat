// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String host;
  int p;
  int login_id;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI,int login) 
    throws IOException 
  {
	  
	  super(host, port); //Call the superclass constructor
	  this.clientUI = clientUI;
	   if(login==-1) {
		  System.out.println("No login ID specified");
		  quit();
	  }else {
		  this.login_id=login;
		  String msg="#login <"+login_id+">";
		 openConnection();
		 sendToServer(msg);
	  }
	   
    
  }  
  
  

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	String msg=(String) message;
         if(message.equals("#quit")) {
        	 sendToServer("logoff <"+login_id+">");;
    		quit();
    	}else if(message.equals("#logoff")){
    		sendToServer("logoff <"+login_id+">"); 
    		closeConnection();
    	}else if(message.equals("#gethost")){
    		System.out.println(getHost());
    	}else if(message.equals("#getport")){
    		System.out.println(getPort());
    	}else if(msg.contains("#")){
    		String msg1 = message.substring(message.indexOf("#") + 1, message.indexOf(" "));
    		String h = message.substring(message.indexOf("<") + 1, message.indexOf(">"));
    	 if(msg1.equals("sethost")) {
        		if(!(isConnected())) {
        			setHost(h);
        			System.out.println("Host set to: "+h);
        		}else {
        			System.out.println("Vous êtes toujours connecté au serveur.Assuez vous de vous déconnecter et réessayez.");
        		}
        }else if(msg1.equals("login")){
    		if(!(isConnected())) {
    			openConnection();
    			sendToServer(message); 
    		}else {
    			System.out.println("Vous êtes toujours connecté au serveur.Assuez vous de vous déconnecter et réessayez.");
    		}
    	}else if(msg1.equals("setport")){
        	   int p= Integer.parseInt( h ); 
        		if(!(isConnected())) {
        			setPort(p);
        			System.out.println("Port set to: "+p);
        		}else {
        			System.out.println("Vous êtes toujours connecté au serveur.Assuez vous de vous déconnecter et réessayez.");
        		}
        	}
    	}else {
    		sendToServer(message); 
    	}

    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  public void connectionClosed() {
	    System.out.println("La connection est interrompue.");
	}
  
  
  public void connectionException(Exception exception) {
	  connectionClosed();
  }
}




//End of ChatClient class
