package org.mtforce.enocean;

public class Response
{
	public static byte RET_OK = 0x00;				//Rueckgabewert OK
	public static byte RET_ERROR = 0x01;			//Rueckgabewert ERROR
	public static byte RET_NOT_SUPPORTED = 0x02;	//Rueckgabewert NOT_SUPPORTED
	public static byte RET_WRONG_PARAM = 0x03;		//Rueckgabewert WRONG_PARAM
	public static byte RET_OPERATION_DENIED = 0x04;	//Rueckgabewert OPERATION_DENIED
	
	private OceanPacket packet;
	
	public Response(OceanPacket packet)
	{
		this.packet = packet;
	}
	
	/**
	 * Gibt den Rueckgabewert zurueck
	 * @return	Rueckgabewert
	 */
	public byte getResponseCode()
	{
		return packet.getData()[0];
	}
	
	/**
	 * Gibt die Rueckgabedaten zurueck
	 * @return	Rueckgabedaten
	 */
	public byte[] getResponseData()
	{
		byte[] b = new byte[packet.getData().length-1];
		for(int i = 0; i < b.length; i++)
			b[i] = packet.getData()[i+1];
		return b;
	}
	
	/**
	 * Gibt das OceanPacket zurueck
	 * @return	OceanPacket
	 */
	public OceanPacket getPacket() {
		return packet;
	}
}
