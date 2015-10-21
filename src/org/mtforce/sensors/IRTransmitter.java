package org.mtforce.sensors;

import org.mtforce.interfaces.GPIOManager;

public class IRTransmitter implements Sensor
{
	private int start;
	private double data;
	
	public void setOn() 
	{
		byte[] data = GPIOManager.write(new byte[3]);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
