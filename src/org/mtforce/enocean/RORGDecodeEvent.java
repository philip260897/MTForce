package org.mtforce.enocean;

public interface RORGDecodeEvent 
{
	public void thermometerReceived(double temperature);
	
	public void buttonReceived(int button1, int button2);
}
