package org.mtforce.main;

import java.util.ArrayList;
import java.util.List;

import org.mtforce.interfaces.CommunicationManager;
import org.mtforce.interfaces.I2CManager;
import org.mtforce.interfaces.SPIManager;
import org.mtforce.sensors.ADC;
import org.mtforce.sensors.Barometer;
import org.mtforce.sensors.DOF9;
import org.mtforce.sensors.DistanceSensor;
import org.mtforce.sensors.HumiditySensor;
import org.mtforce.sensors.IOExpander;
import org.mtforce.sensors.LightSensor;

import org.mtforce.sensors.Sensor;
import org.mtforce.sensors.Ser7Seg;
import org.mtforce.sensors.Thermometer;

/**
 * Beschreibung: Die Sensors-Klasse enthaelt alle Instanzen der verschiedenen Sensoren.
 * 
 * Konstanten: NICHT Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: Alle Sensoren hinzufuegen, getter und setter, Modultest
 */
public final class Sensors 
{
	private static List<Sensor> sensorList = new ArrayList<Sensor>();
	
	private static ADC adc = new ADC();
	private static DistanceSensor distanceSensor = new DistanceSensor();
	private static LightSensor lightSensor = new LightSensor();
	//private static Ser7Seg ser7seg = new Ser7Seg();
	//private static IOExpander ipExp = new IOExpander();
	private static HumiditySensor humidity = new HumiditySensor();
	private static DOF9 dof9 = new DOF9();
	private static Barometer barometer = new Barometer();
	private static Thermometer thermometer = new Thermometer();
	
	
	private static CommunicationManager i2c = null;
	private static CommunicationManager spi = null;
	
	private Sensors()
	{
		
	}
	
	/**
	 * Initialisiert Alle Sensoren und die CommunicationManager (I2C, SPI)
	 * @throws Exception	Falls ein Fehler bei der Initialisierung vorkommt
	 */
	public static void initialize() throws Exception
	{
		sensorList.add(adc);
		sensorList.add(distanceSensor);
		sensorList.add(lightSensor);
		//sensorList.add(ser7seg);
		//sensorList.add(ipExp);
		sensorList.add(thermometer);
		sensorList.add(humidity);
		sensorList.add(dof9);
		sensorList.add(barometer);
		
		if(i2c == null)
			i2c = new I2CManager();
		if(spi == null)
			spi = new SPIManager();
		
		i2c.initialize();
		spi.initialize();
		
		for(Sensor sensor : sensorList)
			sensor.init();
	}

	
	//=====Getter und Setter=====//
	public static void setI2C(CommunicationManager manager) {
		i2c = manager;
	}
	
	public static void setSPI(CommunicationManager manager) {
		spi = manager;
	}
	
	public static CommunicationManager getI2C() {
		return i2c;
	}
	
	public static CommunicationManager getSPI() {
		return spi;
	}
	
	public static Sensor[] getSensors() {
		return sensorList.toArray(new Sensor[sensorList.size()]);
	}
	
	public static DistanceSensor getDistanceSensor() {
		return distanceSensor;
	}

	/*public static IOExpander getIOExpander() {
		return ipExp;
	}*/
	
	public static Thermometer getThermometer() {
		return thermometer;
	}
	
	/*public static Ser7Seg getSer7Seg() {
		return ser7seg;
	}*/
	
	public static ADC getAdc() {
		return adc;
	}

	public static HumiditySensor getHumidity() {
		return humidity;
	}

	public static DOF9 getDof9() {
		return dof9;
	}

	public static Barometer getBarometer() {
		return barometer;
	}
	
	
}
