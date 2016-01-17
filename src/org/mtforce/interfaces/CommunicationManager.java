package org.mtforce.interfaces;


public interface CommunicationManager 
{
    public void initialize() throws Exception; 
    
	public boolean write8(byte address, byte reg, byte val);
	
	public boolean write8(byte address, byte val);
	//public boolean write(byte address,  byte val);
	
	public boolean write16(byte address, byte reg, byte[] val);
	
	public byte read8(byte address, byte reg);

	public byte[] read16(byte address, byte reg);
}
