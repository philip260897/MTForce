package org.mtforce.interfaces;
import java.io.IOException;

import org.mtforce.main.Utils;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.I2C;
import com.pi4j.wiringpi.Spi;

/**
 * Beschreibung: Kommunikation ueber das I2C Interface
 * 
 * Konstanten: Komplett
 * Funktionen: Komplett
 * 
 * TODO: Modultest
 */
public class I2CManager implements CommunicationManager
{	
	private static I2CBus bus;
	private static I2CDevice current;

	/**
	 * Initialisiert das I2C-Interface
	 */
	@Override
    public void initialize() throws Exception
    {
    	bus = I2CFactory.getInstance(I2CBus.BUS_1);
    }  


	public boolean write(byte address, byte val) {
		try
		{
			current = bus.getDevice(address);
			current.write(val);
			return true;
		}catch(Exception ex){}
		return false;
	}
	
	public boolean write(byte address, byte[] val, boolean highByteFirst) {
		try
		{
			current = bus.getDevice(address);
			if(highByteFirst)
				val = Utils.reverseBytes(val);
			
			current.write(val, 0, val.length);
			return true;
		}
		catch(Exception ex){}
		return false;
	}
	
	public boolean write(byte address, byte reg, byte val) 
	{
		try 
		{
			current = bus.getDevice(address);
			current.write(reg, val);
			return true;
		} 
		catch (IOException e) {}
		return false;
	}

	public boolean write(byte address, byte reg, byte[] val) {
		return write(address, reg, val, false);
	}

	public boolean write(byte address, byte reg, byte[] val, boolean highByteFirst) {
		try
		{
			current = bus.getDevice(address);
			if(highByteFirst)
				val = Utils.reverseBytes(val);
			
			System.out.println("W: 0x"+Utils.byteToHexString(val[0]) + " 0x"+Utils.byteToHexString(val[1]));
			
			current.write(reg, val, 0, val.length);
			return true;
		}
		catch(Exception ex){}
		return false;
	}


	public byte read(byte address) {
		try
		{
			current = bus.getDevice(address);
			return (byte)current.read();
		}
		catch(Exception ex){}
		return -1;
	}
	
	public byte read(byte address, byte reg) {
		try
		{
			current = bus.getDevice(address);
			return (byte)current.read(reg);
		}
		catch(Exception ex){}
		return -1;
	}

	public byte[] read (byte address, int bytes) {
		try
		{
			current = bus.getDevice(address);
			byte[] buffer = new byte[bytes];
			current.read(buffer, 0, buffer.length);
			return buffer;
		}catch(Exception ex){}
		return null;
	}
	
	public byte[] read(byte address, byte reg, int bytes) {
		return read(address, reg, bytes, false);
	}
	
	public byte[] read(byte address, byte reg, int bytes, boolean lowByteFirst) {
		try
		{
			current = bus.getDevice(address);
			byte[] buffer = new byte[bytes];
			current.read(reg, buffer, 0, buffer.length);
			if(lowByteFirst)
				buffer = Utils.reverseBytes(buffer);
			System.out.println("R: 0x"+Utils.byteToHexString(buffer[0]) + " 0x"+Utils.byteToHexString(buffer[1]));
			return buffer;
		}
		catch(Exception ex){}
		return null;
	}
}
