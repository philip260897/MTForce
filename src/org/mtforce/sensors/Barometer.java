package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;

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
	
	public static final byte kgsCMD_READ_PROM 		= (byte) 0xA0;	//8-BIT DATA FOLLOWED BY ACK 2 TIMES (16BIT)
	
	public static final byte kgsCMD_CONVERSION_PRESSURE		= 0x40; //Umwandlung starten. Befehl setzt sich aux 0x40 verodert mit einer resoltuion zusammen
	public static final byte kgsCMD_CONVERSION_TEMPERATURE	= 0x50; //Umwandlung starten. Befehl setzt sich aux 0x50 verodert mit einer resoltuion zusammen
	public static final byte kgsCMD_READ					= 0x00;	//8-BIT DATA FOLLOWED BY ACK 3 TIMES (24BIT)
	
	public static final byte kgsRESOLUTION_256	= 0x00;
	public static final byte kgsRESOLUTION_512	= 0x01;
	public static final byte kgsRESOLTUION_1024	= 0x02;
	public static final byte kgsRESOLTUION_2048	= 0x03;
	public static final byte kgsRESOLTUION_4096	= 0x04;
	
	public static final byte kgsPROM_COEFF_1 = 0x02;
	public static final byte kgsPROM_COEFF_2 = 0x04;
	public static final byte kgsPROM_COEFF_3 = 0x06;
	public static final byte kgsPROM_COEFF_4 = 0x08;
	public static final byte kgsPROM_COEFF_5 = 0x0A;
	public static final byte kgsPROM_COEFF_6 = 0x0C;
	public static final byte kgsPROM_CRC = 0x0E;
	
	
	private I2CManager i2c;									//Verweis auf I2CManager
	private byte gResolutionPressure	= kgsRESOLUTION_256;//Von User eingestellte Umwandlungsaufloesung des Drucks
	private byte gResolutionTemperature	= kgsRESOLUTION_256;//Von User eingestellte Umwandlungsaufloesung der Temperatur
	
	/**
	 * Initialisiert den Baustein
	 */
	@Override
	public void init() 
	{
		i2c = (I2CManager) Sensors.getI2C();
		if(i2c.write(ADDRESS, RESET))
		{
			setEnabled(true);
		}
	}
	
	/**
	 * Resettet den Baustein
	 */
	public void reset() 
	{
		i2c.write(ADDRESS, RESET);
	}
	
	/**
	 * Liest den ADC-Wert aus
	 */
	public int getAdcValue() 
	{
		i2c.write(ADDRESS, kgsCMD_READ);
		return Utils.toInt(Utils.reverseBytes(i2c.read(ADDRESS, 3)));
	}
	
	/**
	 * Umwandlungsaufloesung der Druckmessung setzten
	 * @param resolution	Umwandlungskonstante (zwischen 0 - 4, falls > 4, wird 0 verwendet)
	 */
	public void setResolutionPressure(byte resolution)
	{
		if((int)resolution <= 4)
			gResolutionPressure = resolution;
		else
			gResolutionPressure = kgsRESOLUTION_256;
	}
	
	/**
	 * Startet Druck-ADC
	 */
	public void startConversionPressure() 
	{
		i2c.write(ADDRESS, (byte)(kgsCMD_CONVERSION_PRESSURE | this.gResolutionPressure));
	}
	
	/**
	 * Umwandlungsaufloesung der Temperaturmessung setzten
	 * @param resolution	Umwandlungskonstante (zwischen 0 - 4, falls > 4, wird 0 verwendet)
	 */
	public void setResoltuionTemperature(byte resolution)
	{
		if((int)resolution <= 4)
			gResolutionTemperature = resolution;
		else
			gResolutionTemperature = kgsRESOLUTION_256;
	}
	
	/**
	 * Startet Temperatur-ADC
	 */
	public void startConversionTemperature()
	{
		i2c.write(ADDRESS, (byte)(kgsCMD_CONVERSION_TEMPERATURE | this.gResolutionTemperature));
	}
	
	public int getCoeffizient(byte coeffizient)
	{
		if(coeffizient != kgsPROM_COEFF_1 && coeffizient != kgsPROM_COEFF_2 && coeffizient != kgsPROM_COEFF_3 && coeffizient != kgsPROM_COEFF_4 && coeffizient != kgsPROM_COEFF_5 && coeffizient != kgsPROM_COEFF_6)
			return -1;
		i2c.write(ADDRESS, (byte)(kgsCMD_READ_PROM | coeffizient));
		return Utils.toInt(Utils.reverseBytes(i2c.read(ADDRESS, 2)));
	}
	
	public int getCRC()
	{
		i2c.write(ADDRESS, (byte)(kgsCMD_READ_PROM | kgsPROM_CRC));
		return Utils.toInt(Utils.reverseBytes(i2c.read(ADDRESS, 2)));
	}
	
	public int calculateTemperatureDifference(int adcValue, int coeff5)
	{
		int dT = adcValue - coeff5 * 256;
		return dT;
	}
	
	public int calculateTemperature(int temperatureDifference, int coeff6)
	{
		int temp = 2000 + (int)((temperatureDifference*(double)coeff6) / 8388608d);
		return temp;
	}
	
	public long calculatePressureOffset(int temperatureDifference, int coeff2, int coeff4)
	{
		return ((long)coeff2 * 131072l) + ((long)coeff4 * (long)temperatureDifference)/64l;
	}
	
	public long calculatePressureSensitivity(int temperatureDifference, int coeff1, int coeff3)
	{
		return (long)coeff1 * 65536l + ((long)coeff3 * (long)temperatureDifference)/128l;
	}
	
	public int calculatePressure(int adcValue, long pressureSensitivity, long pressureOffset)
	{
		return (int)((((adcValue * pressureSensitivity)/2097152l) - pressureOffset) / 32768l);
	}
}
