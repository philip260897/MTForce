package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;

public class Barometer extends Sensor {

	public static final byte ADDRESS 		= 0x76;			//LAST BIT = !CSB (CSB = HARDWARE PIN)
	public static final byte RESET 			= 0x1E; 		//RESETS PROM
	
	public static final byte PROM_READ 		= (byte) 0xA6;	//8-BIT DATA FOLLOWED BY ACK 2 TIMES (16BIT)
	
	public static final byte ADC_CONVERSION = 0x48; 		//CONVERSION OF ANALOG IN
	public static final byte ADC_READ 		= 0x00;			//8-BIT DATA FOLLOWED BY ACK 3 TIMES (24BIT)
	
	
	private I2CManager i2c;
	
	@Override
	public void init() {
		i2c = (I2CManager) Sensors.getI2C();
		i2c.write(ADDRESS, RESET);
	}
	
	public void reset(){
		i2c.write(ADDRESS, RESET);
	}
	
	public byte[] readProm(){
		i2c.write(ADDRESS, PROM_READ);
		return i2c.read(ADDRESS, 2);
	}
	
	public void conversion(){
		i2c.write(ADDRESS, ADC_CONVERSION);
	}
	
	public void readADC(){
		i2c.write(ADDRESS, ADC_READ);
	}
	
	
}
