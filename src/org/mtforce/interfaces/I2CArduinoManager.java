package org.mtforce.interfaces;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/**
 * NUR ZUM TESTEN
 * @author Philip
 *
 */
@Deprecated
public class I2CArduinoManager {
	private static I2CBus bus;
	private static I2CDevice current;
	private static byte arduinoAddress = (byte)0x03;

    public static void initialize() throws Exception
    {
    	bus = I2CFactory.getInstance(I2CBus.BUS_1);
    }  
    

	public static boolean write(byte address, byte reg, byte val)
	{
		try
		{
			current = bus.getDevice(arduinoAddress);
			
			byte[] b = {0x00, address, reg, val};
			
			current.write(b, 0, b.length);
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
			current = bus.getDevice(arduinoAddress);
			byte[] b = {0x00, address, val};
			
			current.write(b, 0, b.length);
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
			current = bus.getDevice(arduinoAddress);
			byte[] b = new byte[val.length+3];
			b[0] = 0x00;
			b[1] = address;
			b[2] = reg;
			for(int i = 3; i < b.length; i++)
				b[i] = val[i-3];
			
			current.write(b, 0, b.length);
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
			current = bus.getDevice(arduinoAddress);
			byte[] b = {0x01, address, reg};
			current.write(b, 0, b.length);
			
			Thread.sleep(300);

			return current.read();
		}
		catch(Exception ex)
		{
			return -1;
		}
	}
	
	public static int read(byte address)
	{
		try
		{
			current = bus.getDevice(arduinoAddress);
			byte[] b = {0x01, address};
			current.write(b, 0, b.length);
			
			Thread.sleep(300);

			return current.read();
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
			b[0] = (byte) read(address, reg);
			for(int i = 1; i < bytes; i++)
				b[i] = (byte) read(address);
			return b;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
}
