package org.mtforce.interfaces;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.I2C;
import com.pi4j.wiringpi.Spi;
public class I2CManager 
{
	private static I2CBus bus;
	
    public static void initialize() throws Exception
    {
    	
    }  
    
	public static byte[] write(byte[] packets)
	{
		
		return null;
	}
}
