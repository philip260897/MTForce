package org.mtforce.sensors;

import org.mtforce.interfaces.I2CArduinoManager;
import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;

public class Thermometer extends Sensor 
{
	public static final byte kgsADDRESS		=		0x20;	//Baustein-Adresse
	
	public static final byte kgsREG_CONF		=	0x01;	//Konfigurations-Register
	public static final int kgsCONF_HYST_0 		= 	0x0000;	//Hysterese 0.0�C
	public static final int kgsCONF_HYST_15 	= 	0x0200;	//Hysterese 1.5�C
	public static final int kgsCONF_HYST_30 	= 	0x0400;	//Hysterese 3.0�C
	public static final int kgsCONF_HYST_60 	= 	0x0600;	//Hysterese 6.0�C
	public static final int kgsCONF_SHDN		=	0x0100;	//Shutdown Mode
	public static final int kgsCONF_CRIT_LOCK	=	0x0080;	//Kritische Temperaturgrenze sichern
	public static final int kgsCONF_WIN_LOCK	=	0x0040;	//Fenster sichern
	public static final int kgsCONF_INT_CLEAR	=	0x0020;	//Alert-Interrupt
	public static final int kgsCONF_ALERT_STAT	=	0x0010; //Alert-Status-Bit
	public static final int kgsCONF_ALERT_CNT	= 	0x0008;	//Alert-Output Aktiv
	public static final int kgsCONF_ALERT_SEL	=	0x0004;	//Alert-Output Komparator Aktiv
	public static final int kgsCONF_ALERT_POL	=	0x0002;	//Alert-Polaritaet Aktiv-HIGH
	public static final int kgsCONF_ALERT_MOD	=	0x0001;	//Alert-Mode Interrupt
	public static final int kgsCONF_DEFAULT		=	0x0000;	//Standardkonfiguration
	
	public static final byte kgsREG_RES		=	0x08;	//Umwandlungsaufloesung-Register
	public static final byte kgsRES_5		=	0x00;	//Aufloesung 0.5�C
	public static final byte kgsRES_25		=	0x01;	//Aufloesung 0.25�C
	public static final byte kgsRES_125		=	0x02;	//Aufloesung 0.125�C
	public static final byte kgsRES_0625	=	0x03;	//Aufloesung 0.0625�C
	
	public static final byte kgsREG_TUPPER		=	0x02;	//Temperatur-Obergrenze-Register
	public static final byte kgsREG_TLOWER		=	0x03;	//Temperatur-Untergrenze-Register
	public static final byte kgsREG_TCRIT		=	0x04;	//Kritische Temperaturgrenze-Register
	public static final byte kgsREG_TA			=	0x05;	//Gemessene Temperatur-Register
	public static final byte kgsREG_MANUF_ID	=	0x06;	//Hersteller-ID-Register
	public static final byte kgsREG_DEV_ID		=	0x07;	//Baustein-ID-Register
	
	private int gDefaultConfiguration 			=	kgsCONF_DEFAULT;
	
	/**
	 * Schreibt einen Testwert in das Konfigurationsregister und aktiviert nach Erfolg den Sensor
	 */
	@Override
	public void init()
	{
		if(Sensors.getI2C().write(kgsADDRESS, kgsREG_CONF, Utils.toBytes(gDefaultConfiguration, 2))) 
		{
			setEnabled(true);
		} 
		else 
		{
			System.out.println(this.getClass().getSimpleName() + ": init error! Device not functional");
		} 
	}

	/**
	 * Liest Temperaturwert aus dem REG_TA-Register
	 */
	/*@Override
	public void update()
	{
		super.update();
		
		byte[] data = Sensors.getI2C().read(kgsADDRESS, kgsREG_TA, 2);
		gTemperature = Utils.toInt(data);
	}*/

	/**
	 * Nicht verwendet
	 */
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	public void setDefaultConfiguration(int defaultConfiguration)
	{
		this.gDefaultConfiguration = defaultConfiguration;
	}
	
	//=====Configuration===== NEEDS WORK! GETTERS FOR MODES
	
	public void setConfiguration(int configuration)
	{
		Sensors.getI2C().write(kgsADDRESS, kgsREG_CONF, Utils.toBytes(configuration, 2));
	}
	
	//=====Temperature Limits=====IMPLEMENTED
	
	public void setTUpperLimit(double limit)
	{
		Sensors.getI2C().write(kgsADDRESS, kgsREG_TUPPER, formatWriteLimits(limit));
	}
	
	public double getTUpperLimit()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_TUPPER, 2);
		return formatReadLimits(packet);
	}
	
	public void setTLowerLimit(double limit)
	{
		Sensors.getI2C().write(kgsADDRESS, kgsREG_TLOWER, formatWriteLimits(limit));
	}
	
	public double getTLowerLimit()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_TLOWER, 2);
		return formatReadLimits(packet);
	}
	
	public void setTCritical(double limit)
	{
		Sensors.getI2C().write(kgsADDRESS, kgsREG_TCRIT, formatWriteLimits(limit));
	}
	
	public double getTCriticalLimit()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_TCRIT, 2);
		return formatReadLimits(packet);
	}
	
	//=====Ta======IMPLEMENTED
	
	public boolean isTemperatureCritical()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_TA, 2);
		return Utils.isBitSet(packet[1], 7);
	}
	
	public boolean isTemperatureUpper()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_TA, 2);
		return Utils.isBitSet(packet[1], 6);
	}
	
	public boolean isTemperatureLower()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_TA, 2);
		return Utils.isBitSet(packet[1], 5);
	}
	
	public double getTemperature()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_TA, 2);
		return formatReadLimits(packet);
	}
	
	//====Manufacturer ID============IMPLEMENTED
	
	public int getManufacturerID()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_MANUF_ID, 2);
		return Utils.toInt(packet);
	}
	
	//====Device ID and Revision======IMPLEMENTED
	
	public int getDeviceID()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_DEV_ID, 2);
		return (int)packet[1] & 0xFF;
	}
	
	public int getRevision()
	{
		byte[] packet = Sensors.getI2C().read(kgsADDRESS, kgsREG_DEV_ID, 2);
		return (int)packet[0] & 0xFF;
	}
	
	//=====UTILS=====
	
	private byte[] formatWriteLimits(double limit)
	{
		boolean negativ = limit < 0 ? true : false;
		if(negativ)
			limit *= -1;
		int l = Utils.doubleToQNotation(limit, 2);
		l = l <<= 2;
		byte[] packet = Utils.toBytes(l, 2);
		if(negativ)
			packet[1] = Utils.setBit(packet[1], 4);
		return packet;
	}
	
	private double formatReadLimits(byte[] packet)
	{
		byte HighB = Utils.isolateBits(packet[1], 0, 3);
		int num = 0;
		num  |= HighB & 0xFF;
		num <<= 8;
		num |= packet[0] & 0xFF;
		double d = Utils.qNotationToDouble(num, 4);
		if(Utils.isBitSet(packet[1], 4))
			d *= -1;
		return d;
	}
	
	/**
	 * Schreibt den Wert value in das Register red und liest anschliesend den Wert aus dem Register reg um festzustellen ob der schreibvorgang erfolgreich war.
	 * bcount gibt an, wie viele byte von value ins Register geschrieben werden.
	 * @param iReg
	 * @param iValue
	 * @param iBcount
	 * @return
	 */
	private boolean checkRegister(byte iReg, int iValue, int iBcount)
	{
		byte[] txPacket = Utils.toBytes(iValue, iBcount);
		Sensors.getI2C().write(kgsADDRESS, iReg, txPacket);
		
		byte[] rxPacket = Sensors.getI2C().read(kgsADDRESS, iReg, iBcount);
		
		return Utils.compareBytes(txPacket, rxPacket);
	}
}
