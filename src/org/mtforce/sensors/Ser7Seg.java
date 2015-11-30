package org.mtforce.sensors;

import java.util.Calendar;

import org.mtforce.interfaces.SPIManager;

public class Ser7Seg extends Sensor
{
	//Common register declaration 
	private byte max7219_reg_noop        = 0x00;
	private byte max7219_reg_digit0      = 0x01;
	private byte max7219_reg_digit1      = 0x02;
	private byte max7219_reg_digit2      = 0x03;
	private byte max7219_reg_digit3      = 0x04;
	private byte max7219_reg_digit4      = 0x05;
	private byte max7219_reg_digit5      = 0x06;
	private byte max7219_reg_digit6      = 0x07;
	private byte max7219_reg_digit7      = 0x08;
	private byte max7219_reg_decodeMode  = 0x09;
	private byte max7219_reg_intensity   = 0x0a;
	private byte max7219_reg_scanLimit   = 0x0b;
	private byte max7219_reg_shutdown    = 0x0c;
	private byte max7219_reg_displayTest = 0x0f;

	private byte digits[] = {0,0,0,0,0 ,0,0,0};
	
	private int number = 0;
	private int intensity = 3;
	
	private Calendar cal = Calendar.getInstance();

	@Override
	public void init() {
		//initiation of the max 7219
		SPIManager.write(max7219_reg_scanLimit, 	(byte)0x07);      
		SPIManager.write(max7219_reg_decodeMode, 	(byte)0xff); 	// using an digits
		SPIManager.write(max7219_reg_shutdown, 		(byte)0x01);	// not in shutdown mode
		SPIManager.write(max7219_reg_displayTest, 	(byte)0x00);	// no display test
		
		//Set all digits to 0
		for (int e=1; e<=8; e++) 
		{ 
			SPIManager.write((byte)e,(byte)0);
		}
		//SPIManager.write(max7219_reg_intensity, (byte)((byte)0x01 & (byte)0x0f));    // the first 0x0f is the value you can set
		SPIManager.write(max7219_reg_intensity, (byte)intensity);
		setEnabled(true);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
		number++;
		
		//clearDigits();
		//cal = Calendar.getInstance();
		//printNumber(cal.getTime().getHours(), 6);
		//printNumber(cal.getTime().getMinutes(), 3);
		//printNumber(cal.getTime().getSeconds(), 0);
		printNumber(number, 0);
	}



	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();

	}

	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		if(!isEnabled())
			return;
		
		this.number = number;
		
		printNumber(number, 0);
	}
	
	public int getIntensity() {
		return intensity;
	}
	
	public void setIntensity(int intensity) {
		if(!isEnabled())
			return;
		
		this.intensity = intensity;
		
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

	   updateUnusedDigits();
	   
	   SPIManager.write(max7219_reg_digit0, digits[0]);
	   SPIManager.write(max7219_reg_digit1, digits[1]);
	   SPIManager.write(max7219_reg_digit2, digits[2]);
	   SPIManager.write(max7219_reg_digit3, digits[3]);
	   SPIManager.write(max7219_reg_digit4, digits[4]);
	   SPIManager.write(max7219_reg_digit5, digits[5]);
	   SPIManager.write(max7219_reg_digit6, digits[6]);
	   SPIManager.write(max7219_reg_digit7, digits[7]);
	   
	   
	}
	
	private void updateUnusedDigits()
	{
		int c = 7;
		for(int i = 7; i >= 0; i--)
		{
			if(digits[i] == 0) 
				c--;
			else
				i = -1;
			
		}
		SPIManager.write(this.max7219_reg_scanLimit, (byte)c);
		SPIManager.write(max7219_reg_intensity, (byte)intensity);
	}
	
	private void clearDigits() 
	{
		for(int i = 0; i < 8; i++)
			digits[i] = 0;
	}
}
