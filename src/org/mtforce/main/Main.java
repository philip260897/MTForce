package org.mtforce.main;

public class Main 
{

	public static void main(String[] args) 
	{
		Sensors.initialize();
		Sensors.updateAll();
		
		System.out.println(Sensors.getDistanceSensor().getDistance());
	}
}
