package org.mtforce.enocean;

public class Response
{
	public static byte RET_OK = 0x00;
	public static byte RET_ERROR = 0x01;
	public static byte RET_NOT_SUPPORTED = 0x02;
	public static byte RET_WRONG_PARAM = 0x03;
	public static byte RET_OPERATION_DENIED = 0x04;
	
	private OceanPacket packet;
	
	public Response(OceanPacket packet)
	{
		this.packet = packet;
	}
	
	public byte getResponseCode()
	{
		return packet.getData()[0];
	}
	
	public byte[] getResponseData()
	{
		byte[] b = new byte[packet.getData().length-1];
		for(int i = 0; i < b.length; i++)
			b[i] = packet.getData()[i+1];
		return b;
	}
	
	public OceanPacket getPacket() {
		return packet;
	}
}
