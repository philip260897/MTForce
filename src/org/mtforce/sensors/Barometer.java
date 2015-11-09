package org.mtforce.sensors;

public class Barometer extends Sensor {

	public static final int ADDRESS = 0x76; //LAST BIT = !CSB (CSB = HARDWARE PIN)
	public static final int RESET 	= 0x1E;
	
	public static final int PROM_READ = 0xA6;
	
	public static final int ADC_READ = 0x00;
	public static final int ADC_CONVERSION = 0x48;
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
