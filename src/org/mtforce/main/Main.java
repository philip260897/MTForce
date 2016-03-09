package org.mtforce.main;

import org.mtforce.enocean.EnOceanPi;
import org.mtforce.enocean.OceanPacket;
import org.mtforce.enocean.OceanPacketReceivedEvent;
import org.mtforce.enocean.Response;
import org.mtforce.sensors.Sensor;

import com.pi4j.io.serial.Serial;

public class Main 
{
	//TODO: sendPacketForResponse Timeout einfuehren
	public static void main(String[] args) 
	{
		try
		{
			Sensors.initialize();
			
			for(Sensor sensor : Sensors.getSensors()) {
				if(sensor.isEnabled())
					System.out.println(Sensors.class.getName());
			}
			
			EnOceanPi pi = new EnOceanPi();
			pi.init(Serial.DEFAULT_COM_PORT, 57600);
			if(pi.isEnabled())
			{
				System.out.println("EnOceanPi initialized");
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
			}
			else
			{
				System.out.println("EnOceanPi initialization failed!");
			}
			

			//System.out.println("Waited for response: "+Utils.byteToHexString(resp.getResponseCode()));*/
			
			System.in.read();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
