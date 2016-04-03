package org.mtforce.enocean;

import org.mtforce.main.Logger;
import org.mtforce.main.Utils;
import org.mtforce.main.Logger.Status;

public class PacketBuilder 
{
	private String PREFIX = "PacketBuilder";					//Prefix fuer den Logger
	private boolean buildStart = false;							//Gibt an, ob ein neues Packet aufgebaut wird
	private boolean buildDone = false;							//Gibt an, ob ein Packet fertiggestellt wurde
	private int count = 0;										//Zaehlervariable
	private int countData = 0;									//Zaehlervariable fuer Packetdaten
	private int countOptional = 0;								//Zaehlervariable fuer optionale Daten
	private byte[] headerDataLength = new byte[] {0x00,0x00};	//Speichert die Packetdatenlaenge
	private byte[] data;										//Speichert Packetdaten
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
						//System.out.println("Data Length: "+Utils.toInt(headerDataLength));
						//System.out.println("Data Optional: "+(int)packet.getHeaderOptionalLength());
						//System.out.println("CRC8: "+Utils.byteToHexString(packet.getDataCRC8()) + " "+ Utils.byteToHexString(packet.calculateDataCrc8()));
						//System.out.println("[PacketBuilder] Building done!");
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
				//System.out.println("[PacketBuilder] Ignoring transmission: "+Utils.byteToHexString(b));
				Logger.log(Status.WARNING, PREFIX, "Ignoring transmission: "+Utils.byteToHexString(b));
			}
		}
	}
	
	/**
	 * Gibt das aufgebaute OceanPacket zurueck
	 * @return	OceanPacket
	 */
	public OceanPacket getPacket() {
		buildDone = false;
		return packet;
	}
	
	/**
	 * Gibt an, ob das Packet vollstaendig ist
	 * @return	Vollstaendigkeit des Packetes
	 */
	public boolean isPacketDone() {
		return buildDone;
	}
	
	/**
	 * Aktiviert oder Deaktiviert den PacketBuilder
	 * @param enabled	Aktviviert oder Deaktiviert den PacketBuilder
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
