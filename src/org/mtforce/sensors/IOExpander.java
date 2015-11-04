package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Main;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class IOExpander extends Disableable implements Sensor
{
    private int ADDRESS =	0x20;
    private int GPIOA	=	0x14;
   
    private boolean ledState = false;
	
	@Override
	public void init() {

		if(I2CManager.write(ADDRESS, GPIOA, 0x00)) 
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
		if(!isEnabled())
			return;

	}

	public void setLedOn(boolean led)
	{
		if(!isEnabled())
			return;
		
		byte value = 0x01;
		if(!led)
			value = 0x00;
		ledState = led;
		
		I2CManager.write(ADDRESS, GPIOA, value);
	}
	
	public boolean getLedOn() {
		return ledState;
	}
	
	@Override
	public void dispose() {
		if(!isEnabled())
			return;
		
		I2CManager.write(ADDRESS, GPIOA, 0x00);
	}

}
