package org.mtforce.impatouch;

import java.awt.Point;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.interfaces.SPIManager;
import org.mtforce.main.Utils;
import org.mtforce.sensors.Sensors;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Beschreibung: Diese Klasse steuert die LED-Treiber auf dem Board. Aus den angegebenen Symbolen wird ein Bitstream generiert, welcher über SPI
 * 	in die Bausteine geschrieben wird.
 * 
 * Konstanten: Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: Alle Sensoren hinzufügen, getter und setter, Modultest
 */
public class LedDriver 
{
	//Decode-Modes
	public static final byte kgsDM_NO_DECODE	= 0x00;	//7-Segmentanzeigen werden nicht dekodiert
	public static final byte kgsDM_DECODE_0		= 0x01; //Code-B/HEX dekodieren nur für digit 0
	public static final byte kgsDM_DECODE_0_2	= 0x07; //Code-B/HEX dekodieren nur für 0-2
	public static final byte kgsDM_DECODE_0_5	= 0x3F; //Code-B/HEX dekodieren nur für 0-5
	public static final byte kgsDM_DECODE_0_2_5 = 0x25; //Code-B/HEX dekodieren nur für 0, 2, 5
	
	//Shutdown-Modes
	public static final byte kgsSHDM_SHD_RESET_FEAT		= 0x00;			//Shutdown Mode, Resettet Feature-Register auf Default-Werte
	public static final byte kgsSHDM_SHD_UC_FEAT		= (byte)0x80;	//Shutdown Mode, Feature-Register unverändert
	public static final byte kgsSHDM_NORM_RESET_FEAT	= 0x01;			//Normal Mode, Resettet Feature-Register auf Default-Werte
	public static final byte kgsSHDM_NORM_UC_FEAT		= (byte)0x81;	//Normal Mode, Feature-Register unverändert
	
	//Scan-Limit
	public static final byte kgsSCAN_LIMIT_0		=	0x00;	//Scan-Limit 0
	public static final byte kgsSCAN_LIMIT_1		=	0x01;	//Scan-Limit 1
	public static final byte kgsSCAN_LIMIT_2		=	0x02;	//Scan-Limit 2
	public static final byte kgsSCAN_LIMIT_3		=	0x03;	//Scan-Limit 3
	public static final byte kgsSCAN_LIMIT_4		=	0x04;	//Scan-Limit 4
	public static final byte kgsSCAN_LIMIT_5		=	0x05;	//Scan-Limit 5
	public static final byte kgsSCAN_LIMIT_6		=	0x06;	//Scan-Limit 6
	public static final byte kgsSCAN_LIMIT_7		=	0x07;	//Scan-Limit 7
	
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
	
	private byte rgbOrder[][] = new byte[][] {	{3, 2, 1}, 	{6, 5, 4}	};	//Register/Port Map
											//0: R, G, B  1: R, G, B
	private byte valueOrder[] = new byte[] {7,6,5,4,3,2,1,0};	//Digit/Port Map
	private int numberOfDevices = 4;							//Anzahl 
	private LedColor globalColor = LedColor.RED;				//Globale Farbe
	private SPIManager spi;										//Referenz auf SpiManager
	private GpioController gpio;								//Referenz auf GpioController
	private GpioPinDigitalOutput pin;							//LOAD Pin
	
	private static LedDriver instance;
	
	protected LedDriver()
	{
		instance = this;
	}
	
	
	public static LedDriver getInstance() {
		if(instance == null) new LedDriver();
		return instance;
	}
	
	/**
	 * Initialisiert den LEDTreiber. Referenz auf SpiManager, GpioController und Load Pin wird hergestellt.
	 * LedDictionary wird initialisiert.
	 */
	public void initialize()
	{
		LedDictionary.LoadDictionary();
		spi = (SPIManager)Sensors.getSPI();
		gpio = GpioFactory.getInstance();
		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "LD", PinState.HIGH);
		//TODO pin.setState(PinState.LOW);
		pin.setState(PinState.HIGH); //DEBUG
	}
	
	/**
	 * Lässt alle LEDs leuchten
	 * @param on	Gibt an ob LEDs leuchten sollen oder nicht
	 */
	public void setAllLedsOnAll(boolean on)
	{
		byte[] b = generateBytesFromDigit(on ? LedDictionary.getDigit("ALL") : LedDictionary.getDigit("NONE") );
		for(int i = 1; i < 9; i++)
			writeAll(b[i-1], (byte)i);
	}
	
	/**
	 * Lässt alle LEDs einer Anzeigen leuchten
	 * @param display	Index der Anzeige
	 * @param on		Gibt an ob LEDs leuchten sollen oder nicht
	 */
	public void setAllLedsOn(int display, boolean on)
	{
		byte[] b = generateBytesFromDigit(on ? LedDictionary.getDigit("ALL") : LedDictionary.getDigit("NONE") );
		for(int i = 1; i < 9; i++)
			write(display, b[i-1], (byte)i);
	}
	
	/**
	 * Schreibt einen Char auf die angegeben Anzeige
	 * @param display	Index der Anzeige
	 * @param c			Char welcher dargestellt werden soll
	 */
	public void writeChar(int display, char c)
	{
		LedDigit digit = LedDictionary.getDigit(Character.toString(c));
		byte[] data = this.generateBytesFromDigit(digit);
		for(int j = 1; j < 9; j++)
		{
			write(display, data[j-1], (byte)j);
		}
	}
	
	/**
	 * Schreibt die ersten 4 Charakter auf die 4 Anzeigen
	 * @param text	Text welcher angezeigt werden soll
	 */
	public void writeString(String text)
	{
		int length = 4;
		if(text.length() < length)
			length = text.length();
		for(int i = 0; i < length; i++)
		{
			writeChar(i, text.charAt(i));
		}
	}
	
	/**
	 * Schreibt Daten in alle AS1116-Chips
	 * @param data		Daten welche geschickt werden sollen
	 * @param address	Register Adresse in welche geschrieben werden sollen
	 */
	public void writeAll(byte data, byte address)
	{
		pin.setState(PinState.HIGH);
		for(int i = 0; i < numberOfDevices; i++)
			spi.write(Utils.reverseBitsByte(data), Utils.reverseBitsByte(address));
		pin.setState(PinState.LOW);
	}
	
	/**
	 * Schreibt Daten in den angegebenen AS1116-Chip
	 * @param display	Index der Anzeige
	 * @param data		Daten welche geschrieben werden sollen
	 * @param address	Adresse des registers in welches geschrieben werden sollen
	 */
	private void write(int display, byte data, byte address)
	{
		//TODO: pin.setState(PinState.HIGH);
		pin.setState(PinState.LOW); //DEBUG
		int c = 0;
		for (c = numberOfDevices-1; c > display; c--) {
			spi.write((byte)0x00, (byte)0x00);
		}

		spi.write(Utils.reverseBitsByte(data), Utils.reverseBitsByte(address));

		for (c =display-1; c >= 0; c--) {
			spi.write((byte)0x00, (byte)0x00);   
		}
		//TODO: pin.setState(PinState.LOW);
		pin.setState(PinState.HIGH); //DEBUG
	}
	
	/**
	 * Generiert den Bitstream der benötigt wird für ein Symbol
	 * @param digit	Symbol welches in ein Bitstream umgewandelt werden soll
	 * @return		Gibt den generierten Bitstream in Byte-Arrays zurück
	 */
	private byte[] generateBytesFromDigit(LedDigit digit)
	{
		byte[] values = new byte[8];
		for(Point p : digit.getPoints())
		{
			byte currB = values[valueOrder[(int)p.getY()]];
			
			for(int i = 0; i < 3; i++)
				if(globalColor.getValue()[i] == 0x01)
					currB = Utils.setBit(currB, rgbOrder[(int)p.getX()][i]);
			
			values[valueOrder[(int)p.getY()]] |= currB;
		}
		return values;
	}
	
	/**
	 * Setzt die Farbe aller Anzeigen
	 * @param color	Farbe welche leuchten soll
	 */
	public void setGlobalColor(LedColor color) 
	{
		globalColor = color;
	}
	
	/**
	 * Setzt den Dekodier-Mode aller Anzeigen
	 * @param decodeMode	Dekodier-Mode
	 */
	public void setDecodeModeAll(byte decodeMode)
	{
		writeAll(decodeMode, (byte)LedDriver.kgsDECODE_MODE);
	}
	
	/**
	 * Setzt den Dekodier-Mode einer Anzeige
	 * @param display		Index der Anzeige
	 * @param decodeMode	Dekodier-Mode
	 */
	public void setDecodeMode(int display, byte decodeMode)
	{
		write(display, decodeMode, (byte)LedDriver.kgsDECODE_MODE);
	}
	
	/**
	 * Setzt die globale Intensität der Anzeige
	 * @param intensity	Intensität von 0...15
	 */
	public void setGlobalIntensityAll(int intensity)
	{
		writeAll((byte)intensity, (byte)LedDriver.kgsGLOBAL_INTENSITY);
	}
	
	/**
	 * Setzt die globale Intensität einer bestimmten Anzeige
	 * @param display	Index der Anzeige
	 * @param intensity	Intensität von 0...15
	 */
	public void setGlobalIntensity(int display, int intensity)
	{
		write(display, (byte)intensity, (byte)LedDriver.kgsGLOBAL_INTENSITY);
	}
	
	/**
	 * Setzt das Scan-Limit (Welche Digits angezeigt werden sollen) aller Anzeigen
	 * @param scanLimit	Scan-Limit
	 */
	public void setScanLimitAll(byte scanLimit)
	{
		writeAll(scanLimit, (byte)LedDriver.kgsSCAN_LIMIT);
	}
	
	/**
	 * Setzt das Scan-Limit (Welche Digits angezeigt werden sollen) einer bestimmten Anzeige
	 * @param display	Index der Anzeige
	 * @param scanLimit	Scan-Limit
	 */
	public void setScanLimit(int display, byte scanLimit)
	{
		write(display, scanLimit, (byte)LedDriver.kgsSCAN_LIMIT);
	}
	
	/**
	 * Setzt den Shutdown-Mode aller Anzeigen
	 * @param shdnMode	Shutdown-Mode
	 */
	public void setShutdownModeAll(byte shdnMode)
	{
		writeAll(shdnMode, (byte)LedDriver.kgsSHUTDOWN);
	}
	
	/**
	 * Setzt den Shutdown-Mode einer bestimmten Anzeige
	 * @param display	Index der Anzeige
	 * @param shdnMode	Shutdown-Mode
	 */
	public void setShutdownMode(int display, byte shdnMode)
	{
		write(display, shdnMode, (byte)LedDriver.kgsSHUTDOWN);
	}
	
	/**
	 * Setzt die Features aller Anzeigen
	 * @param feature	Features
	 */
	public void setFeatureAll(byte feature)
	{
		writeAll(feature, (byte)LedDriver.kgsFEATURE);
	}
	
	/**
	 * Setzt die Features einer bestimmten Anzeige
	 * @param display	Index der Anzeige
	 * @param feature	Features
	 */
	public void setFeature(int display, byte feature)
	{
		write(display, feature, (byte)LedDriver.kgsFEATURE);
	}
	
	/**
	 * Setzt den DisplayTest aller Anzeigen
	 * @param dispTest	Display-Test
	 */
	public void setDisplayTestAll(byte dispTest)
	{
		writeAll(dispTest, (byte)LedDriver.kgsDISPLAY_TEST);
	}
	
	/**
	 * Setzt den DisplayTest einer bestimmten Anzeige
	 * @param display	Index der Anzeige
	 * @param dispTest	Display-Test
	 */
	public void setDisplayTest(int display, byte dispTest)
	{
		write(display, dispTest, (byte)LedDriver.kgsDISPLAY_TEST);
	}
	

	private byte[][] digitIntensities = new byte[4][8];		//Merkt eingestellte Intensitaet
	/**
	 * Setzt die Intensität von einem Digit auf allen Anzeigen
	 * @param digit		Digit welcher verstellt werden soll (0...7)
	 * @param intensity	Intensität (0...15)
	 */
	public void setIntensityAll(int digit, int intensity)
	{
		for(int i = 0; i < this.numberOfDevices; i++)
			setIntensity(i, digit, intensity);
	}
	
	/**
	 * Setzt die Intensität von einem Digit auf einer bestimmten Anzeige
	 * @param display	Index der Anzeige
	 * @param digit		Digit welcher verstellt werden soll (0...7)
	 * @param intensity	Intensität (0...15)
	 */
	public void setIntensity(int display, int digit, int intensity)
	{
		if(digit > 16)
			digit = 16;
		digitIntensities[display][digit] = (byte)intensity;
		
		if(digit == 0)
		{
			byte b = (byte) (digitIntensities[display][1] << 4);
			write(display,(byte)(b | intensity), (byte)(LedDriver.kgs01INTENSITY ));
		}
		if(digit == 1)
		{
			byte b = (byte) (intensity << 4);
			write(display,(byte)(b | digitIntensities[display][0]), (byte)(LedDriver.kgs01INTENSITY));
		}
		if(digit == 2)
		{
			byte b = (byte) (digitIntensities[display][3] << 4);
			write(display,(byte)(b | intensity), (byte)(LedDriver.kgs23INTENSITY ));
		}
		if(digit == 3)
		{
			byte b = (byte) (intensity << 4);
			write(display,(byte)(b | digitIntensities[display][2]), (byte)(LedDriver.kgs23INTENSITY ));
		}
		
		if(digit == 4)
		{
			byte b = (byte) (digitIntensities[display][5] << 4);
			write(display,(byte)(b | intensity), (byte)(LedDriver.kgs45INTENSITY));
		}
		if(digit == 5)
		{
			byte b = (byte) (intensity << 4);
			write(display,(byte)(b | digitIntensities[display][4]), (byte)(LedDriver.kgs45INTENSITY));
		}
		
		if(digit == 6)
		{
			byte b = (byte) (digitIntensities[display][7] << 4);
			write(display,(byte)(b | intensity), (byte)(LedDriver.kgs67INTENSITY ));
		}
		if(digit == 7)
		{
			byte b = (byte) (intensity << 4);
			write(display,(byte)(b | digitIntensities[display][6]), (byte)(LedDriver.kgs67INTENSITY ));
		}
	}
}
