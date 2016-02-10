package org.mtforce.sensors;

import org.mtforce.main.Sensors;

/**
 * Beschreibung: Analoger Helligkeitssensor, haengt am ADC auf Kanal 2
 * 
 * Konstanten: NICHT Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: Modultest
 */

public class LightSensor extends Sensor{
	private ADC adc;
	public LightSensor()
	{
		super();
		adc=Sensors.getAdc();
	}
	public void init() {
		if(adc.isEnabled())
		{
			setEnabled(true);
		}
		else
		{
			System.out.println(this.getClass().getSimpleName() + ": init error! ADC not functional");
		}
		
	}
	
	public String getBrightness(){
		String[] brightness = {"Dunkel", "Hell"};
		//TODO Voltage von ADC getten
		double voltage = 0;
		if(voltage < 0.090){
			return brightness[0];
		}
		else {
			return brightness[1];
		}
		
		
	}


}
