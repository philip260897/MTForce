package org.mtforce.enocean;

public class Response
{
	public static byte RET_OK = 0x00;				//Rückgabewert OK
	public static byte RET_ERROR = 0x01;			//Rückgabewert ERROR
	public static byte RET_NOT_SUPPORTED = 0x02;	//Rückgabewert NOT_SUPPORTED
	public static byte RET_WRONG_PARAM = 0x03;		//Rückgabewert WRONG_PARAM
	public static byte RET_OPERATION_DENIED = 0x04;	//Rückgabewert OPERATION_DENIED
	
	private OceanPacket packet;
	
	public Response(OceanPacket packet)
	{
		this.packet = packet;
	}
	
	/**
	 * Gibt den Rückgabewert zurück
	 * @return	Rückgabewert
	 */
	public byte getResponseCode()
	{
		return packet.getData()[0];
	}
	
	/**
	 * Gibt die Rückgabedaten zurück
	 * @return	Rückgabedaten
	 */
	public byte[] getResponseData()
	{
		byte[] b = new byte[packet.getData().length-1];
		for(int i = 0; i < b.length; i++)
			b[i] = packet.getData()[i+1];
		return b;
	}
	
	/**
	 * Gibt das OceanPacket zurück
	 * @return	OceanPacket
	 */
	public OceanPacket getPacket() {
		return packet;
	}
}
