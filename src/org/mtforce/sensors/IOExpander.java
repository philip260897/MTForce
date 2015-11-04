package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Main;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class IOExpander extends Disableable implements Sensor
{
	//Address and common register definition
    private byte ADDRESS =	0x20;
    private byte GPIOA	=	0x14;
   
    //track current LED State
    private boolean ledState = false;
	
	@Override
	public void init() 
	{
		//Gets called on Startup once. Check if Sensor is available and setEnabled(true);
		
		//Returns false if component not reachable
		if(I2CManager.write(ADDRESS, GPIOA, (byte)0x00)) 
		{
			setEnabled(true);
			setLedOn(ledState);
		} 
		else 
		{
			System.out.println(this.getClass().getSimpleName() + ": init error! Device not functional");
		} 
	}

	@Override
	public void update() {

		//Call this method to update specific data. Used to read from component and update tracking variables
		if(!isEnabled())
			return;

		
		

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
	
	@Override
	public void dispose() {
		//Gets called on shutdown
		if(!isEnabled())
			return;
		
		//Turn off the LED on shutdown
		I2CManager.write(ADDRESS, GPIOA, (byte)0x00);
	}

}
