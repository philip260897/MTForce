package org.mtforce.sensors;

public class ADC extends Sensor {
	public static final byte PGA_GAIN_x1		= 0x00;	//PGA Gain Selection Bits PGA=1
	public static final byte PGA_GAIN_x2 		= 0x01;	//PGA Gain Selection Bits, PGA=2
	public static final byte PGA_GAIN_x4 		= 0x02;	//PGA Gain Selection Bits, PGA=4
	public static final byte PGA_GAIN_x8 		= 0x03;	//PGA Gain Selection Bits, PGA=8
	public static final byte SPS_240 			= 0x00;	//Sample Rate Selection Bit, 240 SPS, 12 bits
	public static final byte SPS_60 			= 0x04;	//Sample Rate Selection Bit, 60 SPS, 14 bits
	public static final byte SPS_15 			= 0x08;	//Sample Rate Selection Bit, 15 SPS, 16 bits
	public static final byte ONE_SHOT_CONV 		= 0x00;	//Conversion Mode Bit, One-Shot Conversion Mode
	public static final byte CONTINUOUS_CONV 	= 0x10;	//Conversion Mode Bit, Continuous Conversion Mode
	public static final byte SELECT_CH1 		= 0x00;	//Channel Selection Bits, Select Channel 1
	public static final byte SELECT_CH2 		= 0x20;	//Channel Selection Bits, Select Channel 2
	public static final byte SELECT_CH3 		= 0x40;	//Channel Selection Bits, Select Channel 3
	public static final byte SELECT_CH4 		= 0x60;	//Channel Selection Bits, Select Channel 4
	public static final byte NO_EFFECT 			= 0x00;	//One-Shot Conversion mode, No effect
	public static final int INIT_NEW_CONV 		= 0x80;	//One-Shot Conversion mode, Initiate a new conversion.

	
	
	
	
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
}
