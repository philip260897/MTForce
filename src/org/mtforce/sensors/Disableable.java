package org.mtforce.sensors;

public class Disableable 
{
	private boolean enabled = false;
	
	public Disableable()
	{
		
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
