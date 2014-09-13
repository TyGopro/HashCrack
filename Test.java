class Test
{
	public static void main (String[] args)
	{
		System.out.println ("[TESTING] Beginning of test.");
		
		Client testClient = new Client ("127.0.0.1:6789");
		testClient.startWork();
		
		System.out.println ("\n[TESTING] End of test.");
	}
}