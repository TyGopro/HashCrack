/**
*	CLASS:		Server
*	AUTHOR:		Tyler Gauvreau
*	PURPOSE:	Server software to be adapted at a later date
*/

//REMARKS:		Ask a client to provide a thread/client name and other things at some point in the future.

import java.net.*;
import java.net.InetAddress;
import java.io.*;

public class ServerThread extends Thread
{
	private Socket clientSocket = null;
	private PrintWriter socketOutput = null;
	private BufferedReader socketInput = null;
	private static String inputLine;
	
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
				//returns the input we got to the client
				socketOutput.println(inputLine);
				
				//Remember to print it out on this end
				System.out.println ("[CLIENT->SERVER] Message received from client: \"" + inputLine + "\".");
				System.out.println ("[SERVER] Waiting for inbound traffic from the client...");
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