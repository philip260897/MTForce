package org.mtforce.sensors;

import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;

public class ADC extends Sensor {
	public static final byte kgsADDRESS				= 0x68;
	public static final byte kgsSTD_CONFIG			= (byte) 0x90;
	
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

	
	
	
	
	@Override
	public void init() {
		
	}

	@Override
	public void update() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public void setConfiguration(byte configuration)
	{
		Sensors.getI2C().write8(kgsADDRESS, configuration);
	}
	
	public int getGain()
	{
		byte packet = Sensors.getI2C().read8(kgsADDRESS);
		packet = Utils.isolateBits(packet, 0, 1);
		return packet;
	}
		
	public int getConvMode()
	{
		byte packet = Sensors.getI2C().read8(kgsADDRESS);
		return Utils.isolateBits(packet, 4, 4);
	}
	
	public int getReadyBit()
	{
		byte packet = Sensors.getI2C().read8(kgsADDRESS);
		return Utils.isolateBits(packet, 7, 7);
	}
	
	public int getSampleRate()
	{
		byte packet = Sensors.getI2C().read8(kgsADDRESS);
		packet = Utils.isolateBits(packet, 2, 3);
		return packet;
	}
	
	public int getChannelSelection()
	{
		byte packet = Sensors.getI2C().read8(kgsADDRESS);
		packet = Utils.isolateBits(packet, 6, 5);
		return packet;
	}
}
