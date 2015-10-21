package org.mtforce.sensors;

import org.mtforce.main.GPIOManager;

public class IRTransmitter implements Sensor{
	private int start;
	private double data;
	public double setOn() {
		data = GPIOManager.write();
		return data;
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
