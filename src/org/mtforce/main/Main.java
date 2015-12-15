package org.mtforce.main;

import org.mtforce.sensors.Sensor;
import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;
public class Main 
{

	public static void main(String[] args) 
	{
		try
		{
			Sensors.initialize();
			Sensors.updateAll();
			
						
			
			for(Sensor sensor : Sensors.getSensors())
				System.out.println("Registered: "+sensor.getClass().getSimpleName());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
