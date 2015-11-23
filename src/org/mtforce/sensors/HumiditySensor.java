package org.mtforce.sensors;

public class HumiditySensor extends Sensor {
	public final static byte ADDRESS 				= 0x80; 	//DEVICE ADDRESS
	public final static byte MEASURE_HUM_HMM 		= 0xE5;		//Measure Relative Humidity, Hold Master Mode
	public final static byte MEASURE_HUM_NO_HMM 	= 0xF5;		//Measure Relative Humidity, No Hold Master Mode
	public final static byte MT_HMM 				= 0xE3;		//Measure Temperature, Hold Master Mode
	public final static byte MT_NHMM 				= 0xF3;		//Measure Temperature, No Hold Master Mode
	public final static byte READ_TEMP_PREVIOUS 	= 0xE0;		//Read Temperature Value from Previous RH Measurement
	public final static byte RESET 					= 0xFE;		//Reset
	public final static byte WRITE_RHT_REG1 		= 0xE6;		//Write RH/T User Register 1
	public final static byte READ_RHT_REG1 			= 0xE7;		//Read RH/T User Register 1
	public final static byte WRITE_HEATER_CTRL_REG 	= 0x51;		//Write Heater Control Register
	public final static byte READ_HEATER_CTRL_REG 	= 0x11;		//Read Heater Control Register
	public final static byte READ_ID_1ST_BYTE 		= 0xFA0F;	//Read Electronic ID 1st Byte
	public final static byte WRITE_ID_1ST_BYTE 		= 0xFCC9;	//Read Electronic ID 2nd Byte
	public final static byte READ_FIRMWARE_REV		= 0x84B8;	//Read Firmware Revision

	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
