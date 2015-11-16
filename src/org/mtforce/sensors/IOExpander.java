package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Main;
import org.mtforce.main.Utils;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class IOExpander extends Sensor
{
	//Address and common register definition
    private byte ADDRESS =	0x20;
    private byte GPIOA	=	0x14;
   
    //track current LED State
    private boolean ledState = false;
    private boolean buttonState = false;
	
	@Override
	public void init() 
	{
		//Gets called on Startup once. Check if Sensor is available and setEnabled(true);
		
		//Returns false if component not reachable
		if(I2CManager.write(ADDRESS, (byte)0x00, (byte)0xFE)) 
		{
			System.out.println("CHECK: "+checkRegister(GPIOA, 0, 1));
			
			setEnabled(true);
			setLedOn(ledState);
			
			
		} 
		else 
		{
			System.out.println(this.getClass().getSimpleName() + ": init error! Device not functional");
		} 
	}

	@Override
	public void update() 
	{
		//Call this method to update specific data. Used to read from component and update tracking variables
		super.update();
		
		byte button = (byte)(I2CManager.read(ADDRESS, (byte)0x12) & (byte)0x02);
		buttonState = false;
		if(button == 0x02)
			buttonState = true;
	}

	public void setLedOn(boolean led)
	{
		//Check if Sensor is enabled or not
		if(!isEnabled())
			return;
		
		//register value if led is true(0x01) or false(0x00)
		byte value = (byte) (led ? 0x01 : 0x00);
		
		//write value to component
		I2CManager.write(ADDRESS, GPIOA, value);
		ledState = led;
	}
	
	public boolean getLedOn() {
		return ledState;
	}
	
	public boolean getButtonState() {
		return buttonState;
	}
	
	@Override
	public void dispose() {
		//Gets called on shutdown
		super.dispose();
		
		//Turn off the LED on shutdown
		I2CManager.write(ADDRESS, GPIOA, (byte)0x00);
	}

	private boolean checkRegister(byte reg, int value, int bcount)
	{
		byte[] txPacket = Utils.toBytes(value, bcount);
		I2CManager.write(ADDRESS, reg, txPacket);
		
		byte[] rxPacket = I2CManager.read(ADDRESS, reg, bcount);
		
		return Utils.compareBytes(txPacket, rxPacket);
	}
}
