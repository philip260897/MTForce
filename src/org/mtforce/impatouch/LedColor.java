package org.mtforce.impatouch;

public enum LedColor 
{
	RED(new byte[]{1,0,0}), 
	GREEN(new byte[]{0,1,0}), 
	BLUE(new byte[]{0,0,1}), 
	YELLOW(new byte[]{1,1,0}), 
	CYAN(new byte[]{0,1,1}), 
	MAGENTA(new byte[]{1,0,1}), 
	WHITE(new byte[]{1,1,1});
	
	private byte[] value; 
	private LedColor(byte[] value) { this.value = value; }
	
	public byte[] getValue() {
		return value;
	}
}
