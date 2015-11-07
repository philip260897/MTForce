package org.mtforce.sensors;

public class Sensor 
{
	private boolean enabled = false;
	
	public Sensor()
	{
		
	}
	
	public void init()
	{
		setEnabled(false);
	}
	
	public void update()
	{
		if(!isEnabled())
			return;
	}
	
	public void dispose()
	{
		if(!isEnabled())
			return;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}
