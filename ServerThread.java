/**
*	CLASS:		Server
*	AUTHOR:		Tyler Gauvreau
*	PURPOSE:	Server software to be adapted at a later date
*/

//REMARKS:		Ask a client to provide a thread/client name and other things at some point in the future.
//				Static clock goes in here, solves all the design problems. 
// 				What about synchronization?

import java.net.*;
import java.net.InetAddress;
import java.io.*;

public class ServerThread extends Thread
{
	private Socket clientSocket = null;
	private PrintWriter socketOutput = null;
	private BufferedReader socketInput = null;
	private static String inputLine;
	private static Clock clock = new Clock ();
	private static final String CHUNK = "!!";
	private static String startTime;
	private static String stopTime;
	
	public ServerThread (Socket clientSocket)
	{
		super ("ServerThread");				//Because a thread needs a name
		this.clientSocket = clientSocket;	//Pass in the socket made by the ServerSocket in the Server class
	}
    
	public void run () 
	{
        try
		{
			socketOutput = new PrintWriter(clientSocket.getOutputStream(), true);                   
			socketInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			System.out.print ("[SERVER] A new client has joined from the following address: ");
			System.out.println (clientSocket.getInetAddress().getHostAddress());
			
			//Handle client input here; modify this part to suit our purposes
			while ((inputLine = socketInput.readLine()) != null) 
			{
				System.out.println ("[CLIENT->SERVER] Message received from client: \"" + inputLine + "\".");
				
				/** Check inputLine here for what kind of commands to give */
				
				startTime = clock.getTime();
				clock.increment(CHUNK);
				stopTime = clock.getTime();
				
				//Sends the next chunk of work to any client that asks for it
				socketOutput.println ("[COMMAND] Work L1= " + startTime.length() + " L2= " +
					stopTime.length() + " " + startTime + stopTime);
				
				System.out.println ("[SERVER->CLIENT] Sent line to client: " + startTime.length() + " " + stopTime.length() +
					" " + startTime + stopTime);
				
				System.out.println ("[SERVER] Waiting for inbound traffic from the client...");
				//Wait for more communications...
			}
			
			clientSocket.close();
			socketOutput.close();
			socketInput.close();
			
			System.out.print ("[SERVER] A client with the following address has disconnected: ");
			System.out.println (clientSocket.getInetAddress().getHostAddress());
		} 
		catch (IOException ioe)
		{
			System.out.print ("[ERROR] IO error when opening a client's connection at ");
			System.out.println ("address \"" + clientSocket.getInetAddress().getHostAddress() + "\".");
		}
	}
}