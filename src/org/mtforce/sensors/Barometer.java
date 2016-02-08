package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Sensors;

/**
 * Beschreibung: Barometer - Misst den Luftdruck
 * 
 * Konstanten: Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: Modultest
 *	getPressure()	-	gibt den Luftdruck in bar zurueck
 */
public class Barometer extends Sensor {

	public static final byte ADDRESS 		= 0x76;			//LAST BIT = !CSB (CSB = HARDWARE PIN)
	public static final byte RESET 			= 0x1E; 		//RESETS PROM
	
	public static final byte PROM_READ 		= (byte) 0xA6;	//8-BIT DATA FOLLOWED BY ACK 2 TIMES (16BIT)
	
	public static final byte ADC_CONVERSION = 0x48; 		//CONVERSION OF ANALOG IN
	public static final byte ADC_READ 		= 0x00;			//8-BIT DATA FOLLOWED BY ACK 3 TIMES (24BIT)
	
	
	private I2CManager i2c;									//Verweis auf I2CManager
	
	/**
	 * Initialisiert den Baustein
	 */
	@Override
	public void init() 
	{
		//TODO: Richtige Initialisierung!!
		i2c = (I2CManager) Sensors.getI2C();
		i2c.write(ADDRESS, RESET);
	}
	
	/**
	 * Resettet den Baustein
	 */
	public void reset() 
	{
		i2c.write(ADDRESS, RESET);
	}
	
	/**
	 * Liest das PROM aus
	 * @return	gibt den PROM-Wert in byte zurueck
	 */
	public byte[] readProm() 
	{
		i2c.write(ADDRESS, PROM_READ);
		return i2c.read(ADDRESS, 2);
	}
	
	/**
	 * Startet Messwertkonvertierung
	 */
	public void conversion() 
	{
		i2c.write(ADDRESS, ADC_CONVERSION);
	}
	
	/**
	 * Liest den ADC-Wert aus
	 */
	public void readADC() 
	{
		//TODO: Plausibilitaet ueberpruefen (Kein rueckkgabewert?!)
		i2c.write(ADDRESS, ADC_READ);
	}
}
