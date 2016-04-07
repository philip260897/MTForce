package org.mtforce.sensors;

public class Sensor 
{
	private boolean enabled = false;	//True wenn Sensor Aktiv, False wenn Inaktiv
	
	public Sensor()
	{
		
	}
	
	/**
	 * Deaktiviert den Sensor
	 */
	public void init()
	{
		setEnabled(false);
	}
	
	/**
	 * Gibt zurück ob der Sensor aktiv ist oder nicht
	 * @return
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/**
	 * Setzt ob Sensor aktiv oder inaktiv ist
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}
