package org.mtforce.sensors;

import java.util.ArrayList;
import java.util.List;

import org.mtforce.main.Logger;
import org.mtforce.main.Logger.Status;

/**
 * Beschreibung: Analoger Distanzsensor, haengt am ADC
 * 	Spannungswerte werden linear Interpoliert nach der Kennlinie im Datenblatt
 * 
 * Konstanten: Komplett
 * Funktionen: Komplett
 * 
 * TODO: 	Modultest
 * 			getDistance() -	Geschrieben aber nicht getestet
 */

public class DistanceSensor extends Sensor
{
	private List<Double[]> points = new ArrayList<Double[]>();	//Kennlinienwerte laut Datenblatt
	private ADC adc;											//Verweis auf ADC
	
	
	protected DistanceSensor()
	{
		super();
		
		points.add(new Double[] {0.4, 1/80d});
		points.add(new Double[] {1.3, 1/20d});
		points.add(new Double[] {2.3, 1/10d});
		points.add(new Double[]{2.75, 1/8d});
		points.add(new Double[] {3.0, 1/7d});
		points.add(new Double[] {3.15, 1/6d});
		points.add(new Double[] {3.10, 1/5d});
	}
	
	/**
	 * Initialisiert Baustein. (Wird nur initialisiert wenn ADC aktiv ist)
	 */
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
	
	/**
	 * Gibt den Momentanen Distanzwert des Distanzsensors zurueck
	 * @return	Distanzwert in cm
	 */
	private double getDistance()
	{
		double distance = 0;	
		if(adc.isEnabled())
		{
			adc.selectChannel(ADC.kgsCONF_SELECT_CH2);
			distance = adc.getVoltage();
			distance = convertVoltageToDistance(distance);
		}
		return distance;
	}
	
	/**
	 * Wandelt einen Spannungswert in einen Distanzwert um
	 * @param voltage	Spannung in Volt
	 * @return			Gibt einen Distanzwert in cm zurueck
	 */
	private double convertVoltageToDistance(double voltage)
	{
		Double[] lower = getPoint(voltage, false);
		Double[] higher = getPoint(voltage, true);
		
		double k = (higher[0] - lower[0])/(higher[1] - lower[1]);
		double distance = k * (1/voltage);
		
		return distance;
	}
	
	/**
	 * Gibt einen darueberliegenden oder darunterliegenden Referenzwert zurueck. Benoetigt fuer Lineare Interpolation
	 * @param voltage	Spannungswert in Volt
	 * @param above		Waehlt den ueberliegenden Referenzwert aus oder den darunterliegenden
	 * @return			Gibt ein Double-Array zurueck welches x und y Koordinaten zurueck
	 */
	private Double[] getPoint(double voltage, boolean above)
	{
		for(int i = 0; i < points.size(); i++)
		{
			Double[] point = points.get(i);
			if(above)
			{
				if(voltage < point[0])
				{
					return point;
				}
			}
			else
			{
				if(voltage < point[0] && i != 0)
				{
					return points.get(i-1);
				}
			}
		}
		return new Double[]{-1d,-1d};
	}
}
