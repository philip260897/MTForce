package org.mtforce.sensors;

import java.util.ArrayList;
import java.util.List;

import org.mtforce.interfaces.CommunicationManager;
import org.mtforce.interfaces.I2CManager;
import org.mtforce.interfaces.SPIManager;

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
	
	private static boolean enabled = false;
	
	protected Sensors()
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
		
		enabled = true;
	}

	
	
	public static boolean isEnabled() {
		return enabled;
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
	
	public static LightSensor getLightSensor() {
		return lightSensor;
	}
}
