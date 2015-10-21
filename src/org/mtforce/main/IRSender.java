package org.mtforce.main;


public class IRSender {
	private int start;
	private double data;
	public double setOn() {
		data = GPIOManager.write();
		return data;
	}
}
