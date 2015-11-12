package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import utility.Configuration;

public class Configuration {
	private static volatile Configuration INSTANCE = null;
	private static String CONFIG_PATH = "config.txt";
	
	public static int numPeer;
	public static String serverIP;
	
	public static Configuration getInstance() {
		if (INSTANCE == null) {
			synchronized (Configuration.class) {
				if (INSTANCE == null) {
					INSTANCE = new Configuration();
				}
			}
		}
		return INSTANCE;
	}
	
	private Configuration()
	{
		init();
		setVariables();
	}
	
	private void init()
	{
	}
	
	private void setVariables()
	{
		String currLine = "";
		BufferedReader br;
		String[] strArr = null;
		String val = "";
		try {
			br = new BufferedReader(new FileReader(CONFIG_PATH));
			while ((currLine = br.readLine()) != null)
			{
				strArr = currLine.split(" ");
				if(strArr.length == 3)
				{
					val = strArr[2];
					switch (strArr[1])
					{
						case("NumberPeer"):
							numPeer = Integer.valueOf(val);
							break;
						case("Server_IP"):
							serverIP = val;
						default:
							break;
						
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
