package org.mtforce.interfaces;
import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.I2C;
import com.pi4j.wiringpi.Spi;
public class I2CManager 
{	
	private I2CDevice device;
	private static I2CBus bus;
	private static int deviceAddress;
    public static void initialize() throws Exception
    {
    	
    }  
    
//	public static byte[] write(byte[] packets)
//	{
//		
//		return null;
//	}
	public void write(int address, byte data) throws IOException {
        	
			device.write(0x14, (byte) 0x01);
           
       
    }
}
