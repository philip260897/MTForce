package org.mtforce.sensors;

public class Thermometer extends Sensor 
{
	public static final int REG_CONF		=	0x00;
	public static final int CONF_HYST_0 	= 	0x0000;
	public static final int CONF_HYST_15 	= 	0x0200;
	public static final int CONF_HYST_30 	= 	0x0400;
	public static final int CONF_HYST_60 	= 	0x0600;
	
	public static final int CONF_SHDN		=	0x0100;
	public static final int CONF_CRIT_LOCK	=	0x0080;
	public static final int CONF_WIN_LOCK	=	0x0040;
	public static final int CONF_INT_CLEAR	=	0x0020;
	public static final int CONF_ALERT_STAT	=	0x0010;
	public static final int CONF_ALERT_CNT	= 	0x0008;
	public static final int CONF_ALERT_SEL	=	0x0004;
	public static final int CONF_ALERT_POL	=	0x0002;
	public static final int CONF_ALERT_MOD	=	0x0001;
	
	
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
