package org.mtforce.main;

public class Utils {
	
	public static byte[] getByte(int integer){
		byte lastByte[] = new byte[4];
		for(int i = 0; i<4; i++)
			lastByte[i] = (byte) (integer >> 24-8*i);
		return lastByte;
	}
}
