package org.mtforce.main;

import java.util.ArrayList;
import java.util.List;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.interfaces.SPIManager;
import org.mtforce.sensors.DistanceSensor;
import org.mtforce.sensors.IOExpander;
import org.mtforce.sensors.IRTransmitter;
import org.mtforce.sensors.LightSensor;
import org.mtforce.sensors.MCP23017Manager;
import org.mtforce.sensors.Sensor;
import org.mtforce.sensors.Ser7Seg;

public final class Sensors 
{
	private static List<Sensor> sensorList = new ArrayList<Sensor>();
	
	private static DistanceSensor distanceSensor = new DistanceSensor();
	private static IRTransmitter irTransmitter = new IRTransmitter();
	private static LightSensor lightSensor = new LightSensor();
	private static MCP23017Manager MCP23017Sensor = new MCP23017Manager();
	private static Ser7Seg ser7seg = new Ser7Seg();
	private static IOExpander ipExp = new IOExpander();
	
	private Sensors()
	{
		
	}
	
	public static void initialize() throws Exception
	{
		sensorList.add(distanceSensor);
		sensorList.add(irTransmitter);
		sensorList.add(lightSensor);
		sensorList.add(MCP23017Sensor);
		sensorList.add(ser7seg);
		sensorList.add(ipExp);
		
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

	public static IRTransmitter getIrTransmitter() {
		return irTransmitter;
	}
}
