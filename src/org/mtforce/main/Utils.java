package org.mtforce.main;

public class Utils {
	
	public static byte[] toBytes(int integer, int var){
		byte lastByte[] = new byte[var];
		for(int i = 0; i<var; i++){
			lastByte[i] = (byte) (integer >> 24-8*(i+var)); // shifts right every iteration
		}return lastByte;
	}
}
