package org.mtforce.interfaces;
import java.io.IOException;

import org.mtforce.main.Utils;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.I2C;
import com.pi4j.wiringpi.Spi;


public class I2CManager implements CommunicationManager
{	
	/*private static I2CBus bus;
	private static I2CDevice current;*/

	@Override
    public void initialize() throws Exception
    {
    	//bus = I2CFactory.getInstance(I2CBus.BUS_1);
    }  

	@Override
	public boolean write8(byte address, byte reg, byte val) {
		int fd = I2C.wiringPiI2CSetup(address);
		int ret = I2C.wiringPiI2CWriteReg8(fd, reg, val);
		return true;
	}


	@Override
	public boolean write16(byte address, byte reg, byte[] val) {
		int fd = I2C.wiringPiI2CSetup(address);
		byte b = val[0];
		val[0] = val[1];
		val[1] = b;
		int value = Utils.toInt(val);
		int ret = I2C.wiringPiI2CWriteReg16(fd, reg, value);
		return true;
	}


	@Override
	public byte read8(byte address, byte reg) {
		int fd = I2C.wiringPiI2CSetup(address);
		byte xMSB = (byte) I2C.wiringPiI2CReadReg8(fd, reg);
		return xMSB;
	}


	@Override
	public byte[] read16(byte address, byte reg) {
		int fd = I2C.wiringPiI2CSetup(address);
		int xMSB =  I2C.wiringPiI2CReadReg16(fd, reg);
		byte[] packet = Utils.toBytes(xMSB, 2);
		
		return packet;
	}


	@Override
	public boolean write8(byte address, byte val) {
		int fd = I2C.wiringPiI2CSetup(address);
		I2C.wiringPiI2CWrite(fd, val);
		return false;
	}
}
