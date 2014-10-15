/**
*	CLASS:		Server
*	AUTHOR:		Tyler Gauvreau
*	PURPOSE:	Server thread software for HashCrack
*/


import java.net.*;
import java.net.InetAddress;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread
{
	private static final String TARGET = "6dd075556effaa6e7f1e3e3ba9fdc5fa";
	private Socket clientSocket = null;
	private PrintWriter socketOutput = null;
	private BufferedReader socketInput = null;
	private static Clock clock = new Clock ();
	private static final String CHUNK = "!!";
	private static boolean hashFound = false;
	private static String answer = null;
	
	public ServerThread (Socket clientSocket)
	{
		super ("ServerThread");				//Because a thread needs a name
		this.clientSocket = clientSocket;	//Pass in the socket made by the ServerSocket in the Server class
	}
    
	public void run () 
	{
        try
		{
			String inputLine;
			socketOutput = new PrintWriter(clientSocket.getOutputStream(), true);                   
			socketInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			Scanner netScanner;
			String instType;
			
			System.out.print ("[SERVER] A new client has joined from the following address: ");
			System.out.println (clientSocket.getInetAddress().getHostAddress());
			
			//Handle client input here
			while (!hashFound)
			{
				inputLine = socketInput.readLine();
				System.out.println ("[CLIENT->SERVER] Message received from client: \"" + inputLine + "\".");
				
				//Determine command type here
				netScanner = new Scanner (inputLine);
				netScanner.next();	//Skip the [COMMAND] part
				instType = netScanner.next();	//Read in the instruction type
				
				if ("GiveWork".equals(instType))
				{
					giveWork(netScanner);
					System.out.println ("[SERVER] Waiting for inbound traffic from the client...");
					//Wait for more Client communications at the top of this loop
				}
				else if ("Found!".equals(instType))
				{
					//Scan the answer in
					netScanner.next();	//Skip over the "Answer is" part
					netScanner.next();
		
					//Display the answer
					answer = netScanner.next();
					System.out.print ("[SYSTEM] Answer has been found! ");
					System.out.println ("Hash value was \"" + TARGET + "\" and plaintext was \"" + answer + "\"");					
					
					hashFound = true;	//Exit the ServerThread from here
				}
				else
				{
					System.out.println ("[NETERROR] Received an invalid command via network!");
				}
			}
			
			//Wait for one last connection, tell the client it's time to leave
			if (null == answer)
			{
				System.out.println ("[SYNCERROR] ServerThread is terminating without having found an answer!");
			}
			else
			{
				giveAnswer();
			}
			
			clientSocket.close();
			socketOutput.close();
			socketInput.close();
			
			System.out.print ("[SERVER] A client with the following address has been disconnected: ");
			System.out.println (clientSocket.getInetAddress().getHostAddress());
		} 
		catch (IOException ioe)
		{
			System.out.print ("[ERROR] IO error when maintaining a client's connection at ");
			System.out.println ("address \"" + clientSocket.getInetAddress().getHostAddress() + "\".");
		}
	}
	
	//REMARKS:	Discard the Client's request and respond with answer
	private void giveAnswer ()
	{
		System.out.println ("[SYSTEM] Waiting to tell Client the answer...");
		try
		{
			socketInput.readLine();
			socketOutput.println ("[COMMAND] Found! Answer is: " + answer);
		}
		catch (IOException ioe)
		{
			System.out.print ("[ERROR] IO error when maintaining a client's connection at ");
			System.out.println ("address \"" + clientSocket.getInetAddress().getHostAddress() + "\".");
		}
	}
	
	private void giveWork (Scanner netScanner)
	{
		String startTime;
		String stopTime;
		
		startTime = clock.getTime();
		clock.increment(CHUNK);
		stopTime = clock.getTime();
		
		//Sends the next chunk of work to any client that asks for it
		socketOutput.println ("[COMMAND] Work L1= " + startTime.length() + " L2= " + stopTime.length() + 
			" " + startTime + stopTime);
				
		System.out.println ("[SERVER->CLIENT] Sent line to client: " + startTime.length() + " " + stopTime.length() +
			" " + startTime + stopTime);
	}
}