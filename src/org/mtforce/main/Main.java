package org.mtforce.main;

import org.mtforce.sensors.Sensor;

import com.pi4j.wiringpi.I2C;

import org.mtforce.interfaces.CommunicationManager;
import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;
public class Main 
{


	public static void main(String[] args) 
	{
		try
		{
			Sensors.initialize();
			
			for(Sensor sensor : Sensors.getSensors())
				System.out.println("Registered: "+sensor.getClass().getSimpleName());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
