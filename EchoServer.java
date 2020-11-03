// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  
  public void handleMessageFromServerUI(String msg) {
	  String msg1=(String) msg;
	  if(msg1.contains("#")){
		  if(msg.equals("#quit")) {
			System.exit(0);
		}else if(msg.equals("#stop")){
			stopListening();
		}else if(msg.equals("#close")){
			try {
				close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//client disconnected
		}else if(msg.equals("#start")){
			if(!(isListening())) {
				try {
					listen();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				System.out.println(" Assuez vous de vous fermer et réessayez.");
			}
		
		}else if(msg.equals("#getport")){
			System.out.println(getPort());
		}else {
		  String ms = msg1.substring(msg1.indexOf("#") + 1, msg1.indexOf(" "));
		  if(ms.equals("setport")){
			  String h = msg1.substring(msg1.indexOf("<") + 1, msg1.indexOf(">"));
			  int p= Integer.parseInt(h); 
				if(!(isListening())) {
					setPort(p);
				}else {
					System.out.println(" Assuez vous de vous fermer et réessayez.");
				}
			} 
		}
	}
	  
	  sendToAllClients("SERVER MSG>"+msg);

  }
  
  
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  String msg1=(String) msg;
	  if(msg1.contains("#")) {
		  String ms = msg1.substring(msg1.indexOf("#") + 1, msg1.indexOf(" "));
		  if(ms.equals("login")) { 
			  String h = msg1.substring(msg1.indexOf("<") + 1, msg1.indexOf(">"));
			  int login= Integer.parseInt(h); 
			  if(client.getInfo("login_id")!=null){
					 System.out.println(login+" Client déjà connecté"); 
					 try {
						close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }else { client.setInfo("login_id", login);}
		  }else if(ms.equals("logoff")) {
			  clientDisconnected(client);
		  }
	  }
    this.sendToAllClients(msg);
    System.out.println(client.getInfo("login_id")+" Message received: " + msg + " from " + client);
  }
  

    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  public void clientConnected(ConnectionToClient client) {
	    System.out.println
	      ("A new client is connected");
  }
  
  public void clientDisconnected(ConnectionToClient client) {
	  System.out.println
      ("Login_id<"+client.getInfo("login_id")+"> is disconnected");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
