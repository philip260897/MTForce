package org.mtforce.sensors;

import org.mtforce.main.Sensors;

/**
 * Beschreibung: Analoger Helligkeitssensor, haengt am ADC auf Kanal 2
 * 
 * Konstanten: NICHT Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: Modultest, Kommentare hinzufuegen
 */
public class LightSensor extends Sensor
{
	private ADC adc;
	
	public LightSensor()
	{
		super();
		adc=Sensors.getAdc();
	}
	
	@Override
	public void init() 
	{
		if(adc.isEnabled())
		{
			setEnabled(true);
		}
		else
		{
			System.out.println(this.getClass().getSimpleName() + ": init error! ADC not functional");
		}
	}
	
	public String getBrightness()
	{
		String[] brightness = {"Dunkel", "Hell"};
		double voltage = adc.getVoltage();
		return voltage < 0.0900 ? brightness[0] : brightness[1];
	}
}
