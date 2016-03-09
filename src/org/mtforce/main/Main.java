package org.mtforce.main;

import org.mtforce.sensors.Sensor;

import com.pi4j.wiringpi.I2C;

import org.mtforce.enocean.EnOceanPi;
import org.mtforce.enocean.OceanPacket;
import org.mtforce.enocean.OceanPacketReceivedEvent;
import org.mtforce.enocean.Response;
import org.mtforce.interfaces.CommunicationManager;
import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;
public class Main 
{


	public static void main(String[] args) 
	{
		try
		{
			EnOceanPi pi = new EnOceanPi();
			pi.init();
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
						// TODO Auto-generated method stub
						//System.out.println("Response Received: "+Utils.byteToHexString(response.getResponseCode()));
					}
				});
			}
			
			OceanPacket packet = new OceanPacket(new byte[]{(byte) 0x02});
			packet.setPacketType((byte)0x05);
			packet.generateHeader();
			packet.println();
			Response resp = pi.sendPacketForResponse(packet);
			//System.out.println("Waited for response: "+Utils.byteToHexString(resp.getResponseCode()));*/
			
			System.in.read();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
