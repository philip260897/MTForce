package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Logger;
import org.mtforce.main.Utils;
import org.mtforce.main.Logger.Status;

/**
 * Beschreibung: Feuchtigkeitssensor mit integriertem Temperatursensor
 * 
 * Konstanten: Komplett
 * Funktionen: Komplett
 * 
 * TODO: Modultest
 */
public class HumiditySensor extends Sensor 
{
	public final static byte kgsADDRESS 				= (byte) 0x40; 		//DEVICE ADDRESS
	public final static byte kgsMEASURE_HUM_HMM 		= (byte) 0xE5;		//Measure Relative Humidity, Hold Master Mode
	public final static byte kgsMEASURE_HUM_NO_HMM		= (byte) 0xF5;		//Measure Relative Humidity, No Hold Master Mode
	public final static byte kgsMEASURE_TEMP_HMM 		= (byte) 0xE3;		//Measure Temperature, Hold Master Mode
	public final static byte kgsMEASURE_TEMP_NO_HMM 	= (byte) 0xF3;		//Measure Temperature, No Hold Master Mode
	public final static byte kgsREAD_TEMP_PREVIOUS		= (byte) 0xE0;		//Read Temperature Value from Previous RH Measurement
	public final static byte kgsRESET 					= (byte) 0xFE;		//Reset
	public final static byte kgsWRITE_RHT_REG1			= (byte) 0xE6;		//Write RH/T User Register 1
	public final static byte kgsREAD_RHT_REG1 			= (byte) 0xE7;		//Read RH/T User Register 1
	public final static byte kgsWRITE_HEATER_CTRL_REG 	= 0x51;				//Write Heater Control Register
	public final static byte kgsREAD_HEATER_CTRL_REG 	= 0x11;				//Read Heater Control Register
	public final static int  kgsREAD_ID_1ST_BYTE 		= 0xFA0F;			//Read Electronic ID 1st Byte
	public final static int  kgsREAD_ID_2ND_BYTE 		= 0xFCC9;			//Read Electronic ID 2nd Byte
	public final static int  kgsREAD_FIRMWARE_REV		= 0x84B8;			//Read Firmware Revision
	
	public final static byte kgsCONF_MEASURE_RES_12_14	= 0x00;				//Messaufloesung RH: 12 bit, Temp: 14 bit
	public final static byte kgsCONF_MEASURE_RES_8_12	= 0x01;				//Messaufloesung RH: 8  bit, Temp: 12 bit
	public final static byte kgsCONF_MEASURE_RES_10_13	= (byte) 0x80;		//Messaufloesung RH: 10 bit, Temp: 13 bit
	public final static byte kgsCONF_MEASURE_RES_11_11	= (byte) 0x81;		//Messaufloesung RH: 11 bit, Temp: 11 bit
	public final static byte kgsCONF_HEATER_ENABLE		= 0x04;				//Heizung anschalten
	
	private I2CManager gI2c;												//Verweis auf I2CManager
	
	protected HumiditySensor() {}
	
	/**
	 * Initialisierung des Sensors
	 */
	@Override
	public void init() 
	{
		gI2c = (I2CManager) Sensors.getI2C();
		if(gI2c.write(kgsADDRESS, kgsRESET))
		{
			setEnabled(true);
		}
		else
		{
			Logger.log(Status.ERROR, this.getClass().getSimpleName(), "init failed! Device not functional");
		}
	}

	/**
	 * Sensor zuruecksetzten
	 */
	public void reset()
	{
		gI2c.write(kgsADDRESS, kgsRESET);
	}
	
	/**
	 * Benutzerkonfiguration einstellen (Siehe Datenblatt)
	 * @param configuration		Konfigurationsbyte
	 */
	public void setUserConfiguration(byte configuration)
	{
		gI2c.write(kgsADDRESS, this.kgsWRITE_HEATER_CTRL_REG, configuration);
	}
	
	/**
	 * Benutzterkonfiguration auslesen (Siehe Datenblatt)
	 * @return	Gibt das Konfigurationsbyte zurueck
	 */
	public byte getUserConfiguration()
	{
		gI2c.write(kgsADDRESS, kgsREAD_RHT_REG1);
		byte config = gI2c.read(kgsADDRESS);
		return config;
	}
	
	/**
	 * Gibt die eingestellte Messaufloesung zurueck
	 * @return	Gibt die eingestellte Messaufloesung zurueck (Siehe Datenblatt)
	 */
	public byte getMeasurementResolution()
	{
		byte config = getUserConfiguration();
		byte res = (byte) (config & 0x81);
		return res;
	}
	
	/**
	 * Gibt zurueck ob die Heizung eingestellt ist oder nicht
	 * @return	True, wenn Heizung=ein; False, wenn Heizung=aus
	 */
	public boolean isHeaterEnabled()
	{
		byte config = getUserConfiguration();
		return Utils.isBitSet(config, 2);
	}
	
	/**
	 * Den Heizungswert einstellen
	 * @param value	Heizungswert als Integer (von 0 - 16)
	 */
	public void setHeaterValue(int value)
	{
		byte lsb = Utils.isolateBits(Utils.toBytes(value, 1)[0], 0 , 3);
		gI2c.write(kgsADDRESS,this.kgsWRITE_HEATER_CTRL_REG, lsb);
	}
	
	/**
	 * Gibt den momentanen Heizungswert zurueck
	 * @return	Heizungswert von 0-16
	 */
	public int getHeaterValue()
	{
		gI2c.write(kgsADDRESS, kgsREAD_HEATER_CTRL_REG);
		byte lsb = Utils.isolateBits(gI2c.read(kgsADDRESS), 0, 3);
		return (int)lsb;
	}
	
	/**
	 * Misst den momentanten Feuchtigkeitswert und Temperaturwert und gibt nur den Feuchtigkeitswert zurueck
	 * @return	feuchtigkeitswert als double
	 */
	public double getHumidityHoldMasterMode()
	{
		gI2c.write(kgsADDRESS, kgsMEASURE_HUM_HMM);
		byte[] result = gI2c.read(kgsADDRESS, 2);
		int humidity_code = Utils.toInt(result);
		
		return convertCodeToHumidity(humidity_code);
	}
	
	/**
	 * Misst den momentanten Feuchtigkeitswert und Temperaturwert und gibt nur den Feuchtigkeitswert zurueck
	 * @return	feuchtigkeitswert als double
	 */
	public double getHumidityNoHoldMasterMode()
	{
		gI2c.write(kgsADDRESS, this.kgsMEASURE_HUM_NO_HMM);
		byte[] result = gI2c.read(kgsADDRESS, 2);
		int humidity_code = Utils.toInt(result);
		
		return convertCodeToHumidity(humidity_code);
	}
	
	/**
	 * Misst den momentanten  Temperaturwert und gibt ihn zurueck
	 * @return	feuchtigkeitswert als double
	 */
	public double getTemperatureHoldMasterMode()
	{
		gI2c.write(kgsADDRESS,this.kgsMEASURE_TEMP_HMM);
		byte[] result = gI2c.read(kgsADDRESS, 2);
		int temp_code = Utils.toInt(result);
		
		return convertCodeToTemperature(temp_code);
	}
	
	/**
	 * Misst den momentanten  Temperaturwert und gibt ihn zurueck
	 * @return	feuchtigkeitswert als double
	 */
	public double getTemperatureNoHoldMasterMode()
	{
		gI2c.write(kgsADDRESS,this.kgsMEASURE_TEMP_NO_HMM);
		byte[] result = gI2c.read(kgsADDRESS, 2);
		int temp_code = Utils.toInt(result);
		
		return convertCodeToTemperature(temp_code);
	}
	
	/**
	 * Gibt den Temperaturwert zurueck von der vorherigen Feuchtigkeitsmessung
	 * @return	Temperaturwert als double
	 */
	public double getPreviousTemperature()
	{
		gI2c.write(kgsADDRESS, kgsREAD_TEMP_PREVIOUS);
		byte[] result = gI2c.read(kgsADDRESS, 2);
		int temp_code = Utils.toInt(result);
		
		return convertCodeToTemperature(temp_code);
	}
	
	/**
	 * Liest die Serial-Number aus des Bausteins
	 * @return	Serial-Number in 8 bytes (Siehe Datenblatt)
	 */
	public byte[] getSerialNumber()
	{
		gI2c.write(kgsADDRESS, Utils.toBytes(kgsREAD_ID_1ST_BYTE, 2), true);
		byte[] sna = Utils.reverseBytes(gI2c.read(kgsADDRESS, 4));
		gI2c.write(kgsADDRESS, Utils.toBytes(kgsREAD_ID_2ND_BYTE, 2), true);
		byte[] snb = Utils.reverseBytes(gI2c.read(kgsADDRESS, 4));
		byte[] all = new byte[8];
		for(int i = 0; i < 4; i++)
			all[i] = sna[i];
		for(int i = 0; i < 4; i++)
			all[i+4] = snb[i];
		return all;
	}
	
	/**
	 * Liest die Firmware-Revision aus
	 * @return	Firmware-Revision als byte (siehe Datenblatt fuer interpretation)
	 */
	public byte getFirmwareRevision()
	{
		gI2c.write(kgsADDRESS, Utils.toBytes(kgsREAD_FIRMWARE_REV, 2), true);
		return gI2c.read(kgsADDRESS);
	}
	
	/**
	 * Wandelt den empfangenen code des Bausteins in einen Luftfeuchtigkeitswert um
	 * @param humidity_code	Messwert des Bausteins
	 * @return				Gibt den Luftfeuchtigkeitswert zurueck
	 */
	private double convertCodeToHumidity(int humidity_code)
	{
		return ((125*humidity_code)/65536d)-6;
	}
	
	/**
	 * Wandelt den empfangenen code des Bausteins in einen Temperaturwert um
	 * @param temp_code		Messwert des Bausteins
	 * @return				Gibt den Temperaturwert zurueck
	 */
	private double convertCodeToTemperature(int temp_code)
	{
		return ((175.72*temp_code)/65536d) -6 ;
	}
}
