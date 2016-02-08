package org.mtforce.sensors;

import java.util.ArrayList;
import java.util.List;

import org.mtforce.main.Sensors;

/**
 * Beschreibung: Analoger Distanzsensor, haengt am ADC
 * 
 * Konstanten: Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: Modultest
 */

public class DistanceSensor extends Sensor
{
	private List<Double[]> points = new ArrayList<Double[]>();
	private ADC adc;
	
	
	public DistanceSensor()
	{
		super();
		
		points.add(new Double[] {0.4, 1/80d});
		points.add(new Double[] {1.3, 1/20d});
		points.add(new Double[] {2.3, 1/10d});
		points.add(new Double[]{2.75, 1/8d});
		points.add(new Double[] {3.0, 1/7d});
		points.add(new Double[] {3.15, 1/6d});
		points.add(new Double[] {3.10, 1/5d});
		
		adc = Sensors.getAdc();
	}
	
	@Override
	public void init() 
	{
		if(adc.isEnabled())
		{
			
		}
		else
		{
			setEnabled(false);
		}
	}
	
	private double getDistance()
	{
		double distance = 0;
		
		
		return distance;
	}
	
	private double convertVoltageToDistance(double voltage)
	{
		Double[] lower = getPoint(voltage, false);
		Double[] higher = getPoint(voltage, true);
		
		System.out.println(lower[0] + " " + voltage + " " + higher[0]);
		
		double k = (higher[0] - lower[0])/(higher[1] - lower[1]);
		double distance = 1/((voltage)/k);
		
		System.out.println(distance + " cm");
		
		return -1d;
	}
	
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

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
