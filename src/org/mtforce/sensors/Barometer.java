package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Utils;

public class Barometer extends Sensor {

	public static final byte ADDRESS 		= 0x76;	//LAST BIT = !CSB (CSB = HARDWARE PIN)
	public static final byte RESET 			= 0x1E; //RESETS PROM
	
	public static final byte PROM_READ 		= (byte) 0xA6;	//8-BIT DATA FOLLOWED BY ACK 2 TIMES (16BIT)
	
	public static final byte ADC_CONVERSION = 0x48; //CONVERSION OF ANALOG IN
	public static final byte ADC_READ 		= 0x00;	//8-BIT DATA FOLLOWED BY ACK 3 TIMES (24BIT)
	
	@Override
	public void init() {
		
		I2CManager.write(ADDRESS, RESET);
	}

	@Override
	public void update() {
		I2CManager.write(ADDRESS, PROM_READ);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
