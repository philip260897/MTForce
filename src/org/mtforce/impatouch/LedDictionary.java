package org.mtforce.impatouch;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Beschreibung: Diese Klasse laedt Symboldefinitionen aus einer .txt datei. Die Symboldefinitionen werden zum darstellen von Buchstaben und Zahlen
 * 	auf den 14-Segmentanzeigen verwendet
 */
public class LedDictionary 
{
	private static List<LedDigit> digits = new ArrayList<LedDigit>();
	
	/**
	 * Lädt die Symboldefinitionen aus einer .txt Datei in eine Liste
	 */
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
	
	/**
	 * Gibt das Symbol zurück welches von dem String vorgegeben wird
	 * @param name	Buchstabe der als Symbol dargestellt werden soll
	 * @return		Gibt ein LedDigit Objekt zurück welches die Symboldefinition enthält
	 */
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
