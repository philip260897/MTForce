package org.mtforce.interfaces;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

/**
 * Beschreibung: Kommunikation über das SPI Interface
 */
public class SPIManager implements CommunicationManager
{
	private static SpiDevice spi = null;
	
	/**
	 * Initialisiert den SPIManager auf 10MHz, CH0, DEFAULT_SPI_MODE
	 */
	@Override
    public void initialize() throws Exception
    {
        spi = SpiFactory.getInstance(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED*10, SpiDevice.DEFAULT_SPI_MODE);
        
        if(spi == null)
        {
        	throw new Exception("SPI failed to initialize. Is it not enabled on the RPI?");
        }
    }

	/**
	 * Schreibt die übergebenen Bytes auf den SPI-Bus
	 * @param bytes	Daten
	 */
	public void write(byte ...bytes)
	{
		try 
		{
			spi.write(bytes);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
