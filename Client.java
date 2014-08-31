/**
*	CLASS:		Client
*	AUTHOR:		Tyler Gauvreau
*	PURPOSE:	Client software to be adapted at a later date
*/

//REMARKS:		Get this thing threaded if possible, when dealing with the hashes.

import java.net.*;
import java.util.*;
import java.io.*;

class Client
{
	private String hostLocation;
	private int portNumber;
	private Socket toServer = null;	
	PrintWriter socketOutput = null;
	BufferedReader socketInput = null;
	BufferedReader commandLine = null;
	String inputLine;
	
	public Client (String serverLocation)
	{
		Scanner scanner = new Scanner (serverLocation);
		scanner.useDelimiter(":");
		hostLocation = scanner.next();
		portNumber = scanner.nextInt();
		
		System.out.print ("[TESTING] Host location was set to \"" + hostLocation + "\" and ");
		System.out.println ("port number was set to \"" + portNumber + "\".");
		System.out.println ("[SYSTEM] Press ctrl+d to exit.");
	}
	
	//PURPOSE:	Opens a socket and sends a line of input to the server
	public void sendLines ()
	{
		try
		{
			//Open socket to server
			toServer = new Socket (hostLocation, portNumber);
			
			//Set up output from socket
			socketOutput = new PrintWriter(toServer.getOutputStream(), true);
			
			//Set up input to socket
			socketInput = new BufferedReader(new InputStreamReader(toServer.getInputStream()));
			
			//Gets one line of input from the user's command line
			commandLine = new BufferedReader(new InputStreamReader(System.in));
			
			//Keep getting input lines until ctrl+d
			System.out.print ("[CLIENT] Send message to server: ");
			while ((inputLine = commandLine.readLine()) != null)
			{
				//Send the userInput line to the server
				socketOutput.println (inputLine);
				
				//Wait for a response from the server (move this listener to it's own thread if used in a chat client)
				System.out.println ("[SERVER->CLIENT] Message received from server: \"" + socketInput.readLine() + "\"");
				
				/** Right here is where you want to process the clock times from the server */
				
				System.out.print ("[CLIENT] Send message to server: ");
			}
			
			//Close everything off when we're done with it
			toServer.close();
			socketOutput.close();
			socketInput.close();
			commandLine.close();
		}
		catch (UnknownHostException uhe)
		{
			System.out.println ("[ERROR] UnknownHostException for host \"" + hostLocation + "\"");
			System.exit(1);
		}
		catch (IOException ioe)
		{
			System.out.print ("[ERROR] IOException: Could not connect ");
			System.out.println ("to \"" + hostLocation + "\" on port \"" + portNumber + "\".");
			System.exit(1);
		}
	}
	
}