package org.mtforce.enocean;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;

public class EnOceanPi
{
	private List<OceanPacketReceivedEvent> listeners = new ArrayList<OceanPacketReceivedEvent>();
	private Thread threadListener;
	private Serial serial;
	private PacketBuilder builder;
	private Response resp;
	
	private boolean enabled = false;
	
	public EnOceanPi()
	{
		builder = new PacketBuilder();
	}
	
	public void addOceanPacketReceivedEvent(OceanPacketReceivedEvent listener)
	{
		listeners.add(listener);
	}
	
	public void removeOceanPacketReceivedEvent(OceanPacketReceivedEvent listener)
	{
		listeners.remove(listener);
	}
	
	public void init()
	{
		if(enabled)
		{
			System.out.println("[EnOceanPi] Init error! Already initialized!");
			return;
		}
		
		serial = SerialFactory.createInstance();
		serial.open(Serial.DEFAULT_COM_PORT, 57600);
		if(serial.isOpen())
		{
			Runnable runnable = new Runnable(){
				@Override
				public void run() {
					while(true)
						readSerial();
				}
			};
			threadListener = new Thread(runnable);
			threadListener.start();
			System.out.println("[EnOceanPi] Initialized!");
			
			OceanPacket packet = new OceanPacket(new byte[]{(byte) 0x02});
			packet.setPacketType((byte)0x05);
			packet.generateHeader();
			Response response = this.sendPacketForResponse(packet);
			if(response.getResponseCode() == Response.RET_OK)
				enabled = true;
		}
		else
		{
			System.out.println("[EnOceanPi] Init error! Serial connection failed!");
		}
	}
	
	public void sendPacket(OceanPacket packet)
	{
		serial.write(packet.toBytes());
	}
	
	public Response sendPacketForResponse(OceanPacket packet)
	{
		serial.write(packet.toBytes());
		synchronized(this) {
			try {
				this.wait();
				return resp;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void close()
	{
		threadListener.stop();
		threadListener = null;
		serial.close();
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	private void readSerial()
	{
		int i = serial.read();
		builder.build((byte)i);
		//for(OceanPacketReceivedEvent event : listeners)
			//event.byteReceived((byte)i);
		if(builder.isPacketDone()) 
		{
			OceanPacket packet = builder.getPacket();
			if(packet.getPacketType() != 0x02)
				for(OceanPacketReceivedEvent event : listeners)
					event.packetReceived(packet);
			else
			{
				synchronized(this) {
					resp = new Response(packet);
					this.notifyAll();
				}
				for(OceanPacketReceivedEvent event : listeners)
					event.responseReceived(new Response(packet));
			}
		}
	}
}
