package org.mtforce.interfaces;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.wiringpi.Spi;

public class SPIManager 
{
	private static int CHANNEL = 0;
	private static int CLOCK = 10000000;
	
	public static SpiDevice spi = null;
	
    public static void initialize() throws Exception
    {
        int fd = Spi.wiringPiSPISetup(CHANNEL, CLOCK);
        if (fd <= -1) {
            throw new Exception("SPIManager initializing failed. Is it not enabled on the RPI?");
        }
        
        spi = SpiFactory.getInstance(SpiChannel.CS0,
                SpiDevice.DEFAULT_SPI_SPEED, // default spi speed 1 MHz
                SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0
        
        System.out.println(fd);
    }   
	
	public static byte[] write(byte[] packets)
	{
		try {
			return spi.write(packets);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
