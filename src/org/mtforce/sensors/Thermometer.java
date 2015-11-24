package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Utils;

public class Thermometer extends Sensor 
{
	public static final byte ADDRESS		=	0x00;	//Baustein-Adresse
	
	public static final byte REG_CONF		=	0x00;	//Konfigurations-Register
	public static final int CONF_HYST_0 	= 	0x0000;	//Hysterese 0.0°C
	public static final int CONF_HYST_15 	= 	0x0200;	//Hysterese 1.5°C
	public static final int CONF_HYST_30 	= 	0x0400;	//Hysterese 3.0°C
	public static final int CONF_HYST_60 	= 	0x0600;	//Hysterese 6.0°C
	public static final int CONF_SHDN		=	0x0100;	//Shutdown Mode
	public static final int CONF_CRIT_LOCK	=	0x0080;	//Kritische Temperaturgrenze sichern
	public static final int CONF_WIN_LOCK	=	0x0040;	//Fenster sichern
	public static final int CONF_INT_CLEAR	=	0x0020;	//Alert-Interrupt
	public static final int CONF_ALERT_STAT	=	0x0010; //Alert-Status-Bit
	public static final int CONF_ALERT_CNT	= 	0x0008;	//Alert-Output Aktiv
	public static final int CONF_ALERT_SEL	=	0x0004;	//Alert-Output Komparator Aktiv
	public static final int CONF_ALERT_POL	=	0x0002;	//Alert-Polaritaet Aktiv-HIGH
	public static final int CONF_ALERT_MOD	=	0x0001;	//Alert-Mode Interrupt
	public static final int CONF_DEFAULT	=	0x0000;	//Standardkonfiguration
	
	public static final byte REG_RES		=	0x08;	//Umwandlungsaufloesung-Register
	public static final byte RES_5			=	0x00;	//Aufloesung 0.5°C
	public static final byte RES_25			=	0x01;	//Aufloesung 0.25°C
	public static final byte RES_125		=	0x02;	//Aufloesung 0.125°C
	public static final byte RES_0625		=	0x03;	//Aufloesung 0.0625°C
	
	public static final byte REG_TUPPER		=	0x02;	//Temperatur-Obergrenze-Register
	public static final byte REG_TLOWER		=	0x03;	//Temperatur-Untergrenze-Register
	public static final byte REG_TCRIT		=	0x04;	//Kritische Temperaturgrenze-Register
	public static final byte REG_TA			=	0x05;	//Gemessene Temperatur-Register
	public static final byte REG_MANUF_ID	=	0x06;	//Hersteller-ID-Register
	public static final byte REG_DEV_ID		=	0x07;	//Baustein-ID-Register
	
	private double temperature = 0;						//Ausgelesener Temperaturwert
	
	@Override
	public void init() //Schreibt einen Testwert in das Konfigurationsregister und aktivier nach Erfolg den Sensor
	{
		// TODO Auto-generated method stub
		if(I2CManager.write(ADDRESS, REG_CONF, Utils.toBytes(CONF_DEFAULT, 2))) 
		{
			setEnabled(true);
		} 
		else 
		{
			System.out.println(this.getClass().getSimpleName() + ": init error! Device not functional");
		} 
	}

	@Override
	public void update() //Liest Temperaturwert aus dem REG_TA-Register
	{
		super.update();
		
		byte[] data = I2CManager.read(ADDRESS, REG_TA, 2);
	}

	@Override
	public void dispose() //Nicht verwendet
	{
		super.dispose();
		
	}
	
	private boolean checkRegister(byte reg, int value, int bcount) //Schreibt den Wert value in das Register red und liest anschliesend den Wert aus dem Register reg um festzustellen ob der schreibvorgang erfolgreich war. bcount gibt an, wie viele byte von value ins Register geschrieben werden.
	{
		byte[] txPacket = Utils.toBytes(value, bcount);
		I2CManager.write(ADDRESS, reg, txPacket);
		
		byte[] rxPacket = I2CManager.read(ADDRESS, reg, bcount);
		
		return Utils.compareBytes(txPacket, rxPacket);
	}
}
