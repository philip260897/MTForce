package org.mtforce.main;

public class Utils {
	public static byte[] toBytes(int integer, int var){ // returns bytearray
		byte lastByte[] = new byte[var];
		for(int i = 0; i<var; i++){
			lastByte[i] = (byte) (integer >> 24-8*(i+var)); // shifts right every iteration and copies in byte array
		}return lastByte;
	}
	
	public static int toInt(byte []b){ //returns integer from Bytearray (max 32bit)
		int combInt = 0;
		for (int i=0; i<b.length; i++) {
			combInt <<= 8;				// shifts Int left
			combInt |= (int)b[i];		// bitwise-OR with int (can be replaced with "+")
		  }	
		return combInt;
		}
}
