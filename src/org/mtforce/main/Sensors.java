package org.mtforce.main;

import java.util.ArrayList;
import java.util.List;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.interfaces.SPIManager;
import org.mtforce.sensors.DistanceSensor;
import org.mtforce.sensors.IOExpander;
import org.mtforce.sensors.LightSensor;

import org.mtforce.sensors.Sensor;
import org.mtforce.sensors.Ser7Seg;
import org.mtforce.sensors.Thermometer;

public final class Sensors 
{
	private static List<Sensor> sensorList = new ArrayList<Sensor>();
	
	private static DistanceSensor distanceSensor = new DistanceSensor();
	private static LightSensor lightSensor = new LightSensor();
	private static Ser7Seg ser7seg = new Ser7Seg();
	private static IOExpander ipExp = new IOExpander();
	private static Thermometer thermometer = new Thermometer();
	
	private Sensors()
	{
		
	}
	
	public static void initialize() throws Exception
	{
		sensorList.add(distanceSensor);
		sensorList.add(lightSensor);
		sensorList.add(ser7seg);
		sensorList.add(ipExp);
		sensorList.add(thermometer);
		
		SPIManager.initialize();
		I2CManager.initialize();
		
		for(Sensor sensor : sensorList)
			sensor.init();
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

	public static IOExpander getIOExpander() {
		return ipExp;
	}
	
	public static Thermometer getThermometer() {
		return thermometer;
	}
}
