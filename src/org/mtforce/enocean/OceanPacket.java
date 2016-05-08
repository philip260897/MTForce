package org.mtforce.enocean;

import org.mtforce.main.Utils;

public class OceanPacket 
{
	private byte headerSyncByte = 0x55;					//Synchronisationsbyte
	private byte[] headerDataLength = { 0x00, 0x00 };	//Datenlänge
	private byte headerOptionalLength = 0x00;			//Optionale Datenlänge
	private byte headerPacketType = 0x00;				//Packettype
	private byte headerCRC8 = 0x00;						//Header-CRC8
		
	private byte[] data;								//Daten
	private byte[] dataOptional;						//Optionale Daten
	private byte dataCRC8;								//Payload-CRC8
	
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
	
	/**
	 * Generiert den Header anhand der Payload
	 */
	public void generateHeader()
	{
		if(data != null)
			headerDataLength = Utils.toBytes(data.length, 2);
		if(dataOptional != null)
			headerOptionalLength = (byte)dataOptional.length;
		
		headerCRC8 = this.calculateHeaderCrc8();
		dataCRC8 = this.calculateDataCrc8();
	}
	
	/**
	 * Berechnet den CRC8-Wert für den Header
	 * @return	Gibt den CRC8-Wert zurück
	 */
    private byte calculateHeaderCrc8() {
        CRC8 crc8 = new CRC8();
        crc8.update(headerDataLength[1]);
        crc8.update(headerDataLength[0]);
        crc8.update(headerOptionalLength);
        crc8.update(headerPacketType);
        return (byte) crc8.getValue();
    }
    
    /**
     * Berechnet den CRC8-Wert für die Payload
     * @return	Gibt den CRC8-Wert zurück
     */
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
	
    /**
     * Setzt den PacketType
     * @param packetType	PacketType
     */
	public void setPacketType(byte packetType)
	{
		this.headerPacketType = packetType;
	}
	
	/**
	 * Gibt den PacketType zurück
	 * @return	PacketType
	 */
	public byte getPacketType()
	{
		return this.headerPacketType;
	}

	/**
	 * Gibt die Datenlänge zurück
	 * @return	Datenlänge
	 */
	public byte[] getHeaderDataLength() {
		return headerDataLength;
	}

	/**
	 * Gibt die optionale Datenlänge zurück
	 * @return	Optionale Datenlänge
	 */
	public byte getHeaderOptionalLength() {
		return headerOptionalLength;
	}

	/**
	 * Gibt den Header CRC8-Wert zurück
	 * @return	CRC8-Wert
	 */
	public byte getHeaderCRC8() {
		return headerCRC8;
	}

	/**
	 * Gibt die Paketdaten zurück
	 * @return Paketdaten
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Gibt die Optionalen Daten zurück
	 * @return	optionale Daten
	 */
	public byte[] getDataOptional() {
		return dataOptional;
	}

	/**
	 * Gibt den Payload CRC8-Wert zurück
	 * @return	CRC8-Wert
	 */
	public byte getDataCRC8() {
		return dataCRC8;
	}
	
	/**
	 * Bestimmt die Paketdaten
	 * @param data	Paketdaten
	 */
	public void setData(byte ...data) {
		this.data = data;
	}
	
	/**
	 * Bestimmt die optionale Daten
	 * @param dataOptional	Optionale Daten
	 */
	public void setDataOptional(byte ...dataOptional) {
		this.dataOptional = dataOptional;
	}
	
	/**
	 * Gibt zurück ob der CRC8 vom Header mit dem berechneten CRC8 übereinstimmt
	 * @return	Übereinstimmung des CRC8
	 */
	public boolean isHeaderValid() {
		return headerCRC8 == this.calculateHeaderCrc8();
	}
	
	/**
	 * Gibt zurück ob der CRC8 von der Payload mit dem berechneten CRC8 übereinstimmt
	 * @return	Übereinstimmung des CRC8
	 */
	public boolean isDataValid() {
		return dataCRC8 == this.calculateDataCrc8();
	}
	
	/**
	 * Baut ein OceanPacket aus einem rohem Datenpuffer
	 * @param bytes			Datenpuffer mit Paketdaten
	 * @throws Exception	Wenn kein valides Packet
	 */
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
	
	/**
	 * Schreibt die Rohdaten in ein Datenpuffer
	 * @return	Datenpuffer mit Rohdaten
	 */
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
	
	/**
	 * Schreibt das Packet in die Console in einer Zeile
	 */
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
	
	/**
	 * Gibt das Packet in die Console aus
	 */
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

	/**
	 * Gibt das Synchronisationsbyte zurück
	 * @return	Synchronisationsbyte
	 */
	protected byte getHeaderSyncByte() {
		return headerSyncByte;
	}

	/**
	 * Setzt das Synchronisationsbyte
	 * @param headerSyncByte	Synchronisationsbyte
	 */
	protected void setHeaderSyncByte(byte headerSyncByte) {
		this.headerSyncByte = headerSyncByte;
	}

	/**
	 * Gibt den PacketType zurück
	 * @return	PacketType
	 */
	protected byte getHeaderPacketType() {
		return headerPacketType;
	}

	/**
	 * Setzt den PacketType
	 * @param headerPacketType	PacketType
	 */
	protected void setHeaderPacketType(byte headerPacketType) {
		this.headerPacketType = headerPacketType;
	}

	/**
	 * Setzt Datenlänge
	 * @param headerDataLength	Datenlänge
	 */
	protected void setHeaderDataLength(byte[] headerDataLength) {
		this.headerDataLength = headerDataLength;
	}

	/**
	 * Setzt die optionale Datenlänge
	 */
	protected void setHeaderOptionalLength(byte headerOptionalLength) {
		this.headerOptionalLength = headerOptionalLength;
	}

	/**
	 * Setzt den CRC8-Wert vom Header
	 * @param headerCRC8 CRC-Wert
	 */
	protected void setHeaderCRC8(byte headerCRC8) {
		this.headerCRC8 = headerCRC8;
	}

	/**
	 * Setzt den CRC8-Wert von der Payload
	 * @param dataCRC8
	 */
	protected void setDataCRC8(byte dataCRC8) {
		this.dataCRC8 = dataCRC8;
	}
}
