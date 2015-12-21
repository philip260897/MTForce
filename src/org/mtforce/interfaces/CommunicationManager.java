package org.mtforce.interfaces;


public interface CommunicationManager 
{
    public void initialize() throws Exception; 
    
	public boolean write(byte address, byte reg, byte val);
	
	public boolean write(byte address,  byte val);
	
	public boolean write(byte address, byte reg, byte...val);
	
	public int read(byte address, byte reg);

	public byte[] read(byte address, byte reg, int bytes);
}
