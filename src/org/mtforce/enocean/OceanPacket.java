package org.mtforce.enocean;

import org.mtforce.main.Utils;

public class OceanPacket 
{
	private byte headerSyncByte = 0x55;
	private byte[] headerDataLength = { 0x00, 0x00 };
	private byte headerOptionalLength = 0x00;
	private byte headerPacketType = 0x00;
	private byte headerCRC8 = 0x00;
	
	private byte[] data;
	private byte[] dataOptional;
	private byte dataCRC8;
	
	public OceanPacket(byte data[], byte dataOptional[])
	{
		this.data = data;
		this.dataOptional = dataOptional;
	}
	
	public OceanPacket(byte data[])
	{
		this.data = data;
	}
	
	public OceanPacket()
	{
		
	}
	
	public void generateHeader()
	{
		if(data != null)
			headerDataLength = Utils.toBytes(data.length, 2);
		if(dataOptional != null)
			headerOptionalLength = (byte)dataOptional.length;
		
		headerCRC8 = this.calculateHeaderCrc8();
		dataCRC8 = this.calculateDataCrc8();
	}
	
    private byte calculateHeaderCrc8() {
        CRC8 crc8 = new CRC8();
        crc8.update(headerDataLength[1]);
        crc8.update(headerDataLength[0]);
        crc8.update(headerOptionalLength);
        crc8.update(headerPacketType);
        return (byte) crc8.getValue();
    }
    
    private byte calculateDataCrc8() {
        CRC8 crc8 = new CRC8();
        if (data != null) {
            crc8.update(data, 0, data.length);
        }
        if (dataOptional != null) {
            crc8.update(dataOptional, 0, dataOptional.length);
        }
        return (byte) crc8.getValue();
    }
	
	public void setPacketType(byte packetType)
	{
		this.headerPacketType = packetType;
	}
	
	public byte getPacketType()
	{
		return this.headerPacketType;
	}

	public byte[] getHeaderDataLength() {
		return headerDataLength;
	}

	public byte getHeaderOptionalLength() {
		return headerOptionalLength;
	}

	public byte getHeaderCRC8() {
		return headerCRC8;
	}

	public byte[] getData() {
		return data;
	}

	public byte[] getDataOptional() {
		return dataOptional;
	}

	public byte getDataCRC8() {
		return dataCRC8;
	}
	
	public void setData(byte ...data) {
		this.data = data;
	}
	
	public void setDataOptional(byte ...dataOptional) {
		this.dataOptional = dataOptional;
	}
	
	public boolean isHeaderValid() {
		return headerCRC8 == this.calculateHeaderCrc8();
	}
	
	public boolean isDataValid() {
		return dataCRC8 == this.calculateDataCrc8();
	}
	
	public void parsePacket(byte[] bytes) throws Exception
	{
		if(bytes[0] != 0x55)
			throw new Exception("Sync byte invalid!");
		
		headerDataLength[0] = bytes[2];
		headerDataLength[1] = bytes[1];
		headerOptionalLength = bytes[3];
		headerPacketType = bytes[4];
		headerCRC8 = bytes[5];
		if(Utils.toInt(headerDataLength) != 0)
			data = new byte[Utils.toInt(headerDataLength)];
		for(int i = 0; i < Utils.toInt(headerDataLength); i++)
			data[i] = bytes[i+6];
		if((int)headerOptionalLength != 0)
			dataOptional = new byte[(int)headerOptionalLength];
		for(int i = 0; i < (int)headerOptionalLength; i++)
			dataOptional[i] = bytes[data.length+6+i];
		int index = Utils.toInt(headerDataLength)+6+headerOptionalLength;
		dataCRC8 = bytes[index];
	}
	
	public byte[] toBytes()
	{
		int length = 7;
		if(data != null) length += data.length;
		if(dataOptional != null) length += dataOptional.length;
		byte[] bytes = new byte[length];
		bytes[0] = headerSyncByte;
		bytes[1] = headerDataLength[1];
		bytes[2] = headerDataLength[0];
		bytes[3] = headerOptionalLength;
		bytes[4] = headerPacketType;
		bytes[5] = headerCRC8;
		if(data != null)
			for(int i = 0; i < data.length; i++)
				bytes[i+6] = data[i];
		if(dataOptional != null)
			for(int i = 0; i < dataOptional.length; i++)
				bytes[data.length+6+i] = dataOptional[i];
		bytes[bytes.length-1] = dataCRC8;
		return bytes;
	}
	
	public void println()
	{
		System.out.print("Header("+ (isHeaderValid() ? "VALID" : "INVALID")+")["+Utils.byteToHexString(headerDataLength[1])+""+Utils.byteToHexString(headerDataLength[0]).replace("0x", ""));
		System.out.print(", "+ Utils.byteToHexString(headerOptionalLength));
		System.out.print(", "+ Utils.byteToHexString(headerPacketType));
		System.out.print(", "+ Utils.byteToHexString(headerCRC8) + "] Data[");
		for(byte b : data)
			System.out.print(Utils.byteToHexString(b)+", ");
		if(dataOptional != null) {
		System.out.print("] Optional Data[");
		for(byte b : dataOptional)
			System.out.print(Utils.byteToHexString(b)+", ");
		}
		System.out.println("] CRC8("+(isDataValid() ? "VALID" : "INVALID")+")"+"["+Utils.byteToHexString(dataCRC8)+"]");
	}
	
	public void print()
	{
		System.out.println("====HEADER====[" + (isHeaderValid() ? "VALID" : "INVALID")+"]");
		System.out.println("Sync: "+Utils.byteToHexString(headerSyncByte));
		System.out.println("Data Length: "+Utils.byteToHexString(headerDataLength[1])+""+Utils.byteToHexString(headerDataLength[0]).replace("0x", ""));
		System.out.println("Optional Length: "+Utils.byteToHexString(headerOptionalLength));
		System.out.println("PacketType: "+Utils.byteToHexString(headerPacketType));
		System.out.println("CRC8: "+Utils.byteToHexString(headerCRC8));
		System.out.println("====DATA====[" + (isDataValid() ? "VALID" : "INVALID")+"]");
		
		String data = "";
		for(byte b : getData())
		{
			data += Utils.byteToHexString(b) + " ";
		}
		data = data.substring(0, data.length()-1);
		System.out.println("Data: "+data);
		
		data = "";
		if(dataOptional != null)
		{
			for(byte b : getDataOptional())
			{
				data += Utils.byteToHexString(b) + " ";
			}
			data = data.substring(0, data.length()-1);
			System.out.println("DataOptional: "+data);
		}
		
		System.out.println("CRC8: "+ Utils.byteToHexString(dataCRC8));
		System.out.println("Calculated CRC8: "+Utils.byteToHexString(this.calculateDataCrc8()));
	}

	protected byte getHeaderSyncByte() {
		return headerSyncByte;
	}

	protected void setHeaderSyncByte(byte headerSyncByte) {
		this.headerSyncByte = headerSyncByte;
	}

	protected byte getHeaderPacketType() {
		return headerPacketType;
	}

	protected void setHeaderPacketType(byte headerPacketType) {
		this.headerPacketType = headerPacketType;
	}

	protected void setHeaderDataLength(byte[] headerDataLength) {
		this.headerDataLength = headerDataLength;
	}

	protected void setHeaderOptionalLength(byte headerOptionalLength) {
		this.headerOptionalLength = headerOptionalLength;
	}

	protected void setHeaderCRC8(byte headerCRC8) {
		this.headerCRC8 = headerCRC8;
	}

	protected void setDataCRC8(byte dataCRC8) {
		this.dataCRC8 = dataCRC8;
	}
}
