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

	@Override
	public boolean write8(byte address, byte reg, byte val) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean write16(byte address, byte reg, byte[] val) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte read8(byte address, byte reg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] read16(byte address, byte reg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean write8(byte address, byte val) {
		// TODO Auto-generated method stub
		return false;
	}   
	

}
