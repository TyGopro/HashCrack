/**
*	CLASS:		HashCrack
*	AUTHOR:		Tyler Gauvreau
*	PURPOSE:	Generates rainbow tables
*/

import java.security.*;
import java.io.UnsupportedEncodingException;

public class HashCrack
{
	private static final String targetHash = "6dd075556effaa6e7f1e3e3ba9fdc5fa";
	
	public static void main (String[] args)
	{
		System.out.println ("[SYSTEM] Beginning MD5 search.");
		
		process();
		
		System.out.println ("[SYSTEM] Plaintext found, results are above. Terminating.");
	}
	
	//PURPOSE:	Handles the main clock loop
	//REMARKS:	"6dd075556effaa6e7f1e3e3ba9fdc5fa" == "!!!"
	private static void process ()
	{
		String clockTime;
		String testHash;
		boolean found = false;
		
		Clock clock = new Clock ("!  ");	//Create new clock.
		clockTime = clock.getTime();
		while (!found)
		{
			testHash = getHash(clockTime);
			
			System.out.println ("[TESTING] Clock is: \"" + clockTime + "\" and hash is: " + testHash);
			
			if (testHash.equals(targetHash))
			{
				found = true;
				System.out.println ("[SYSTEM] Hashed plaintext is: " + clockTime);
			}
			else
			{
				clockTime = clock.getNext();
			}
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