package org.mtforce.impatouch;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LedDictionary 
{
	private static List<LedDigit> digits = new ArrayList<LedDigit>();
	
	public static void LoadDictionary()
	{
		try
		{
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(LedDictionary.class.getResourceAsStream("test.txt")));
			while((line = reader.readLine()) != null)
			{
				String split[] = line.split(";");
				LedDigit digit = new LedDigit(split[0]);
                for (int i = 1; i < split.length; i++)
                {   
                    int x = Integer.parseInt(split[i].split(" ")[0]);
                    int y = Integer.parseInt(split[i].split(" ")[1]);

                    digit.addCoordinate(x, y);
                }
                digits.add(digit);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static LedDigit getDigit(String name)
	{
		for(LedDigit digit : digits)
		{
			if(digit.getName().equals(name))
				return digit;
		}
		return null;
	}
}
