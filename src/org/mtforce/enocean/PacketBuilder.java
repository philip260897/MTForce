package org.mtforce.enocean;

import org.mtforce.main.Logger;
import org.mtforce.main.Utils;
import org.mtforce.main.Logger.Status;

public class PacketBuilder 
{
	private String PREFIX = "PacketBuilder";					//Prefix für den Logger
	private boolean buildStart = false;							//Gibt an, ob ein neues Packet aufgebaut wird
	private boolean buildDone = false;							//Gibt an, ob ein Packet fertiggestellt wurde
	private int count = 0;										//Zählervariable
	private int countData = 0;									//Zählervariable für Paketdaten
	private int countOptional = 0;								//Zählervariable für optionale Daten
	private byte[] headerDataLength = new byte[] {0x00,0x00};	//Speichert die Paketdatenlänge
	private byte[] data;										//Speichert Paketdaten
	private byte[] dataOptional;								//Speichert optionale Daten
	private boolean enabled = true;								//Aktiviert oder deaktiviert den PacketBuilder
	
	public PacketBuilder(){}
	public OceanPacket packet = new OceanPacket();				//OceanPacket welches aufgebaut wird
	
	/**
	 * Baut ein OceanPacket auf
	 * @param b	Packet-Byte
	 */
	public void build(byte b)
	{
		if(!enabled)
			return;
		if(buildStart)
		{
			switch(count)
			{
				case 0:	headerDataLength[1] = b; break;
				case 1: headerDataLength[0] = b; packet.setHeaderDataLength(headerDataLength); break;
				case 2: packet.setHeaderOptionalLength(b); break;
				case 3: packet.setHeaderPacketType(b); break;
				case 4: 
				{
					packet.setHeaderCRC8(b);
					if(!packet.isHeaderValid())
						System.out.println("[PacketBuilder] Header CRC8 is invalid!");
					data = new byte[Utils.toInt(headerDataLength)];
					dataOptional = new byte[(int)packet.getHeaderOptionalLength()];
					break;
				}
				default:
				{
					if(countData < data.length)
						data[countData++] = b;
					else if(countOptional < dataOptional.length)
						dataOptional[countOptional++] = b;
					else
					{
						packet.setData(data);
						packet.setDataOptional(dataOptional);
						packet.setDataCRC8(b);
						if(!packet.isDataValid())
							System.out.println("[PacketBuilder] Data CRC8 is invalid!");
						buildDone = true;
						buildStart = false;
						Logger.log(PREFIX, "Building done!");
					}
				}
			}
			
			count++;
		}
		else
		{
			if(b == 0x55) 
			{
				buildStart = true;
				buildDone = false;
				count = countData = countOptional = 0;
				packet = new OceanPacket();
				data = dataOptional = null;
				headerDataLength = new byte[]{0x00,0x00};
			}
			else
			{
				Logger.log(Status.WARNING, PREFIX, "Ignoring transmission: "+Utils.byteToHexString(b));
			}
		}
	}
	
	/**
	 * Gibt das aufgebaute OceanPacket zurück
	 * @return	OceanPacket
	 */
	public OceanPacket getPacket() {
		buildDone = false;
		return packet;
	}
	
	/**
	 * Gibt an, ob das Paket vollständig ist
	 * @return	Vollständigkeit des Paketes
	 */
	public boolean isPacketDone() {
		return buildDone;
	}
	
	/**
	 * Aktiviert oder deaktiviert den PacketBuilder
	 * @param enabled	Aktiviert oder deaktiviert den PacketBuilder
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * Gibt an ob der PacketBuilder aktiv ist oder nicht
	 * @return	PacketBuilder aktiv oder nicht
	 */
	public boolean isEnabled() {
		return enabled;
	}
}
