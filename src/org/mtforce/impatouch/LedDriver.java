package org.mtforce.impatouch;

import java.awt.Point;

import org.mtforce.main.Utils;

public class LedDriver 
{
	//Decode-Modes
	public static final byte kgsDM_NO_DECODE	= 0x00;	//7-Segmentanzeigen werden nicht dekodiert
	public static final byte kgsDM_DECODE_0		= 0x01; //Code-B/HEX dekodieren nur fuer digit 0
	public static final byte kgsDM_DECODE_0_2	= 0x07; //Code-B/HEX dekodieren nur fuer 0-2
	public static final byte kgsDM_DECODE_0_5	= 0x3F; //Code-B/HEX dekodieren nur fuer 0-5
	public static final byte kgsDM_DECODE_0_2_5 = 0x25; //Code-B/HEX dekodieren nur fuer 0, 2, 5
	
	//Shutdown-Modes
	public static final byte kgsSHDM_SHD_RESET_FEAT		= 0x00;			//Shutdown Mode, Resetet Feature-Register auf Default-Werte
	public static final byte kgsSHDM_SHD_UC_FEAT		= (byte)0x80;	//Shutdown Mode, Feature-Register unveraendert
	public static final byte kgsSHDM_NORM_RESET_FEAT	= 0x01;			//Normal Mode, Resetet Feature-Register auf Default-Werte
	public static final byte kgsSHDM_NORM_UC_FEAT		= (byte)0x81;	//Normal Mode, Feature-Register unveraendert
	
	//Scan-Limit
	public static final byte kgsSCAN_LIMIT_0		=	0x00;
	public static final byte kgsSCAN_LIMIT_1		=	0x01;
	public static final byte kgsSCAN_LIMIT_2		=	0x02;
	public static final byte kgsSCAN_LIMIT_3		=	0x03;
	public static final byte kgsSCAN_LIMIT_4		=	0x04;
	public static final byte kgsSCAN_LIMIT_5		=	0x05;
	public static final byte kgsSCAN_LIMIT_6		=	0x06;
	public static final byte kgsSCAN_LIMIT_7		=	0x07;
	
	//Features
	public static final byte kgsFEAT_CLK_EN				=	0x01;	//USE SERIAL CLOCK
	public static final byte kgsFEAT_REG_RES			=	0x02;	//ALL CONTROL REGISTER RESETTED
	public static final byte kgsFEAT_DECODE_SEL			=	0x04;	//Enable HEX decoding, else Code-B decoding
	public static final byte kgsFEAT_BLINK_EN			=	0x10;	//Enable blinking
	public static final byte kgsFEAT_BLINK_FREQ_SEL		=	0x20;	//Slow Blinking, 2 seconds; else 1 second
	public static final byte kgsFEAT_SYNC				=	0x40;	//Synchronizes blinking on rising edge
	public static final byte kgsFEAT_BLINK_START		=	(byte) 0x80;	//Blinking starts with the display turned on
	
	//DisplayTest
	public static final byte kgsDISPTEST_DISP_TEST		=	0x01;	//Test all digits independently
	public static final byte kgsDISPTEST_LED_SHORT		=	0x02;	//Starts a test for shorted LEDS
	public static final byte kgsDISPTEST_LED_OPEN		=	0x04;	//Starts a test for open LEDS
	public static final byte kgsDISPTEST_LED_TEST		= 	0x08;	//Indicates an ongoing open/short LED test
	public static final byte kgsDISPTEST_LED_GLOBAL		=	0x10;	//Indicates that the last open/short LED test has detected an error
	public static final byte kgsDISPTEST_REXT_OPEN		=	0x20;	//Checks if external resistor REXT is open
	public static final byte kgsDISPTEST_REXT_SHORT		=	0x40;	//Checks if external resistor REXT is shorted
	
	//Registers
	public static final byte kgsNOOP 				= 0x00;	//No Operation
	public static final byte kgsDIGIT_0 			= 0x01; //Digit 0 Register
	public static final byte kgsDIGIT_1 			= 0x02; //Digit 1 Register
	public static final byte kgsDIGIT_2 			= 0x03; //Digit 2 Register
	public static final byte kgsDIGIT_3 			= 0x04; //Digit 3 Register
	public static final byte kgsDIGIT_4 			= 0x05; //Digit 4 Register
	public static final byte kgsDIGIT_5 			= 0x06; //Digit 5 Register
	public static final byte kgsDIGIT_6 			= 0x07; //Digit 6 Register
	public static final byte kgsDIGIT_7 			= 0x08; //Digit 7 Register
	
	public static final byte kgsDECODE_MODE 		= 0x09; //DecodeMode Register
	public static final byte kgsGLOBAL_INTENSITY 	= 0x0A; //Global Intensity Register
	public static final byte kgsSCAN_LIMIT 			= 0x0B; //Scan Limit Register
	public static final byte kgsSHUTDOWN 			= 0x0C; //Shutdown Register
	public static final byte kgsFEATURE 			= 0x0E; //Feature Register
	public static final byte kgsDISPLAY_TEST 		= 0x0F; //DisplayTest Register
	public static final byte kgs01INTENSITY 		= 0x10; //Digit 0:1 Intensity Register
	public static final byte kgs23INTENSITY 		= 0x11; //Digit 2:3 Intensity Register
	public static final byte kgs45INTENSITY 		= 0x12; //Digit 4:5 Intensity Register
	public static final byte kgs67INTENSITY 		= 0x13; //Digit 6:7 Intensity Register
	
	public static final byte kgsDIAGNOSTIC_DIGIT_0 	= 0x54; //Diagnostic digit 0 Register
	public static final byte kgsDIAGNOSTIC_DIGIT_1 	= 0x55; //Diagnostic digit 1 Register
	public static final byte kgsDIAGNOSTIC_DIGIT_2 	= 0x56; //Diagnostic digit 2 Register
	public static final byte kgsDIAGNOSTIC_DIGIT_3 	= 0x57; //Diagnostic digit 3 Register
	public static final byte kgsDIAGNOSTIC_DIGIT_4 	= 0x58; //Diagnostic digit 4 Register
	public static final byte kgsDIAGNOSTIC_DIGIT_5 	= 0x59; //Diagnostic digit 5 Register
	public static final byte kgsDIAGNOSTIC_DIGIT_6 	= 0x5A; //Diagnostic digit 6 Register
	public static final byte kgsDIAGNOSTIC_DIGIT_7 	= 0x5B; //Diagnostic digit 7 Register
	
	private byte rgbOrder[][] = new byte[][] {	{2, 1, 0}, 	{5, 4, 3}	};
											//0: R, G, B  1: R, G, B
	private byte valueOrder[] = new byte[] {0,1,2,3,4,5,6,7};
	
	public LedDriver()
	{
		
	}
	
	private byte[] generateBytesFromDigit(LedDigit digit)
	{
		byte[] values = new byte[8];
		for(Point p : digit.getPoints())
		{
			byte currB = values[valueOrder[(int)p.getY()]];
			currB = Utils.setBit(currB, rgbOrder[(int)p.getX()][0]);
			values[valueOrder[(int)p.getY()]] = currB;
		}
		return values;
	}
}
