package org.mtforce.interfaces;
import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.I2C;
import com.pi4j.wiringpi.Spi;
public class I2CManager 
{	
	
	private static I2CBus bus;

	private static I2CDevice current;

    public static void initialize() throws Exception
    {
    	bus = I2CFactory.getInstance(I2CBus.BUS_1);
    }  
    

	public static boolean write(byte address, byte reg, byte val)
	{
		try
		{
			current = bus.getDevice(address);
			current.write(reg, (byte)val);
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}

}
