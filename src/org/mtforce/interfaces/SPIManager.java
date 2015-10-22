package org.mtforce.interfaces;

import com.pi4j.wiringpi.Spi;

public class SPIManager 
{
	private static int CHANNEL = 0;
	private static int CLOCK = 10000000;
	
    public static void initialize() throws Exception
    {
        int fd = Spi.wiringPiSPISetup(CHANNEL, CLOCK);
        if (fd <= -1) {
            throw new Exception("SPIManager initializing failed.");
        }
    }   
	
	public static byte[] write(byte[] packets)
	{
		return null;
	}
}
