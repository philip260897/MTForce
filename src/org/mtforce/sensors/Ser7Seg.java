package org.mtforce.sensors;

import org.mtforce.interfaces.SPIManager;

public class Ser7Seg implements Sensor
{
	int maxInUse = 1; 
	// define max7219 registers
	byte max7219_reg_noop        = 0x00;
	byte max7219_reg_digit0      = 0x01;
	byte max7219_reg_digit1      = 0x02;
	byte max7219_reg_digit2      = 0x03;
	byte max7219_reg_digit3      = 0x04;
	byte max7219_reg_digit4      = 0x05;
	byte max7219_reg_digit5      = 0x06;
	byte max7219_reg_digit6      = 0x07;
	byte max7219_reg_digit7      = 0x08;
	byte max7219_reg_decodeMode  = 0x09;
	byte max7219_reg_intensity   = 0x0a;
	byte max7219_reg_scanLimit   = 0x0b;
	byte max7219_reg_shutdown    = 0x0c;
	byte max7219_reg_displayTest = 0x0f;

	byte digits[] = {0,0,0,0,0 ,0,0,0};
	
	private int number = 0;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		//initiation of the max 7219
		SPIManager.write(max7219_reg_scanLimit, 	(byte)0x07);      
		SPIManager.write(max7219_reg_decodeMode, 	(byte)0xff);  // using an digits
		SPIManager.write(max7219_reg_shutdown, 		(byte)0x01);    // not in shutdown mode
		SPIManager.write(max7219_reg_displayTest, 	(byte)0x00); // no display test
		
		for (int e=1; e<=8; e++) 
		{ 
			SPIManager.write((byte)e,(byte)0);
		}
		SPIManager.write(max7219_reg_intensity, (byte)((byte)0x01 & (byte)0x0f));    // the first 0x0f is the value you can set
	

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		number++;
		printNumber(number, 0);
	}



	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public int getNumber() {
		return number;
	}
	
	private void printNumber(int number, int offset)
	{
	   int uiValue = number;
	   int uiResult = 0;
	   int i = 0;
	   while (uiValue > 0) {
	      uiResult <<= 4;
	      uiResult |= uiValue % 10;
	      if(i == 0) digits[offset+0] = (byte) (uiValue % 10);
	      if(i == 1) digits[offset+1] = (byte) (uiValue % 10);
	      if(i == 2) digits[offset+2] = (byte) (uiValue % 10);
	      if(i == 3) digits[offset+3] = (byte) (uiValue % 10);
	      uiValue /= 10;
	      i++;
	   }

	   SPIManager.write(max7219_reg_digit0, digits[0]);
	   SPIManager.write(max7219_reg_digit1, digits[1]);
	   SPIManager.write(max7219_reg_digit2, digits[2]);
	   SPIManager.write(max7219_reg_digit3, digits[3]);
	   SPIManager.write(max7219_reg_digit4, digits[4]);
	   SPIManager.write(max7219_reg_digit5, digits[5]);
	   SPIManager.write(max7219_reg_digit6, digits[6]);
	   SPIManager.write(max7219_reg_digit7, digits[7]);
	}
}
