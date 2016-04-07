package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Logger;
import org.mtforce.main.Utils;
import org.mtforce.main.Logger.Status;

/**
 * Beschreibung: Thermometer
 * 
 * Konstanten: Komplett
 * Funktionen: Komplett
 * 
 * TODO: Modultest
 */
public class Thermometer extends Sensor 
{
	public static final byte kgsADDRESS		=		0x18;	//Baustein-Adresse
	
	public static final byte kgsREG_CONF		=	0x01;	//Konfigurations-Register
	public static final int kgsCONF_HYST_0 		= 	0x0000;	//Hysterese 0.0°C
	public static final int kgsCONF_HYST_15 	= 	0x0200;	//Hysterese 1.5°C
	public static final int kgsCONF_HYST_30 	= 	0x0400;	//Hysterese 3.0°C
	public static final int kgsCONF_HYST_60 	= 	0x0600;	//Hysterese 6.0°C
	public static final int kgsCONF_SHDN		=	0x0100;	//Shutdown Mode
	public static final int kgsCONF_CRIT_LOCK	=	0x0080;	//Kritische Temperaturgrenze sichern
	public static final int kgsCONF_WIN_LOCK	=	0x0040;	//Fenster sichern
	public static final int kgsCONF_INT_CLEAR	=	0x0020;	//Alert-Interrupt
	public static final int kgsCONF_ALERT_STAT	=	0x0010; //Alert-Status-Bit
	public static final int kgsCONF_ALERT_CNT	= 	0x0008;	//Alert-Output Aktiv
	public static final int kgsCONF_ALERT_SEL	=	0x0004;	//Alert-Output Komparator Aktiv
	public static final int kgsCONF_ALERT_POL	=	0x0002;	//Alert-Polarität Aktiv-HIGH
	public static final int kgsCONF_ALERT_MOD	=	0x0001;	//Alert-Mode Interrupt
	public static final int kgsCONF_DEFAULT		=	0x0000;	//Standardkonfiguration
	
	public static final byte kgsREG_RES		=	0x08;	//Umwandlungsauflösungs-Register
	public static final byte kgsRES_5		=	0x00;	//Auflösung 0.5°C
	public static final byte kgsRES_25		=	0x01;	//Auflösung 0.25°C
	public static final byte kgsRES_125		=	0x02;	//Auflösung 0.125°C
	public static final byte kgsRES_0625	=	0x03;	//Auflösung 0.0625°C
	
	public static final byte kgsREG_TUPPER		=	0x02;	//Temperatur-Obergrenze-Register
	public static final byte kgsREG_TLOWER		=	0x03;	//Temperatur-Untergrenze-Register
	public static final byte kgsREG_TCRIT		=	0x04;	//Kritische Temperaturgrenze-Register
	public static final byte kgsREG_TA			=	0x05;	//Gemessene Temperatur-Register
	public static final byte kgsREG_MANUF_ID	=	0x06;	//Hersteller-ID-Register
	public static final byte kgsREG_DEV_ID		=	0x07;	//Baustein-ID-Register
	
	private int gDefaultConfiguration 			=	kgsCONF_DEFAULT;
	
	private I2CManager i2c;
	
	protected Thermometer() {}
	
	/**
	 * Schreibt einen Testwert in das Konfigurationsregister und aktiviert nach Erfolg den Sensor
	 */
	@Override
	public void init()
	{
		i2c = (I2CManager)Sensors.getI2C();
		
		if(i2c.write(kgsADDRESS, kgsREG_CONF, Utils.toBytes(gDefaultConfiguration, 2), true)) 
		{
			setEnabled(true);
		} 
		else 
		{
			Logger.log(Status.ERROR, this.getClass().getSimpleName(), "init failed! Device not functional");
		} 
	}
	
	/**
	 * Setzt die Standard-Konfiguration
	 * WICHTIG: Nur vor dem Initialisieren aufrufen! 
	 * @param defaultConfiguration	Konstante Konfiguration
	 */
	public void setDefaultConfiguration(int defaultConfiguration)
	{
		this.gDefaultConfiguration = defaultConfiguration;
	}

	//=====Configuration===== IMPLEMENTED
	
	/**
	 * Setzt die gewünschte Konfiguration
	 * @param configuration		gewünschte Konstanten verODERn
	 */
	public void setConfiguration(int configuration)
	{
		i2c.write(kgsADDRESS, kgsREG_CONF, Utils.toBytes(configuration, 2), true);
	}
	
	/**
	 * Führt read aus und schreibt das Eingelesene in einen Integer
	 * @return	gelesener Integer-Wert
	 */
	public int getConfiguration()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		byte b = packet[0];
		packet[0] = packet[1];
		packet[1] = b;
		return Utils.toInt(packet);
	}
	
	/**
	 * Liest die Hysteresen-Konfiguration aus
	 * @return 	Hysteresen-Konfiguration Integer
	 */
	public int getHysteresis()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		packet[0] = 0; packet[1] = Utils.isolateBits(packet[1], 1, 2);
		return Utils.toInt(packet);
	}
	
	/**
	 * Gibt zurück ob das Shutdown-Bit gesetzt ist
	 * @return	boolean ob gesetzt
	 */
	public boolean isShutdownSet()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		return Utils.isBitSet(packet[1], 0);
	}
	
	/**
	 * Gibt zurück ob CriticalLock-Bit gesetzt ist
	 * @return boolean ob gesetzt
	 */
	public boolean isCriticalLockSet()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		return Utils.isBitSet(packet[0], 7);
	}
	
	/**
	 * Gibt zurück ob Window-Bit gesetzt ist
	 * @return boolean ob gesetzt
	 */
	public boolean isWindowLockSet()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		return Utils.isBitSet(packet[0], 6);
	}
	
	/**
	 * Gibt zurück ob InterruptClear-Bit gesetzt ist
	 * @return boolean ob gesetzt
	 */
	public boolean isInterruptClearSet()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		return Utils.isBitSet(packet[0], 5);
	}
	
	/**
	 * Gibt zurück ob AlertOutputStatus-Bit gesetzt ist
	 * @return boolean ob gesetzt
	 */
	public boolean isAlertOutputStatusSet()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		return Utils.isBitSet(packet[0], 4);
	}
	
	/**
	 * Gibt zurück ob AlertOutputControl-Bit gesetzt ist
	 * @return boolean ob gesetzt
	 */
	public boolean isAlertOutputControlSet()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		return Utils.isBitSet(packet[0], 3);
	}
	
	/**
	 * Gibt zurück ob AlertOutputSelectSet-Bit gesetzt ist
	 * @return boolean ob gesetzt
	 */
	public boolean isAlertOutputSelectSet()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		return Utils.isBitSet(packet[0], 2);
	}
	
	/**
	 * Gibt zurück ob AlertOutputPolaritySet-Bit gesetzt ist
	 * @return boolean ob gesetzt
	 */
	public boolean isAlertOutputPolaritySet()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		return Utils.isBitSet(packet[0], 1);
	}
	
	/**
	 * Gibt zurück ob AlertOutputModeSet-Bit gesetzt ist
	 * @return boolean ob gesetzt
	 */
	public boolean isAlertOutputModeSet()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_CONF, 2, true);
		return Utils.isBitSet(packet[0], 0);
	}
	
	//=====Temperature Limits=====IMPLEMENTED
	
	/**
	 * Schreibt das gewünschte Upper-Limit ins Register
	 * @param limit		Upper Limit in Grad Celsius
	 */
	public void setTUpperLimit(double limit)
	{
		i2c.write(kgsADDRESS, kgsREG_TUPPER, formatWriteLimits(limit), true);
	}
	
	/**
	 * Liest Upper-Limit aus Register aus
	 * @param return	Upper-Limit in Grad Celsius
	 */
	public double getTUpperLimit()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_TUPPER, 2, true);
		return formatReadLimits(packet);
	}
	
	/**
	 * Schreibt das gewünschte Lower-Limit ins Register
	 * @param limit		Lower-Limit in Grad Celsius
	 */
	public void setTLowerLimit(double limit)
	{
		i2c.write(kgsADDRESS, kgsREG_TLOWER, formatWriteLimits(limit), true);
	}
	
	/**
	 * Liest Lower-Limit aus Register aus
	 * @param return	Lower-Limit in Grad Celsius
	 */
	public double getTLowerLimit()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_TLOWER, 2, true);
		return formatReadLimits(packet);
	}
	
	/**
	 * Schreibt die gewünschte Critical-Temperature ins Register
	 * @param limit		Critical-Temperature in Grad Celsius
	 */
	public void setTCritical(double limit)
	{
		i2c.write(kgsADDRESS, kgsREG_TCRIT, formatWriteLimits(limit), true);
	}
	
	/**
	 * Liest Critical-Temperature aus Register aus
	 * @param return	Critical-Temperature in Grad Celsius
	 */
	public double getTCriticalLimit()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_TCRIT, 2, true);
		return formatReadLimits(packet);
	}
	
	//=====Ta======IMPLEMENTED
	
	/**
	 * Gibt zurück ob Temperature-Critical-Bit gesetzt ist
	 * COMMENT: True wenn Temperatur das gewünschte Critical Limit überschreitet
	 * @return boolean ob gesetzt
	 */
	public boolean isTemperatureCritical()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_TA, 2, true);
		return Utils.isBitSet(packet[1], 7);
	}
	
	/**
	 * Gibt zurück ob TemperatureUpper-Bit gesetzt ist
	 * COMMENT: True wenn Temperatur das gewünschte Upper Limit überschreitet
	 * @return boolean ob gesetzt
	 */
	public boolean isTemperatureUpper()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_TA, 2, true);
		return Utils.isBitSet(packet[1], 6);
	}
	
	/**
	 * Gibt zurück ob TemperatureLower-Bit gesetzt ist
	 * COMMENT: True wenn Temperatur das gewünschte Lower Limit überschreitet
	 * @return boolean ob gesetzt
	 */
	public boolean isTemperatureLower()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_TA, 2, true);
		return Utils.isBitSet(packet[1], 5);
	}
	
	/**
	 * Gibt die Temperatur zurück
	 * @return Temperatur
	 */
	public double getTemperature()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_TA, 2, true);
		return formatReadLimits(packet);
	}
	
	//====Manufacturer ID============IMPLEMENTED
	
	/**
	 * Gibt die Manufacturer ID zurück
	 * @return Manufacturer ID
	 */
	public int getManufacturerID()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_MANUF_ID, 2, true);
		return Utils.toInt(packet);
	}
	
	//====Device ID and Revision======IMPLEMENTED
	
	/**
	 * Gibt die Device ID zurück
	 * @return Device ID
	 */
	public int getDeviceID()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_DEV_ID, 2, true);
		return (int)packet[1] & 0xFF;
	}
	
	/**
	 * Gibt die Devision zurück
	 * @return Devision
	 */
	public int getRevision()
	{
		byte[] packet = i2c.read(kgsADDRESS, kgsREG_DEV_ID, 2, true);
		return (int)packet[0] & 0xFF;
	}
	
	//======RESOLUTIOn=====
	
	/**
	 * Gewünschte Auflösung einstellen
	 * @param 	gewünschte Auflösung
	 */
	public void setResolution(byte resolution)
	{
		i2c.write(kgsADDRESS, kgsREG_RES, resolution);
	}
	
	/**
	 * Gibt die Auflösung zurück
	 * @return resolution
	 */
	public byte getResolution()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_RES);
		return packet;
	}
	
	//=====UTILS=====
	
	/**
	 * Konvertiert einen Double Wert in Q-Notation
	 * @param limit	Parameter welcher Wert konvertiert werden soll
	 * @return		Konvertiertes Ergebnis in Q-Notation
	 */
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
	
	/**
	 * Q-Notation zu double Wert
	 * @param packet	Parameter welcher Wert konvertiert werden soll
	 * @return d		Konvertiertes Ergebnis
	 */
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
}
