package org.mtforce.interfaces;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

public class SPIManager 
{
	public static SpiDevice spi = null;
	
    public static void initialize() throws Exception
    {
        spi = SpiFactory.getInstance(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED, SpiDevice.DEFAULT_SPI_MODE);
        if(spi == null)
        {
        	throw new Exception("SPI failed to initialize. Is it not enabled on the RPI?");
        }
    }   
	
	public static byte[] write(byte ...packets)
	{
		try 
		{
			return spi.write(packets);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
