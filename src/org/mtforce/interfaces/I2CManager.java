package org.mtforce.interfaces;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.I2C;
import com.pi4j.wiringpi.Spi;
public class I2CManager 
{
	public static byte[] write(byte[] packets)
	{
		
		return null;
	}
	private static int CHANNEL = 0;
	private static int CLOCK = 10000000;
	
    public static void initialize() throws Exception
    {
        int fd = I2C.wiringPiI2CSetup(0); //0 = Falsch
        if (fd <= -1) {
            throw new Exception("SPIManager initializing failed.");
        }
    }   
}
