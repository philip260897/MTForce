package org.mtforce.interfaces;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

/**
 * Beschreibung: Kommunikation ueber das SPI Interface
 * 
 * Konstanten: Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: write und read funktionen, Modultest
 */
public class SPIManager implements CommunicationManager
{
	private static SpiDevice spi = null;
	
	/**
	 * Initialisiert den SPIManager auf 16MHz, CH0, DEFAULT_SPI_MODE
	 */
	@Override
    public void initialize() throws Exception
    {
        spi = SpiFactory.getInstance(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED/1000, SpiDevice.DEFAULT_SPI_MODE);
        
        if(spi == null)
        {
        	throw new Exception("SPI failed to initialize. Is it not enabled on the RPI?");
        }
    }

	
	public void write(byte ...bytes)
	{
		try {
			spi.write(bytes);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//DEBUG: noch nicht fertig
	/*public boolean write(byte address, byte val) {
		// TODO Auto-generated method stub
		//byte[] packet = new byte[] {address, val};
		try {
			spi.write(address, val);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}*/

}
