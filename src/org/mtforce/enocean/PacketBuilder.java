package org.mtforce.enocean;

import org.mtforce.main.Utils;

public class PacketBuilder 
{
	private boolean buildStart = false;
	private boolean buildDone = false;
	private int count = 0;
	private int countData = 0;
	private int countOptional = 0;
	private byte[] headerDataLength = new byte[] {0x00,0x00};
	private byte[] data, dataOptional;
	
	public PacketBuilder(){}
	public OceanPacket packet = new OceanPacket();
	
	public void build(byte b)
	{
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
						System.out.println("[PacketBuilder] Building done!");
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
				System.out.println("[PacketBuilder] Ignoring transmission: "+Utils.byteToHexString(b));
			}
		}
	}
	
	public OceanPacket getPacket() {
		return packet;
	}
	
	public boolean isPacketDone() {
		return buildDone;
	}
}
