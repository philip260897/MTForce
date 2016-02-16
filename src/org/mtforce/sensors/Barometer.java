package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Sensors;
import org.mtforce.main.Utils;

/**
 * Beschreibung: Barometer - Misst den Luftdruck
 * 
 * Konstanten: Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: Modultest
 *	CRC CHECK TESTEN
 */
public class Barometer extends Sensor {

	public static final byte ADDRESS 		= 0x76;			//LAST BIT = !CSB (CSB = HARDWARE PIN)
	public static final byte RESET 			= 0x1E; 		//RESETS PROM
	
	public static final byte kgsCMD_READ_PROM 		= (byte) 0xA0;	//8-BIT DATA FOLLOWED BY ACK 2 TIMES (16BIT)
	
	public static final byte kgsCMD_CONVERSION_PRESSURE		= 0x40; //Umwandlung starten. Befehl setzt sich aux 0x40 verodert mit einer resoltuion zusammen
	public static final byte kgsCMD_CONVERSION_TEMPERATURE	= 0x50; //Umwandlung starten. Befehl setzt sich aux 0x50 verodert mit einer resoltuion zusammen
	public static final byte kgsCMD_READ					= 0x00;	//8-BIT DATA FOLLOWED BY ACK 3 TIMES (24BIT)
	
	public static final byte kgsRESOLUTION_256	= 0x00;
	public static final byte kgsRESOLUTION_512	= 0x01;
	public static final byte kgsRESOLUTION_1024	= 0x02;
	public static final byte kgsRESOLUTION_2048	= 0x03;
	public static final byte kgsRESOLUTION_4096	= 0x04;
	
	public static final byte kgsPROM_COEFF_1 = 0x02;
	public static final byte kgsPROM_COEFF_2 = 0x04;
	public static final byte kgsPROM_COEFF_3 = 0x06;
	public static final byte kgsPROM_COEFF_4 = 0x08;
	public static final byte kgsPROM_COEFF_5 = 0x0A;
	public static final byte kgsPROM_COEFF_6 = 0x0C;
	public static final byte kgsPROM_CRC 	 = 0x0E;
	
	public static final int kgsWAIT_256  = 1;
	public static final int kgsWAIT_512  = 3;
	public static final int kgsWAIT_1024 = 4;
	public static final int kgsWAIT_2048 = 6;
	public static final int kgsWAIT_4096 = 10;
	
	private I2CManager i2c;									//Verweis auf I2CManager
	private byte gResolutionPressure	= kgsRESOLUTION_256;//Von User eingestellte Umwandlungsaufloesung des Drucks
	private byte gResolutionTemperature	= kgsRESOLUTION_256;//Von User eingestellte Umwandlungsaufloesung der Temperatur
	private int[] gCoeffizients			= new int[6];		//Koeffizienten fuer die Umrechnung speichern
	
	/**
	 * Initialisiert den Baustein
	 */
	@Override
	public void init() 
	{
		i2c = (I2CManager) Sensors.getI2C();
		if(i2c.write(ADDRESS, RESET))
		{
			gCoeffizients = getCoeffizients();
			checkCRC(gCoeffizients, getCRC());
			setEnabled(true);
		}
	}
	
	/**
	 * Resettet den Baustein
	 */
	public void reset() 
	{
		i2c.write(ADDRESS, RESET);
	}
	
	/**
	 * Liest den ADC-Wert aus
	 */
	public int getAdcValue() 
	{
		i2c.write(ADDRESS, kgsCMD_READ);
		return Utils.toInt(Utils.reverseBytes(i2c.read(ADDRESS, 3)));
	}
	
	/**
	 * Umwandlungsaufloesung der Druckmessung setzten
	 * @param resolution	Umwandlungskonstante (zwischen 0 - 4, falls > 4, wird 0 verwendet)
	 */
	public void setResolutionPressure(byte resolution)
	{
		if((int)resolution <= 4)
			gResolutionPressure = resolution;
		else
			gResolutionPressure = kgsRESOLUTION_256;
	}
	
	/**
	 * Startet Druck-ADC
	 */
	public void startConversionPressure() 
	{
		i2c.write(ADDRESS, (byte)(kgsCMD_CONVERSION_PRESSURE | this.gResolutionPressure));
	}
	
	/**
	 * Umwandlungsaufloesung der Temperaturmessung setzten
	 * @param resolution	Umwandlungskonstante (zwischen 0 - 4, falls > 4, wird 0 verwendet)
	 */
	public void setResoltuionTemperature(byte resolution)
	{
		if((int)resolution <= 4)
			gResolutionTemperature = resolution;
		else
			gResolutionTemperature = kgsRESOLUTION_256;
	}
	
	/**
	 * Startet Temperatur-ADC
	 */
	public void startConversionTemperature()
	{
		i2c.write(ADDRESS, (byte)(kgsCMD_CONVERSION_TEMPERATURE | this.gResolutionTemperature));
	}
	
	public int getCoeffizient(byte coeffizient)
	{
		if(coeffizient != kgsPROM_COEFF_1 && coeffizient != kgsPROM_COEFF_2 && coeffizient != kgsPROM_COEFF_3 && coeffizient != kgsPROM_COEFF_4 && coeffizient != kgsPROM_COEFF_5 && coeffizient != kgsPROM_COEFF_6)
			return -1;
		i2c.write(ADDRESS, (byte)(kgsCMD_READ_PROM | coeffizient));
		return Utils.toInt(Utils.reverseBytes(i2c.read(ADDRESS, 2)));
	}
	
	public int[] getCoeffizients()
	{
		int coeff[] = new int[6];
		coeff[0] = this.getCoeffizient(kgsPROM_COEFF_1);
		coeff[1] = this.getCoeffizient(kgsPROM_COEFF_2);
		coeff[2] = this.getCoeffizient(kgsPROM_COEFF_3);
		coeff[3] = this.getCoeffizient(kgsPROM_COEFF_4);
		coeff[4] = this.getCoeffizient(kgsPROM_COEFF_5);
		coeff[5] = this.getCoeffizient(kgsPROM_COEFF_6);
		return coeff;
	}
	
	public int getCRC()
	{
		i2c.write(ADDRESS, (byte)(kgsCMD_READ_PROM | kgsPROM_CRC));
		return Utils.toInt(Utils.reverseBytes(i2c.read(ADDRESS, 2)));
	}
	
	public int calculateTemperatureDifference(int adcValue)
	{
		return calculateTemperatureDifference(adcValue, this.gCoeffizients[4]);
	}
	
	public int calculateTemperatureDifference(int adcValue, int coeff5)
	{
		int dT = adcValue - coeff5 * 256;
		return dT;
	}
	
	public int calculateTemperature(int temperatureDifference)
	{
		return this.calculateTemperature(temperatureDifference, gCoeffizients[5]);
	}
	
	public int calculateTemperature(int temperatureDifference, int coeff6)
	{
		int temp = 2000 + (int)((temperatureDifference*(double)coeff6) / 8388608d);
		return temp;
	}
	
	public long calculatePressureOffset(int temperatureDifference)
	{
		return this.calculatePressureOffset(temperatureDifference, gCoeffizients[1], gCoeffizients[3]);
	}
	
	public long calculatePressureOffset(int temperatureDifference, int coeff2, int coeff4)
	{
		return ((long)coeff2 * 131072l) + ((long)coeff4 * (long)temperatureDifference)/64l;
	}
	
	public long calculatePressureSensitivity(int temperatureDifference)
	{
		return this.calculatePressureSensitivity(temperatureDifference, gCoeffizients[0], gCoeffizients[2]);
	}
	
	public long calculatePressureSensitivity(int temperatureDifference, int coeff1, int coeff3)
	{
		return (long)coeff1 * 65536l + ((long)coeff3 * (long)temperatureDifference)/128l;
	}
	
	public int calculatePressure(int adcValue, long pressureSensitivity, long pressureOffset)
	{
		return (int)((((adcValue * pressureSensitivity)/2097152l) - pressureOffset) / 32768l);
	}
	
	public int calculateTemperatureCompensated(int temperature, int temperatureDifference)
	{
		long t2 = 0;
		if(temperature < 20000)
		{
			t2 = ((long)temperatureDifference*(long)temperatureDifference) / 2147483648l;
		}
		return (int)(temperature - t2);
	}
	
	public int calculatePressureCompensated(int adcPressure, int temperature, long pressureSensitivity, long pressureOffset)
	{
		long off2 = 0, sens2 = 0;
		if(temperature < 20000)
		{
			off2 = 61 * (temperature - 2000)*(temperature - 2000) / 16;
			sens2 = 2 * (temperature - 2000)*(temperature - 2000);
			if(temperature < 1500)
			{
				off2 = off2 + 15 * (temperature + 1500) * (temperature + 1500);
				sens2 = sens2 + 8 * (temperature + 1500) * (temperature + 1500);
			}
		}
		
		pressureSensitivity = pressureSensitivity - sens2;
		pressureOffset = pressureOffset - off2;
		return calculatePressure(adcPressure, pressureSensitivity, pressureOffset);
	}
	
	public double getTemperature()
	{
		this.startConversionTemperature();
		sleepForResult(this.gResolutionTemperature);
		int adcValue 	= getAdcValue();
		int tempDiff	= calculateTemperatureDifference(adcValue);
		int temp		= calculateTemperature(tempDiff);
		int compTemp	= calculateTemperatureCompensated(temp, tempDiff);
		return compTemp / 100;
	}
	
	public double getPressure()
	{
		this.startConversionTemperature();
		sleepForResult(this.gResolutionTemperature);
		int adcValue 	= getAdcValue();
		int tempDiff	= calculateTemperatureDifference(adcValue);
		int temp		= calculateTemperature(tempDiff, 28165);
		
		
		this.startConversionPressure();
		sleepForResult(this.gResolutionPressure);
		adcValue 	= getAdcValue();
		long pOFF		= calculatePressureOffset(tempDiff);
		long pSENS		= calculatePressureSensitivity(tempDiff);
		int compPress	= calculatePressureCompensated(adcValue, temp, pSENS, pOFF);
		return compPress / 100;
	}
	
	private void sleepForResult(byte resolution)
	{
		try
		{
			int sleepTime = kgsWAIT_4096;
			switch(resolution)
			{
				case kgsRESOLUTION_256: sleepTime = kgsWAIT_256; break;
				case kgsRESOLUTION_512: sleepTime = kgsWAIT_512; break;
				case kgsRESOLUTION_1024: sleepTime = kgsWAIT_1024; break;
				case kgsRESOLUTION_2048: sleepTime = kgsWAIT_2048; break;
				case kgsRESOLUTION_4096: sleepTime = kgsWAIT_4096; break;
				default: sleepTime = kgsWAIT_4096;
			}
			Thread.sleep(sleepTime);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private char checkCRC(int[] coeff, int crc)
	{
		//TODO: CRC TESTEN!!!! SEHR WICHTIG!!!!
		int cnt;
		long n_rem;
		long crc_read;
		int n_bit;
		n_rem = 0x00;
		
		crc_read = crc;
		crc = (0xFF00 & (crc));
		for(cnt = 0; cnt < 16; cnt++)
		{
			if(cnt % 2 == 1)
			{
				n_rem ^= (long) ((coeff[cnt>>1]) & 0x00FF);
			}
			else
			{
				n_rem ^= (long) (coeff[cnt>>1]>>8);
			}
			
			for(n_bit = 8; n_bit > 0; n_bit--)
			{
				if((n_rem & (0x8000)) == (0x8000))
				{
					n_rem = (n_rem << 1) ^ 0x3000; 
				}
				else
				{
					n_rem = (n_rem << 1); 
				}
			}
		}
		
		n_rem = (0x000F & (n_rem >> 12));
		crc = (int) crc_read;
		return (char)(n_rem ^ 0x0);
	}
}
