package org.mtforce.enocean;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mtforce.main.Logger;
import org.mtforce.main.Utils;

public class RORGDecoder 
{
	private Map<String,Integer> knownIds = new HashMap<String,Integer>();			//Liste aller bekannten IDs
	private List<RORGDecodeEvent> listeners = new ArrayList<RORGDecodeEvent>();		//Liste aller EventListener
	
	public RORGDecoder()
	{
		knownIds.put("Thermometer", Utils.toInt(new byte[]{0x01, (byte) 0x9b, (byte) 0xcf, 0x5e}));
		knownIds.put("Button", Utils.toInt(new byte[]{0x01, (byte) 0xa1, 0x31, (byte) 0xc0}));
	}
	
	/**
	 * Fügt einen neuen EventListener hinzu
	 * @param listener	EventListener
	 */
	public void addRORGDecodeEventListener(RORGDecodeEvent listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Entfernt einen EventListener
	 * @param listener	EventListener
	 */
	public void removeRORGDecodeEventListener(RORGDecodeEvent listener) {
		this.listeners.remove(listener);
	}
	
	/**
	 * Dekodiert ein Telegram. Momentan unterstützt 4BS und RPS
	 * @param telegram	Telegram welches dekodiert werden soll
	 */
	public void decode(byte[] telegram)
	{
		//System.out.println(Utils.byteToHexString(telegram[0]));
		if(telegram[0] == (byte)0xa5) //4BS Telegram
		{
			if(telegram.length == 10)
			{
				if(knownIds.containsValue(Utils.toInt(new byte[]{telegram[5], telegram[6], telegram[7], telegram[8]})))
				{
					byte teachIndicator = telegram[4];
					if(Utils.isBitSet(teachIndicator, 3))
					{
						double temp = 40-((40d/255d) * (double)telegram[3]);
						this.fireTemperatureEvent(temp);
					}
					else
					{
						Logger.log(Logger.Status.WARNING,"RORGDecoder" , "Teach-In Telegram received. Ignoring!");
					}
				}
				else
				{
					Logger.log(Logger.Status.WARNING, "RORGDecoder", "ID des Funkmoduls nicht bekannt!");
				}
			}
			else
			{
				Logger.log(Logger.Status.WARNING, "RORGDecoder", "Paket hat eine Invalide länge");
			}
		}
		else if(telegram[0] == (byte)0xf6)
		{
			if(telegram.length == 7)
			{
				if(Utils.isBitSet(telegram[6], 4))
				{
					if(Utils.isBitSet(telegram[6], 0)) {
						this.fireButtonEvent(1, 1);
					} else {
						if(Utils.isBitSet(telegram[1], 5))
							this.fireButtonEvent(1, 0);
						else
							this.fireButtonEvent( 0, 1);
					}
				}
				else
				{
					
					if(Utils.isolateBits(telegram[1], 5, 7) == 0x00)
						this.fireButtonEvent(0, 0);
					else
						this.fireButtonEvent(1, 1);
				}
			}
			else
			{
				Logger.log(Logger.Status.WARNING, "RORGDecoder", "Paket hat eine Invalide länge");
			}
		}
		else
		{
			Logger.log(Logger.Status.WARNING, "RORGDecoder", "Telegram wird nicht unterstützt");
		}
	}
	
	/**
	 * Löst das Temperaturempfangen-Event aus
	 * @param temp	Empfangene Temperatur
	 */
	public void fireTemperatureEvent(double temp)
	{
		for(RORGDecodeEvent event: listeners)
			event.thermometerReceived(temp);
	}
	
	/**
	 * Löst das Buttonpress-Event aus
	 * @param button1	Button-State von Button1
	 * @param button2	Button-State von Button2
	 */
	public void fireButtonEvent(int button1, int button2)
	{
		for(RORGDecodeEvent event: listeners)
			event.buttonReceived(button1, button2);
	}
}
