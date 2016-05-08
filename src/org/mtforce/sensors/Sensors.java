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
	private static List<Sensor> sensorList = new ArrayList<Sensor>();		//Enthält referenz auf jeden Sensor
	
	private static ADC adc = new ADC();										//ADC
	private static DistanceSensor distanceSensor = new DistanceSensor();	//Distanz-Sensor
	private static LightSensor lightSensor = new LightSensor();				//Helligkeits-Sensor
	private static HumiditySensor humidity = new HumiditySensor();			//Luftfeuchtigkeits-Sensor
	private static DOF9 dof9 = new DOF9();									//Beschleunigungs-, Gyroskope-, Hall-Sensor
	private static Barometer barometer = new Barometer();					//Luftdruck-Sensor
	private static Thermometer thermometer = new Thermometer();				//Temperatur-Sensor
	
	
	private static CommunicationManager i2c = null;							//I2CManager
	private static CommunicationManager spi = null;							//SPIManager
	
	private static boolean enabled = false;									
	
	protected Sensors()
	{
		
	}
	
	/**
	 * Initialisiert alle Sensoren und die CommunicationManager (I2C, SPI)
	 * @throws Exception	Falls ein Fehler bei der Initialisierung vorkommt
	 */
	public static void initialize() throws Exception
	{
		sensorList.add(adc);
		sensorList.add(distanceSensor);
		sensorList.add(lightSensor);
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

	/**
	 * Gibt an, ob Sensors Aktiv ist oder nicht. Sensors wird aktiv, sobald die initialize()-Methode aufgerufen wurde
	 * @return	Aktiv oder nicht
	 */
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
	
	public static Thermometer getThermometer() {
		return thermometer;
	}
	
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
