package org.mtforce.sensors;

public class Sensor 
{
	private boolean enabled = false;									//True wenn Sensor Aktiv, False wenn Inaktiv
	
	public Sensor()
	{
		
	}
	
	public void init()													//Deaktiviert den Sensor
	{
		setEnabled(false);
	}
	
	@Deprecated
	public void update()												//Ueberprueft ob Sensor aktiv ist
	{
		if(!isEnabled())
			return;
	}
	
	@Deprecated
	public void dispose()												//Ueberprueft ob Sensor aktiv ist
	{
		if(!isEnabled())
			return;
	}
	
	public boolean isEnabled()											//Gibt zurueck ob Sensor aktiv oder inaktiv ist
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)								//Setzt ob Sensor aktiv oder inaktiv ist
	{
		this.enabled = enabled;
	}
}
