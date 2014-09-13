/**
*	CLASS:		Server
*	AUTHOR:		Tyler Gauvreau
*	PURPOSE:	Server software to be adapted at a later date
*/

//ADDITIONAL REMARKS: 	Look up multicasting, if that's the kind of thing we need?
//						Make a counter for # of connected clients as well.

import java.net.*;
import java.io.*;

class Server
{
	private static final int PORT_NUMBER = 6789;
	private static ServerSocket serverSocket = null;
	private static boolean isServerOpen;
	
	public static void main(String[] args)
	{
		System.out.println ("[SYSTEM] Starting up server over port " + PORT_NUMBER);
		System.out.println ("[SYSTEM] Press ctrl+c to exit.");
		
		openServer();
		
		System.out.println ("[SYSTEM] Closing server. Terminated.");
	}
	
	//PURPOSE:	Handles the opening of the server and handling the inputs from clients
	private static void openServer ()
	{
		try
		{
			System.out.println ("[SERVER] Waiting for inbound traffic from a client...");
			
			serverSocket = new ServerSocket (PORT_NUMBER);
			
			isServerOpen = true;
			
			//Handle client input here; modify this part to suit our purposes
			while (isServerOpen) 	//Use isFound here in the future when finding hashes?
			{
				//Create a new ServerThread and start it. Waits for client connection.
				new ServerThread (serverSocket.accept()).start();
			}
			
			serverSocket.close();
		} 
		catch (IOException ioe)
		{
			System.out.println("[ERROR] IO error when opening the server's Socket over port \"" + PORT_NUMBER + "\".");
			System.exit(1);
		}
	}
}