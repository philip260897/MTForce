package org.mtforce.enocean;

import java.util.ArrayList;
import java.util.List;

import org.mtforce.main.Logger;
import org.mtforce.main.Logger.Status;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;

/**
 * WICHTIG fuers Protokoll: UART Port von console output freigeben -> googlen
 * 							I2C in rpi system aktivieren
 * 							SPI in rpi system aktivieren
 * 							ENOCEAN MODULE IN UNSECURE MODE BRINGEN!!!
 *
 */
public class EnOceanPi
{
	private static String PREFIX = "EnOceanPi";
	
	public static final byte PACKETTYPE_RESERVED_1 			= 0x00; //RESERVED
	public static final byte PACKETTYPE_RADIO 				= 0x01; //Radio telegram
	public static final byte PACKETTYPE_RESPONSE 			= 0x02; //Response to any packet
	public static final byte PACKETTYPE_RADIO_SUB_TEL 		= 0x03; //Radio subtelegram
	public static final byte PACKETTYPE_EVENT 				= 0x04; //Event message
	public static final byte PACKETTYPE_COMMON_COMMAND 		= 0x05; //Common command
	public static final byte PACKETTYPE_SMART_ACK_COMMAND 	= 0x06; //Smart Ack command
	public static final byte PACKETTYPE_REMOTE_MAN_COMMAND 	= 0x07; //Remote management command
	public static final byte PACKETTYPE_RESERVED_2 			= 0x08; //Reserved for EnOcean
	public static final byte PACKETTYPE_RADIO_MESSAGE 		= 0x09; //Radio message
	public static final byte PACKETTYPE_RADIO_ADVANCED 		= 0x0A; //Advanced protocol radio telegram
	
	private Serial serial;
	private boolean enabled = false;
	
	private List<OceanPacketReceivedEvent> listeners = new ArrayList<OceanPacketReceivedEvent>();
	private Thread threadListener;
	
	private PacketBuilder builder;
	private Response resp;
	
	
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
	
	public void init(String comPort, int baud)
	{
		if(enabled)
		{
			//System.out.println("[EnOceanPi] Init error! Already initialized!");
			Logger.log(Status.ERROR, PREFIX, "Init error! Already initialized!");
			return;
		}
		
		serial = SerialFactory.createInstance();
		serial.open(comPort, baud);
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
			
			
			OceanPacket packet = new OceanPacket(new byte[]{(byte) 0x02});
			packet.setPacketType((byte)0x05);
			packet.generateHeader();
			Response response = this.sendPacketForResponse(packet);
			if(response != null && response.getResponseCode() == Response.RET_OK) {
				enabled = true;
				Logger.log(PREFIX, "initialized!");
				return;
			} else {
				Logger.log(Status.ERROR, PREFIX, "init error! Device not functional");
			}
		}
		else
		{
			Logger.log(Status.ERROR, PREFIX, "Init error! Serial connection failed!");
		}
		builder.setEnabled(false);
		Logger.log(Status.WARNING, PREFIX, "Disabling PacketBuilder");
		close();
	}
	
	public void sendPacket(OceanPacket packet)
	{
		serial.write(packet.toBytes());
	}
	
	public Response sendPacketForResponse(OceanPacket packet) {
		return this.sendPacketForResponse(packet, 1000);
	}
	
	public Response sendPacketForResponse(OceanPacket packet, int timeout)
	{
		serial.write(packet.toBytes());
		synchronized(EnOceanPi.this) 
		{
			try 
			{
				this.wait(timeout);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		return resp;
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
			{
				for(OceanPacketReceivedEvent event : listeners) 
				{
					event.packetReceived(packet);
				}
			}
			else
			{
				synchronized(EnOceanPi.this) 
				{
					this.resp = new Response(packet);
					this.notifyAll();
				}
				for(OceanPacketReceivedEvent event : listeners)
				{
					event.responseReceived(new Response(packet));
				}
			}
		}
	}
}
