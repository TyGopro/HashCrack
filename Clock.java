/**
*	CLASS:		Clock
*	AUTHOR:		Tyler Gauvreau
*	PURPOSE:	Acts as the counter (or clock) to the rainbow table generator
*/

class Clock
{
	private final int ARRAY_LENGTH = 10;
	private char[] asciiTime = null;
	
	//PURPOSE:	Used to initialize the clock using a starting-value or "time"
	public Clock (String startingValue)
	{
		if (startingValue.length() > ARRAY_LENGTH)
		{
			System.out.println ("[ERROR] Clock starting value is larger then set maximum size. No clock initialized.");
		}
		else
		{
			System.out.println ("[SYSTEM] Setting clock to the following value: \"" + startingValue + "\".");
			
			asciiTime = new char[ARRAY_LENGTH];
			reset();
			
			for (int i = startingValue.length(); i > 0; i--)
			{
				asciiTime[ARRAY_LENGTH - i] = startingValue.charAt(startingValue.length() - i);
			}
		}
	}
	
	//PURPOSE:	Used to initialize a clean clock
	public Clock ()
	{
		asciiTime = new char[ARRAY_LENGTH];
		reset();
	}
	
	//PURPOSE:	Adds an arbitrary amount of time to the current clock. Mostly used for load balancing.
	public void increment (String incrementVal)
	{
		char[] incrementArr;
		
		if (incrementVal.length() > ARRAY_LENGTH)
		{
			System.out.println ("[ERROR] Increment value is larger then clock array. Increment aborted.");
		}
		else if (asciiTime == null)
		{
			System.out.println ("[ERROR] Clock was not initialized. Increment aborted.");
		}
		else
		{
			incrementArr = incrementVal.toCharArray();
			
			//Add both arrays together in the appropriate spots
			for (int i = 0; i < incrementArr.length; i++)
			{
				asciiTime[asciiTime.length - incrementArr.length + i] += incrementArr[i];
			}
			
			//Now adjust values to be within printable range
			checkLimits();
		}	
	}
	
	public void checkLimits ()
	{
		//Check printable clock limits
		for (int i = ARRAY_LENGTH-1; i >= 0; i--)
		{
			if (asciiTime[i] > 126)		//Last printable character "~"
			{
				asciiTime[i] = 32;		//First printable character " " (space)
				if (i != 0)				//If not at the beginning of the arrray, prevents out of index boundary problem
				{
					asciiTime[i-1]++;	//Increment next "time unit"
				}
			}
		}
	}
	
	//PURPOSE:	Returns a String containing the value of the next clock tick
	public String getNext ()
	{
		if (asciiTime != null)
		{
			//Increment clock by one
			asciiTime[ARRAY_LENGTH-1]++;
			
			checkLimits();
		}
		else
		{
			System.out.println ("[ERROR] Clock was not initialized. Will not get next clock value.");
		}
		
		//Convert array into String and return it
		return getClock();
	}
	
	//PURPOSE: 	Return the current value of the clock as a trimmed string
	public String getClock ()
	{
		String returnValue = "";
		
		if (asciiTime != null)
		{
			//So long as the value isn't our placeholder, add it to the string
			for (int i = 0; i < ARRAY_LENGTH; i++)
			{
				if (asciiTime[i] >= 32)
				{
					returnValue = returnValue + asciiTime[i];
				}
			}
		}
		else
		{
			System.out.println ("[ERROR] Clock was not initialized. A blank string will be returned.");
		}
		
		return returnValue;
	}
		
	//PURPOSE:	Prints the underlying array this clock uses, for debugging purposes
	public void printClock ()
	{
		if (asciiTime != null)
		{
			System.out.print ("[");
			for (int i = 0; i < ARRAY_LENGTH; i++)
			{
				System.out.print (asciiTime[i]);
			}
			System.out.print ("]");
		}
		else
		{
			System.out.println ("[ERROR] Clock was not initialized. No printing will be performed.");
		}
	}
	
	//PURPOSE: 	Reset the char clock to blank values
	public void reset ()
	{
		if (asciiTime != null)
		{
			for (int i = 0; i < ARRAY_LENGTH; i++)
			{
				asciiTime[i] = 31;	//First non-printable character before space
			}
		}
		else
		{
			System.out.println ("[ERROR] Clock was not initialized. No reset performed.");
		}
	}
}