package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Main;
import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

@Deprecated
public class IOExpander extends Sensor
{
	//Address and common register definition
    private byte ADDRESS =	0x21;
    private byte GPIOA	=	0x14;
   
    //track current LED State
    private boolean ledState = false;
    private boolean buttonState = false;
	
    private I2CManager i2c;
    
	@Override
	public void init() 
	{
		i2c = (I2CManager)Sensors.getI2C();
		//Gets called on Startup once. Check if Sensor is available and setEnabled(true);
		
		//Returns false if component not reachable
		if(i2c.write(ADDRESS, (byte)0x00, (byte)0xFE)) 
		{
			//doCheck();
			setEnabled(true);
			setLedOn(ledState);
			
			
		} 
		else 
		{
			System.out.println(this.getClass().getSimpleName() + ": init error! Device not functional");
		} 
	}

	public void setLedOn(boolean led)
	{
		//Check if Sensor is enabled or not
		if(!isEnabled())
			return;
		
		//register value if led is true(0x01) or false(0x00)
		byte value = (byte) (led ? 0x01 : 0x00);
		
		//write value to component
		i2c.write(ADDRESS, GPIOA, value);
		ledState = led;
	}
	
	public boolean getLedOn() {
		return ledState;
	}
	
	public boolean getButtonState() {
		return buttonState;
	}
	
	public void doCheck()
	{
		for(byte b = 0; b < 15; b++)
		{
			boolean t = checkRegister(b, 0, 1);
			System.out.println(b+": "+t);
		}
	}

	private boolean checkRegister(byte reg, int value, int bcount)
	{
		byte[] txPacket = Utils.toBytes(value, bcount);
		i2c.write(ADDRESS, reg, txPacket);
		
		byte[] rxPacket = i2c.read(ADDRESS, reg, 2);
		
		return Utils.compareBytes(txPacket, rxPacket);
	}
}
