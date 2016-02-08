package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;


/**
 * Beschreibung: 4-Kanal ADC
 * 		Kanal 1: Distanzsensor
 * 		Kanal 2: Lichtsnsor
 * 
 * Konstanten: Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: Modultest
 * 	getVoltage(int Kanal) 	- Spannungswert von bestimmten Kanal einlesen
 * 	getVoltage()			- Spannungswert von momentan ausgewaehlten Kanal einlesen
 */
public class ADC extends Sensor 
{
	public static final byte kgsADDRESS				= 0x68;	//Bausteinaddresse
	public static final byte kgsSTD_CONFIG			= (byte) 0x90;	//Standard Konfiguration
	
	public static final byte kgsPGA_GAIN_x1			= 0x00;	//PGA Gain Selection Bits PGA=1
	public static final byte kgsPGA_GAIN_x2 		= 0x01;	//PGA Gain Selection Bits, PGA=2
	public static final byte kgsPGA_GAIN_x4 		= 0x02;	//PGA Gain Selection Bits, PGA=4
	public static final byte kgsPGA_GAIN_x8 		= 0x03;	//PGA Gain Selection Bits, PGA=8
	
	public static final byte kgsSPS_240 			= 0x00;	//Sample Rate Selection Bit, 240 SPS, 12 bits
	public static final byte kgsSPS_60 				= 0x04;	//Sample Rate Selection Bit, 60 SPS, 14 bits
	public static final byte kgsSPS_15 				= 0x08;	//Sample Rate Selection Bit, 15 SPS, 16 bits
	
	public static final byte kgsONE_SHOT_CONV 		= 0x00;	//Conversion Mode Bit, One-Shot Conversion Mode
	public static final byte kgsCONTINUOUS_CONV 	= 0x10;	//Conversion Mode Bit, Continuous Conversion Mode
	
	public static final byte kgsSELECT_CH1 			= 0x00;	//Channel Selection Bits, Select Channel 1
	public static final byte kgsSELECT_CH2 			= 0x20;	//Channel Selection Bits, Select Channel 2
	public static final byte kgsSELECT_CH3 			= 0x40;	//Channel Selection Bits, Select Channel 3
	public static final byte kgsSELECT_CH4 			= 0x60;	//Channel Selection Bits, Select Channel 4
	
	public static final byte kgsNO_EFFECT 			= 0x00;	//One-Shot Conversion mode, No effect
	public static final int	 kgsINIT_NEW_CONV 		= 0x80;	//One-Shot Conversion mode, Initiate a new conversion.

	private I2CManager i2c;									//Verweis auf I2CManager
	
	
	/**
	 * Initialisiert den Baustein
	 */
	@Override
	public void init() 
	{
		i2c = (I2CManager)Sensors.getI2C();
	}
	
	/**
	 * Setzt die Standard Konfiguration
	 */
	public void setStandardConfiguration()
	{
		i2c.write(kgsADDRESS, kgsSTD_CONFIG);
	}
	
	/**
	 * Setzt die gewünschte Konfiguration
	 * @param configuration	Bausteinkonfiguration
	 */
	public void setConfiguration(byte configuration)
	{
		i2c.write(kgsADDRESS, configuration);
	}
	
	/**
	 * Returned die Gain-Konfiguration
	 * @return	Gibt die Gain-Konstante zurueck
	 */
	public int getGain()
	{
		byte packet = i2c.read(kgsADDRESS);
		packet = Utils.isolateBits(packet, 0, 1);
		return packet;
	}
	
	/**
	 * Returned die Conversions-Konfiguration
	 * @return	Gibt die Conversion-Mode-Konstante zurueck
	 */
	public int getConvMode()
	{
		byte packet = i2c.read(kgsADDRESS);
		return Utils.isolateBits(packet, 4, 4);
	}
	
	/**
	 * Returned das Ready Bit
	 * @return Gibt das "Ready" bit zurueck
	 */
	public int getReadyBit()
	{
		byte packet = i2c.read(kgsADDRESS);
		return Utils.isolateBits(packet, 7, 7);
	}
	
	/***
	 * Returned die SampleRate-Konfiguration
	 * @return Gibt die Samplerate-Konstante zurueck
	 */
	public int getSampleRate()
	{
		byte packet = i2c.read(kgsADDRESS);
		packet = Utils.isolateBits(packet, 2, 3);
		return packet;
	}
	
	/**
	 * Returned die Channel-Konfiguration
	 * @return	Gibt die Channel-Select-Konstante zurueck
	 */
	public int getChannelSelection()
	{
		byte packet = i2c.read(kgsADDRESS);
		packet = Utils.isolateBits(packet, 6, 5);
		return packet;
	}
}
