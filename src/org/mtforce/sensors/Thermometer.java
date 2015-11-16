package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Utils;

public class Thermometer extends Sensor 
{
	public static final byte ADDRESS		=	0x00;
	
	public static final byte REG_CONF		=	0x00;	//CONFIG REGISTER ADDRESS
	public static final int CONF_HYST_0 	= 	0x0000;	//Hysteresis 0.0°C
	public static final int CONF_HYST_15 	= 	0x0200;	//Hysteresis 1.5°C
	public static final int CONF_HYST_30 	= 	0x0400;	//Hysteresis 3.0°C
	public static final int CONF_HYST_60 	= 	0x0600;	//Hysteresis 6.0°C
	public static final int CONF_SHDN		=	0x0100;	//SHUTDOWN MODE 1, 0 NORMAL MODE
	public static final int CONF_CRIT_LOCK	=	0x0080;	//CRITICAL LOCK 1
	public static final int CONF_WIN_LOCK	=	0x0040;	//WINDOW LOCK	1
	public static final int CONF_INT_CLEAR	=	0x0020;	//ALERT INTERRUPT
	public static final int CONF_ALERT_STAT	=	0x0010; //ALERT STATUS BIT
	public static final int CONF_ALERT_CNT	= 	0x0008;	//ALERT OUTPUT 1 ACTIVE, 0 INACTIVE
	public static final int CONF_ALERT_SEL	=	0x0004;	//ALERT OUTPUT 1 COMPARATOR OUTPUT (ALERT MODE IGNORED)
	public static final int CONF_ALERT_POL	=	0x0002;	//ALERT POLARITY  1 ACTIVE HIGH, 0 ACTIVE LOW
	public static final int CONF_ALERT_MOD	=	0x0001;	//ALERT MODE 1 INTERRUPT,0 COMPARATOR
	public static final int CONF_DEFAULT	=	0x0000;	//DEFAULT CONFIGURATION
	
	public static final byte REG_RES		=	0x08;	//RESOLUTION REGISTER ADDRESS
	public static final byte RES_5			=	0x00;	//RESOLUTION 0.5°C		30ms
	public static final byte RES_25			=	0x01;	//RESOLUTION 0.25°C		65ms
	public static final byte RES_125		=	0x02;	//RESOLUTION 0.125°C	130ms
	public static final byte RES_0625		=	0x03;	//RESOLUTION 0.0625°C	250ms
	
	public static final byte REG_TUPPER		=	0x02;	//SET T_UPPER LIMIT		(11-2)bit TEMP Value / 12bit Sign
	public static final byte REG_TLOWER		=	0x03;	//SET T_LOWER LIMIT		(11-2)bit TEMP Value / 12bit Sign
	public static final byte REG_TCRIT		=	0x04;	//SET T_CRITICAL LIMIT	(11-2)bit TEMP Value / 12bit Sign
	public static final byte REG_TA			=	0x05;	//GET TEMPERATURE		(11-0)bit TEMP Value / 15bit Ta vs Tcrit / 14bit Ta vs Tupper / 13bit Ta vs Tlower
	public static final byte REG_MANUF_ID	=	0x06;	//MANUFACTURER ID		(16bit)
	public static final byte REG_DEV_ID		=	0x07;	//DEVICE ID				(15-8)bit DEVICE ID / (7-0)bit DEVICE REVISION
	
	private double temperature = 0;
	
	@Override
	public void init() {
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
	public void update() {
		super.update();
		
		byte[] data = I2CManager.read(ADDRESS, REG_TA, 2);
		
	}

	@Override
	public void dispose() {
		super.dispose();
		
	}
	
	private boolean checkRegister(byte reg, int value, int bcount)
	{
		byte[] txPacket = Utils.toBytes(value, bcount);
		I2CManager.write(ADDRESS, reg, txPacket);
		
		byte[] rxPacket = I2CManager.read(ADDRESS, reg, bcount);
		
		return Utils.compareBytes(txPacket, rxPacket);
	}
}
