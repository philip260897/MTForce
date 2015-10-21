package org.mtforce.main;

import java.util.ArrayList;
import java.util.List;

import org.mtforce.sensors.DistanceSensor;
import org.mtforce.sensors.IRTransmitter;
import org.mtforce.sensors.LightSensor;
import org.mtforce.sensors.Sensor;

public class Sensors 
{
	private static List<Sensor> sensorList = new ArrayList<Sensor>();
	
	private static DistanceSensor distanceSensor = new DistanceSensor();
	private static IRTransmitter irTransmitter = new IRTransmitter();
	private static LightSensor lightSensor = new LightSensor();
	
	public static void initialize()
	{
		sensorList.add(distanceSensor);
		sensorList.add(irTransmitter);
		sensorList.add(lightSensor);
		
		for(Sensor sensor : sensorList)
			sensor.update();
	}
	
	public static void updateAll()
	{
		for(Sensor sensor : sensorList)
			sensor.update();
	}

	public static Sensor[] getSensors() {
		return sensorList.toArray(new Sensor[sensorList.size()]);
	}
	
	public static DistanceSensor getDistanceSensor() {
		return distanceSensor;
	}

	public static IRTransmitter getIrTransmitter() {
		return irTransmitter;
	}
}
