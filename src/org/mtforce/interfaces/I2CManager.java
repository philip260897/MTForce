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
	
	public static boolean write(byte address,  byte val)
	{
		try
		{
			current = bus.getDevice(address);
			current.write((byte)val);
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}	
	
	public static boolean write(byte address, byte reg, byte...val)
	{
		try
		{
			current = bus.getDevice(address);
			current.write(reg, val[0]);
			for(int i = 1;i < val.length;i++)
				current.write(val[i]);
		}
		catch(Exception ex)
		{
			return false;
		}
		return true;
	}
	
	public static int read(byte address, byte reg)
	{
		try
		{
			current = bus.getDevice(address);
			return current.read(reg);
		}
		catch(Exception ex)
		{
			return -1;
		}
	}

	public static byte[] read(byte address, byte reg, int bytes)
	{
		byte[] b = new byte[bytes];
		try
		{
			current = bus.getDevice(address);
			b[0] = (byte) current.read(reg);
			for(int i = 1; i < bytes; i++)
				b[i] = (byte) current.read();
			return b;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
}
