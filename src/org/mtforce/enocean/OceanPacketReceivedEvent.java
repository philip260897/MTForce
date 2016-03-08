package org.mtforce.enocean;

public interface OceanPacketReceivedEvent 
{
	public void packetReceived(OceanPacket packet);
	
	public void responseReceived(Response response);
	
}
