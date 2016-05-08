package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Logger;
import org.mtforce.main.Utils;
import org.mtforce.main.Logger.Status;


/**
 * Beschreibung: 4-Kanal ADC
 * 		Kanal 1: Distanz-Sensor
 * 		Kanal 2: Licht-Sensor
 * 
 * Konstanten: Komplett
 * Funktionen: NICHT Komplett
 * 
 * FUERS PROTOKOLL: Wenn in one-Shot Konversion => Siehe Datenblatt für Konversion bevor auslesen
 */
public class ADC extends Sensor 
{
	public static final byte kgsADDRESS					= 0x69;	//Bausteinadresse
	public static final byte kgsSTD_CONFIG				= (byte) 0x90;	//Standard Konfiguration
	
	public static final byte kgsCONF_PGA_GAIN_x1		= 0x00;	//PGA Gain Selection Bits PGA=1
	public static final byte kgsCONF_PGA_GAIN_x2 		= 0x01;	//PGA Gain Selection Bits, PGA=2
	public static final byte kgsCONF_PGA_GAIN_x4 		= 0x02;	//PGA Gain Selection Bits, PGA=4
	public static final byte kgsCONF_PGA_GAIN_x8 		= 0x03;	//PGA Gain Selection Bits, PGA=8
	
	public static final byte kgsCONF_RES_12 			= 0x00;	//Sample Rate Selection Bit, 240 SPS, 12 bits
	public static final byte kgsCONF_RES_14 			= 0x04;	//Sample Rate Selection Bit, 60 SPS, 14 bits
	public static final byte kgsCONF_RES_16 			= 0x08;	//Sample Rate Selection Bit, 15 SPS, 16 bits
	
	public static final byte kgsCONF_ONE_SHOT_CONV 		= 0x00;	//Conversion Mode Bit, One-Shot Conversion Mode
	public static final byte kgsCONF_CONTINUOUS_CONV 	= 0x10;	//Conversion Mode Bit, Continuous Conversion Mode
	
	public static final byte kgsCONF_SELECT_CH1 		= 0x00;	//Channel Selection Bits, Select Channel 1
	public static final byte kgsCONF_SELECT_CH2 		= 0x20;	//Channel Selection Bits, Select Channel 2
	public static final byte kgsCONF_SELECT_CH3 		= 0x40;	//Channel Selection Bits, Select Channel 3
	public static final byte kgsCONF_SELECT_CH4 		= 0x60;	//Channel Selection Bits, Select Channel 4
	
	public static final byte kgsNO_EFFECT 				= 0x00;	//One-Shot Conversion mode, No effect
	public static final int	 kgsCONF_INIT_NEW_CONV 		= 0x80;	//One-Shot Conversion mode, Initiate a new conversion.
	
	private I2CManager i2c;									//Verweis auf I2CManager
	private byte gSelected_Channel = kgsCONF_SELECT_CH1;
	private int currentResolution;
	
	protected ADC() {}
	
	/**
	 * Initialisiert den Baustein
	 */
	@Override
	public void init() 
	{
		i2c = (I2CManager)Sensors.getI2C();
		if(i2c.write(kgsADDRESS, kgsSTD_CONFIG))
		{
			setEnabled(true);
		}
		else
		{
			Logger.log(Status.ERROR, this.getClass().getSimpleName(), "init failed! Device not functional");
		}
	}
	
	/**
	 * Setzt die Standard Konfiguration
	 */
	public void setStandardConfiguration()
	{
		i2c.write(kgsADDRESS, kgsSTD_CONFIG);
	}
	
	/**
	 * Wenn der ADC im One-Shot Conversion Mode ist, wird eine Umwandlung gestartet
	 * Wichtig: Je nach Samplerate muss eine bestimmte zeit gewartet werden, bevor man den gemessenen Wert ausliest (Siehe Datenblatt)
	 */
	public void startConversion()
	{
		if(getConvMode() == ADC.kgsCONF_ONE_SHOT_CONV)
		{
			i2c.write(kgsADDRESS, (byte)(getConfiguration() | 0x80));
		}
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
	 * Ändert den Kanal auf dem man Spannungswerte misst
	 * @param channel	Kanal auf dem gemessen werden soll
	 */
	public void selectChannel(byte channel)
	{
		setConfiguration((byte)((getConfiguration() & 0x9F) | channel));
	}
	
	/**
	 * Liest das Konfigurationsregister aus dem Baustein aus.
	 * @return	Konfigurationsregister
	 */
	public byte getConfiguration()
	{
		return i2c.read(kgsADDRESS, 3)[2];
	}
	
	/**
	 * Gibt die Gain-Konfiguration zurück
	 * @return	Gibt die Gain-Konstante zurück
	 */
	public int getGain()
	{
		byte packet = getConfiguration();
		packet = Utils.isolateBits(packet, 0, 1);
		return packet;
	}
	
	/**
	 * Gibt die Conversions-Konfiguration zurück
	 * @return	Gibt die Conversion-Mode-Konstante zurück
	 */
	public int getConvMode()
	{
		byte packet = getConfiguration();
		return Utils.isolateBits(packet, 4, 4);
	}
	
	/**
	 * Gibt das Ready Bit zurück
	 * @return Gibt das "Ready" bit zurück
	 */
	public int getReadyBit()
	{
		byte packet = getConfiguration();
		return Utils.isolateBits(packet, 7, 7);
	}
	
	/***
	 * Gibt die SampleRate-Konfiguration zurück
	 * @return Gibt die Samplerate-Konstante zurück
	 */
	public int getSampleRate()
	{
		byte packet = getConfiguration();
		packet = Utils.isolateBits(packet, 2, 3);
		return packet;
	}
	
	/**
	 * Gibt die Channel-Konfiguration zurück
	 * @return	Gibt die Channel-Select-Konstante zurück
	 */
	public int getChannelSelection()
	{
		byte packet1 = Utils.isolateBits(getConfiguration(), 6, 5);
		return packet1;
	}
	
	/**
	 * Gemessenen Spannungswert auslesen
	 * @return	Spannung in Volt
	 */
	public double getVoltage()
	{
		byte[] packet = i2c.read(kgsADDRESS, 3);
		
		byte config = Utils.isolateBits(packet[2], 2, 3);
		double res = 2048;
				
		packet = new byte[]{packet[0], packet[1]};
		short wert = (short)Utils.toInt(Utils.reverseBytes(packet));
		if(config == ADC.kgsCONF_RES_12) res = 2048;
		if(config == ADC.kgsCONF_RES_14) res = 8192;
		if(config == ADC.kgsCONF_RES_16) res = 32768;
		
		//System.out.println(Utils.byteToHexString(packet[0]) + " " + Utils.byteToHexString(packet[1]));
		
		double volts = (double)wert * (2.048/res);
		return volts;
	}
}
