package org.mtforce.main;

import org.mtforce.enocean.EnOceanPi;
import org.mtforce.enocean.OceanPacket;
import org.mtforce.enocean.OceanPacketReceivedEvent;
import org.mtforce.enocean.Response;
import org.mtforce.sensors.DOF9;
import org.mtforce.sensors.Sensor;
import org.mtforce.sensors.Thermometer;

import com.pi4j.io.serial.Serial;

public class Main 
{
	//TODO: sendPacketForResponse Timeout einfuehren
	public static void main(String[] args) 
	{
		Logger.console(true);
		
		try
		{
			Sensors.initialize();
			
			/*for(Sensor sensor : Sensors.getSensors())
			{
				if(sensor.isEnabled())
				{
					System.out.println(sensor.getClass().getSimpleName()+" AKTIVIERT!!!!!!!");
				}
			}
			
			EnOceanPi pi = new EnOceanPi();
			pi.init(Serial.DEFAULT_COM_PORT, 57600);
			if(pi.isEnabled())
			{
				pi.addOceanPacketReceivedEvent(new OceanPacketReceivedEvent()
				{
					@Override
					public void packetReceived(OceanPacket packet) {
						packet.println();
					}

					@Override
					public void responseReceived(Response response) {
						
					}
				});
				
				OceanPacket packet = new OceanPacket();
				packet.setPacketType(EnOceanPi.PACKETTYPE_COMMON_COMMAND);
				packet.setData((byte)0x02);
				packet.setDataOptional(null);
				packet.generateHeader();
				packet.println();
				Response resp = pi.sendPacketForResponse(packet);
			}*/

			Thermometer therm = Sensors.getThermometer();
			therm.setTCritical(20);
			therm.setConfiguration(Thermometer.kgsCONF_HYST_15 | Thermometer.kgsCONF_WIN_LOCK);
			therm.setResolution(Thermometer.kgsRES_5);
			if(therm.isEnabled())
			{
				System.out.println("Temperature: "+Sensors.getThermometer().getTemperature());
				System.out.println("ManufacturerID: "+Sensors.getThermometer().getManufacturerID());
				System.out.println("DeviceID: "+Sensors.getThermometer().getDeviceID());
				System.out.println("Revision: "+Sensors.getThermometer().getRevision());
				System.out.println("TCritical: "+Sensors.getThermometer().getTCriticalLimit());
				System.out.println("TUpper: "+Sensors.getThermometer().getTUpperLimit());
				System.out.println("TLower: "+Sensors.getThermometer().getTLowerLimit());
				System.out.println("isCritical: "+Sensors.getThermometer().isTemperatureCritical());
				System.out.println("isUpper: "+Sensors.getThermometer().isTemperatureUpper());
				System.out.println("isLower: "+Sensors.getThermometer().isTemperatureLower());
				System.out.println("isCriticalLockSet: "+Sensors.getThermometer().isCriticalLockSet());
				System.out.println("isWindowLockSet: "+Sensors.getThermometer().isWindowLockSet());
				System.out.println("isInterruptClearSet: "+Sensors.getThermometer().isInterruptClearSet());
				System.out.println("isAlertOutputStatusSet: "+Sensors.getThermometer().isAlertOutputStatusSet());
				System.out.println("isAlertOutputControl: "+Sensors.getThermometer().isAlertOutputControlSet());
				System.out.println("isAlertOutputSelectSet: "+Sensors.getThermometer().isAlertOutputSelectSet());
				System.out.println("isAlertOutputPolaritySet: "+Sensors.getThermometer().isAlertOutputPolaritySet());
				System.out.println("isAlertOutputModeSet: "+Sensors.getThermometer().isAlertOutputModeSet());
				if(Sensors.getThermometer().getHysteresis() == Thermometer.kgsCONF_HYST_15)
					System.out.println("Hysteresis: 1.5");
			}
			
			System.in.read();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void testThermoter()
	{
		Logger.log("Thermometer", "Checking availability...");
		Thermometer therm = Sensors.getThermometer();
		if(therm.isEnabled())
		{
			
			double temp = 
		}
		
	}
}
