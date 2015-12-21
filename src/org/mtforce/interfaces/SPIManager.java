package org.mtforce.interfaces;

import java.io.IOException;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

public class SPIManager implements CommunicationManager
{
	private static SpiDevice spi = null;
	
	@Override
    public void initialize() throws Exception
    {
        spi = SpiFactory.getInstance(SpiChannel.CS0, 15600000, SpiDevice.DEFAULT_SPI_MODE);
        
        if(spi == null)
        {
        	throw new Exception("SPI failed to initialize. Is it not enabled on the RPI?");
        }
    }   
	
    //Write packets to SPI data line. Returns result if no erros occure. Null on error
    
	public byte[] write(byte ...packets)
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

	@Override
	public boolean write(byte address, byte reg, byte val) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean write(byte address, byte val) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean write(byte address, byte reg, byte... val) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int read(byte address, byte reg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] read(byte address, byte reg, int bytes) {
		// TODO Auto-generated method stub
		return null;
	}
}
