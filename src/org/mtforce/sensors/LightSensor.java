package org.mtforce.sensors;

import org.mtforce.main.Logger;
import org.mtforce.main.Logger.Status;

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
	
	
	
	protected LightSensor()
	{
		super();
		adc=Sensors.getAdc();
	}
	
	@Override
	public void init() 
	{
		adc = Sensors.getAdc();
		if(adc.isEnabled())
		{
			setEnabled(true);
		}
		else
		{
			Logger.log(Status.ERROR, this.getClass().getSimpleName(), "init failed! ADC not functional");
		}
	}
	
	public String getBrightness()
	{
		String[] brightness = {"Dunkel", "Hell"};
		adc.selectChannel(ADC.kgsCONF_SELECT_CH1);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double voltage = adc.getVoltage() / 2 * 5;
		return voltage < 0.700 ? brightness[0] + " " + voltage : brightness[1] + " " + voltage;
	}
}
