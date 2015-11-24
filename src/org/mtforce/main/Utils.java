package org.mtforce.main;

public class Utils {
	public static byte[] toBytes(int integer, int var){ //Wandelt eine Integer-Zahl in ein byte array mit integer-Anzahl an bytes um
		byte lastByte[] = new byte[var];
		for(int i = 0; i<var; i++){
			lastByte[i] = (byte) (integer >> 24-8*(i+var)); // shifts right every iteration and copies in byte array
		}return lastByte;
	}

	public static int toInt(byte []b){ //Wandelt ein Byte-Array mit max. 4 bytes in einen+E127 Integer um
		int combInt = 0;
		for (int i=0; i<b.length; i++) {
			combInt <<= 8;				// shifts Int left
			combInt |= (int)b[i];		// bitwise-OR with int (can be replaced with "+")
		}	
		return combInt;
	}
	public static byte isolateBits(byte b, int start, int stop){ //Retourniert ein Byte mit allen Bits 0 auser die Bits welche zwischen start und stop stehen
		byte mask = 0x00;										 // start must be smaller than stop, start => 0, stop <= 7
			for(int i = 0; i<=(stop-start); i++)
				mask = (byte) ((mask << 1)+1); // creates a mask with (stop-start) times 1s
			b = (byte) ((mask<<start) & b); // shifts 'start' times left and AND-Operator with b
			return b;
	}

	public static boolean isBitSet(byte b, int i) //Retourniert True, wenn das bit an Index i 1 ist, ansonsten False
	{
		byte mask = (byte) Math.pow(2, i);
		return ((b & mask) != 0);
	}
	
	public static boolean compareBytes(byte[] a, byte[] b) //Retourniert True, wenn Array und Array b ident sind, ansonsten False
	{
		if(a.length != b.length)
			return false;
		for(int i = 0; i < a.length; i++)
			if(a[i] != b[i])
				return false;
		return true;
	}
}
