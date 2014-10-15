/**
*	CLASS:		Server
*	AUTHOR:		Tyler Gauvreau
*	PURPOSE:	Server software for HashCrack
*/

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
			
			while (isServerOpen)
			{
				//Create a new ServerThread and start it. Then waits for more client connections.
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