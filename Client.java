/**
*	CLASS:		Client
*	AUTHOR:		Tyler Gauvreau
*	PURPOSE:	Client software to be adapted at a later date
*/

//REMARKS:		Get this thing threaded if possible, when dealing with the hashes.

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.*;
import java.security.*;
import java.io.UnsupportedEncodingException;

class Client
{
	private static String hostAddr;
	private static int portNumber;
	private Socket toServer = null;	
	private PrintWriter socketOutput = null;
	private BufferedReader socketInput = null;
	private BufferedReader commandLine = null;
	private Clock clientClock;
	private static final String TARGET = "6dd075556effaa6e7f1e3e3ba9fdc5fa";
	
	
	
	public Client (String serverLocation)
	{
		Scanner scanner = new Scanner (serverLocation);
		scanner.useDelimiter(":");
		hostAddr = scanner.next();
		portNumber = scanner.nextInt();
		
		System.out.print ("[TESTING] Host location was set to \"" + hostAddr + "\" and ");
		System.out.println ("port number was set to \"" + portNumber + "\".");
		System.out.println ("[SYSTEM] Press ctrl+d to exit.");
	}
	
	//PURPOSE:	Opens a socket and sends a line of input to the server
	public void startWork ()
	{
		try
		{
			String inputLine;
			Scanner netScanner;
			String serverString;
			String startTime = "";
			String stopTime = "";
			String testHash;
			boolean doneSearching = false;
			String instType;
			int startLength;
			int stopLength;
	
			//Open socket to server
			toServer = new Socket (hostAddr, portNumber);
			
			//Set up output from socket
			socketOutput = new PrintWriter(toServer.getOutputStream(), true);
			
			//Set up input to socket
			socketInput = new BufferedReader(new InputStreamReader(toServer.getInputStream()));
			
			//Gets one line of input from the user's command line
			commandLine = new BufferedReader(new InputStreamReader(System.in));
			
			//Keep getting input lines until ctrl+d
			System.out.print ("[CLIENT] Enter message to send to server and get a chunk of work: "); 
			
			while ((inputLine = commandLine.readLine()) != null)
			{
				//Send the userInput line to the server
				socketOutput.println (inputLine);
				
				//Get response from server
				serverString = socketInput.readLine();
								
				System.out.println ("[SERVER->CLIENT] Message received from server: \"" + serverString + "\"");
				
				/** [WORK COMMAND] Under construction; phase 1 below... */
				netScanner = new Scanner (serverString);
				netScanner.next(); //Skip the [COMMAND] for now
				instType = netScanner.next(); //Read in the command type
				
				if ("Work".equals(instType))
				{
					netScanner.next(); 	//Skip over the "L1="
					startLength = netScanner.nextInt();
					
					netScanner.next();	//Skip over the "L2="
					stopLength = netScanner.nextInt();
					
					String temporary = netScanner.nextLine();
					
					startTime = temporary.substring (1, startLength+1);
					stopTime = temporary.substring (startLength+1);	//Can also use stopLength here if needed.
					
					System.out.println ("[TESTING] startLength was: \"" + startLength + 
						"\" and stopLength was: \"" + stopLength + "\"");
					System.out.println ("[TESTING] startTime was: \"" + startTime + 
						"\" and stopTime was: \"" + stopTime + "\"");
				}
				else
				{
					System.out.println ("[ERROR] Received an invalid work packet!");
				}
				
				clientClock = new Clock (startTime);
				
				while (!doneSearching)
				{
					testHash = getHash(clientClock.getTime());
			
// 					System.out.println ("[TESTING] Clock is: \"" + clientClock.getTime() + "\" and hash is: " + testHash);
			
					if (testHash.equals(TARGET))
					{
						doneSearching = true;
						System.out.println ("[SYSTEM] Hashed plaintext is: " + clientClock.getTime());
					}
					else if (clientClock.getTime().equals(stopTime))
					{
						doneSearching = true;
						System.out.println ("[SYSTEM] Didn't find hash in given chunk of work.");
					}
					else
					{
						clientClock.getNext();
					}
				}
				doneSearching = false;
				/** [WORK COMMAND] Under construction; phase 1 above ^ */ //NEXT UP: DIVIDE INTO EACH WORK TYPE METHODS!
				
				System.out.print ("[CLIENT] Send message to server: ");
				//Get another line of input at the top of this loop
			}
			
			//Close everything off when we're done with it
			toServer.close();
			socketOutput.close();
			socketInput.close();
			commandLine.close();
		}
		catch (UnknownHostException uhe)
		{
			System.out.println ("[ERROR] UnknownHostException for host \"" + hostAddr + "\"");
			System.exit(1);
		}
		catch (IOException ioe)
		{
			System.out.print ("[ERROR] IOException: Could not connect ");
			System.out.println ("to \"" + hostAddr + "\" on port \"" + portNumber + "\".");
			System.exit(1);
		}
	}
	
	// PURPOSE:	String goes in, hashed String comes out */
	public static String getHash (String hashThis)
	{
		String hashType = "MD5";
		String encodingType = "UTF-8";
		StringBuffer hexString = new StringBuffer ();
		MessageDigest digester = null;
		byte[] byteDigest = null;
		byte[] bytesOfInput = null;
		
		/** Set our digester type to MD5 */
		try
		{
			digester = MessageDigest.getInstance(hashType);
		}
		catch (NoSuchAlgorithmException nsae)
		{
			System.out.println ("[ERROR] Requested hash function \"" + hashType + "\" is unsupported.");
		}
		
		/** Convert input into an array of bytes using specified encoding format */
		try
		{
			bytesOfInput = hashThis.getBytes(encodingType);
		}
		catch (UnsupportedEncodingException uee)
		{
			System.out.println ("[ERROR] Requested encoding type \"" + encodingType + "\" is not supported.");
		}
		
		/** Digest the input byte array */
		if (digester != null)
		{
			byteDigest = digester.digest(bytesOfInput);
		}
		
		/** Turn outputs into something legible, not that other garble; this is the only part I do not fully understand! */
		for (int i = 0; i < byteDigest.length; i++)
		{
			int representation = byteDigest[i] & 0xFF;	//OHGOD, turns out the "& 0xFF" part is REALLY important! WHY?!
			
			if (representation < 0x10)
			{
				hexString.append('\u0030');	//Put that leading zero back on the byte if it's under 0x10.
			}// '\u0030' is the hex value for '0'.
			
			hexString.append(Integer.toHexString(representation));	//Finally, add the resulting byte in base 16 to the output.
		}
		
		return hexString.toString();	//Return as a string because other programmers don't like playing with StringBuffers.
	}
}