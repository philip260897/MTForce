package org.mtforce.main;

import org.mtforce.sensors.Sensor;

public class Main 
{

	public static void main(String[] args) 
	{
		Sensors.initialize();
		Sensors.updateAll();
		
		for(Sensor sensor : Sensors.getSensors())
			System.out.println("Registered: "+sensor.getClass().getSimpleName());
	}
}
