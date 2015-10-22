package org.mtforce.main;

import org.mtforce.sensors.Sensor;

public class Main 
{

	public static void main(String[] args) 
	{
		try
		{
			Sensors.initialize();
			Sensors.updateAll();
			
			
			for(Sensor sensor : Sensors.getSensors())
				System.out.println("Registeredd: "+sensor.getClass().getSimpleName());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
