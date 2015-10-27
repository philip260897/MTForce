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
	 private I2CDevice mcp23017;
	 //Raspberry Pi's I2C bus
	 private static final int i2cBus = 1;
	 // Device address 
	 private static final int address = 0x77;
	 
	 
	@Override
	public void init() {
		try {
			
            bus = I2CFactory.getInstance(I2CBus.BUS_1);
            System.out.println("Connected to bus OK!!!");

            //get device itself
            
            mcp23017 = bus.getDevice(address);
            System.out.println("Connected to device OK!!!");
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void update() {
		
		//int ret = I2C.i2cWriteByteDirect(bus.getFileDescriptor(), deviceAddress, (byte) 1);
		//int reader = I2C.i2cReadByteDirect(bus.getFileDescriptor(), deviceAddress);
		//mcp23017.read(arg0, arg1, arg2)
		//System.out.println("Fehler?: "+ ret +" Daten" + reader);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
