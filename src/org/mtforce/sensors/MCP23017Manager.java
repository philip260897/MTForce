package org.mtforce.sensors;
import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.jni.I2C;
public class MCP23017Manager implements Sensor {
	private int deviceAddress = 0x22;

	I2CBus bus;
//	public MCP23017Manager(I2CBus bus, int address) {
//        this.bus = bus;
//        this.deviceAddress = address;
//    }
	@Override
	public void init() {
		try {
			bus = I2CFactory.getInstance(I2CBus.BUS_1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void update() {
		
		int ret = I2C.i2cWriteByteDirect(bus.getFileDescriptor(), deviceAddress, (byte) 1);
		int reader = I2C.i2cReadByteDirect(bus.getFileDescriptor(), deviceAddress);
		System.out.println("Fehler?: "+ ret +" Daten" + reader);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
