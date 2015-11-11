package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;

public class Barometer extends Sensor {

	public static final byte ADDRESS = 0x76;		 	//LAST BIT = !CSB (CSB = HARDWARE PIN)
	public static final byte RESET 	= 0x1E; 		//RESETS PROM
	
	public static final int PROM_READ = 0xA6;		//8-BIT DATA FOLLOWED BY ACK 2 TIMES (16BIT)
	
	public static final byte ADC_CONVERSION = 0x48; 	//CONVERSION OF ANALOG IN
	public static final byte ADC_READ = 0x00;		//8-BIT DATA FOLLOWED BY ACK 3 TIMES (24BIT)
	
	@Override
	public void init() {
		
		I2CManager.write((byte)ADDRESS, (byte)RESET);
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
