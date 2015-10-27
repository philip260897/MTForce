package org.mtforce.sensors;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class IOExpander implements Sensor
{
    //I2C bus
    private I2CBus bus;
    // Device object
    private I2CDevice device;
    
    private byte address = 0x20;
	
	@Override
	public void init() {
		try{
	        bus = I2CFactory.getInstance(I2CBus.BUS_1);
	        System.out.println("Connected to bus OK!!!");
	
	        //get device itself
	        device = bus.getDevice((byte)0x20);
	        System.out.println("Connected to device OK!!!");
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		try{
			System.out.println("I2c");
		device.write(0x14, (byte) 0x01);
		}catch(Exception ex){ex.printStackTrace();}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
