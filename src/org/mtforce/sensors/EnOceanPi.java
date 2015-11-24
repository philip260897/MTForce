package org.mtforce.sensors;

public class EnOceanPi extends Sensor {

	public static final byte RESERVED_1 		= 0x00; //RESERVED
	public static final byte RADIO 				= 0x01; //Radio telegram
	public static final byte RESPONSE 			= 0x02; //Response to any packet
	public static final byte RADIO_SUB_TEL 		= 0x03; //Radio subtelegram
	public static final byte EVENT 				= 0x04; //Event message
	public static final byte COMMON_COMMAND 	= 0x05; //Common command
	public static final byte SMART_ACK_COMMAND 	= 0x06; //Smart Ack command
	public static final byte REMOTE_MAN_COMMAND = 0x07; //Remote management command
	public static final byte RESERVED_2 		= 0x08; //Reserved for EnOcean
	public static final byte RADIO_MESSAGE 		= 0x09; //Radio message
	public static final byte RADIO_ADVANCED 	= 0x0A; //Advanced protocol radio telegram

	
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
