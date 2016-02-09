package org.mtforce.main;

/**
 * Beschreibung: Diese Klasse beinhaltet haeufig verwendete Hilfsmethoden zur Bit und Byte manipulation
 * 
 * Konstanten: Komplett
 * Funktionen: Komplett (Erweiterbar)
 */
public class Utils 
{
	
	/**
	 * Ein bestimmtest Bit in einem Byte an einer Position setzten
	 * @param b			Das Byte welches manipuliert werden soll
	 * @param index		Welches bit manipuliert werden soll
	 * @return			Gibt das manipulierte Byte zurueck
	 */
	public static byte setBit(byte b, int index){
		byte mask = (byte) (0x01<<index);
		byte temp = (byte) (mask | b);
		return temp;
	}
	
	/**
	 * Wandelt einen Integer in ein Byte-Array um
	 * @param integer	Integer welcher umgewandelt werden soll
	 * @param var		Anzahl in wie viele bytes (bzw. wie viele bytes umgewandelt werden sollen)
	 * @return			Gibt das Byte-Array zurueck
	 */
	public static byte[] toBytes(int integer, int var){ //Wandelt eine Integer-Zahl in ein byte array mit integer-Anzahl an bytes um
		byte lastByte[] = new byte[var];
		for(int i = 0; i<var; i++){
			lastByte[var - 1 - i] = (byte) (integer >> 24-8*(i+var)); // shifts right every iteration and copies in byte array
		}return lastByte;
	}

	/**
	 * Wandelt ein Byte-Array in einen Integer um
	 * @param b	Byte-Array
	 * @return	Gibt den Integer-Wert zurueck
	 */
	public static int toInt(byte []b){ //Wandelt ein Byte-Array mit max. 4 bytes in einen+E127 Integer um
		int combInt = 0;
		for (int i=0; i<b.length; i++) {
			combInt <<= 8;				// shifts Int left
			combInt |= ((byte)b[b.length - 1 - i] & 0xFF);		// bitwise-OR with int (can be replaced with "+")
		}	
		return combInt;
	}
	
	/**
	 * Gibt nur Bits in einer bestimmten reichweite zurueck
	 * @param b		Byte aus dem bestimmte bits isoliert werden sollen
	 * @param start	Anfangswert der Reichweite
	 * @param stop	Endwert der Reichweite
	 * @return		Gibt die in der Reichweite definierten Bits zurueck in einem Byte
	 */
	public static byte isolateBits(byte b, int start, int stop){ //Retourniert ein Byte mit allen Bits 0 auser die Bits welche zwischen start und stop stehen
		byte mask = 0x00;										 // start must be smaller than stop, start => 0, stop <= 7
			for(int i = 0; i<=(stop-start); i++)
				mask = (byte) ((mask << 1)+1); // creates a mask with (stop-start) times 1s
			b = (byte) ((mask<<start) & b); // shifts 'start' times left and AND-Operator with b
			return b;
	}

	/**
	 * Ueberprueft ob ein bestimmtes Bit an einer bestimmten Stelle gesetzt ist
	 * @param b	Byte das ueberprueft werden soll
	 * @param i	Index des Bits das ueberprueft werden soll
	 * @return	Gibt True, wenn das bit==1 ist, oder False wenn das bit==0 ist zurueck
	 */
	public static boolean isBitSet(byte b, int i) //Retourniert True, wenn das bit an Index i 1 ist, ansonsten False
	{
		byte mask = (byte) (0x01<<i);
		return ((b & mask) != 0);
	}
	
	/**
	 * Vergleicht 2 Byte-Arrays auf exakte gleichheit
	 * @param a	Byte-Array A
	 * @param b	Byte-Array B
	 * @return	True wenn Array A und B exakt gleich sind, False wenn nicht
	 */
	public static boolean compareBytes(byte[] a, byte[] b) //Retourniert True, wenn Array und Array b ident sind, ansonsten False
	{
		if(a.length != b.length)
			return false;
		for(int i = 0; i < a.length; i++)
			if(a[i] != b[i])
				return false;
		return true;
	}
	
	/**
	 * Wandelt einen Dezimalwert in Q-Notation um
	 * @param number	Dezimalwert der Umgewandelt werden soll
	 * @param n			16-nQn (Anzahl der Kommastellen)
	 * @return			Gibt einen Integer mit der Q-Notation zurueck
	 */
	public static int doubleToQNotation(double number, int n)
	{
		return Math.round((float)number * (float)Math.pow(2, n));
	}
	
	/**
	 * Wandelt Q-Notation in einen Dezimalwert um
	 * @param number	Q-Notation Zahl
	 * @param n			16-nQn (Anzahl der Kommastellen)
	 * @return			Gibt den Dezimalwert in double zurueck
	 */
	public static double qNotationToDouble(int number, int n)
	{
		return (double)number  * Math.pow(2, -n);
	}
	
	/**
	 * Kehrt die reihnfolge des Byte-Arrays um
	 * @param array	Byte-Array welches umgedreht werden soll
	 * @return		Gibt das verkehrte Byte-Array zurueck
	 */
	public static byte[] reverseBytes(byte[] array)
	{
		byte[] n = new byte[array.length];
		for(int i = 0; i < n.length; i++)
			n[n.length-1-i] = array[i];
		return n;
	}
	
	/**
	 * Wandelt ein Byte in einen Hex-String um
	 * @param b	Byte welches als String dargestellt werden soll
	 * @return	Gibt den Hex-String zurueck
	 */
	public static String byteToHexString(byte b)
	{
		return String.format("%02x", b & 0xff);
	}
}
