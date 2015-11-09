package org.mtforce.sensors;

public class Thermometer extends Sensor 
{
	public static final int REG_CONF		=	0x00;	//CONFIG REGISTER ADDRESS
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
	
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
