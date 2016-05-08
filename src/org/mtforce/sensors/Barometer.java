package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Logger;
import org.mtforce.main.Utils;
import org.mtforce.main.Logger.Status;

/**
 * Beschreibung: Barometer - Misst den Luftdruck
 */
public class Barometer extends Sensor {

	public static final byte kgsADDRESS 					= 0x77;	//Adresse des Bausteins
	
	public static final byte kgsCMD_RESET 					= 0x1E; //PROM Resetten
	public static final byte kgsCMD_READ_PROM 				= (byte) 0xA0;	//READ PROM - Befehl
	public static final byte kgsCMD_CONVERSION_PRESSURE		= 0x40; //Umwandlung starten. Befehl setzt sich aus 0x40 verodert mit einer Resoltuion zusammen
	public static final byte kgsCMD_CONVERSION_TEMPERATURE	= 0x50; //Umwandlung starten. Befehl setzt sich aus 0x50 verodert mit einer Resoltuion zusammen
	public static final byte kgsCMD_READ					= 0x00;	//READ-Befehl
	
	public static final byte kgsRESOLUTION_256	= 0x00;				//ADC-Auflösung 8-bit
	public static final byte kgsRESOLUTION_512	= 0x01;				//ADC-Auflösung 9-bit
	public static final byte kgsRESOLUTION_1024	= 0x02;				//ADC-Auflösung 10-bit
	public static final byte kgsRESOLUTION_2048	= 0x03;				//ADC-Auflösung 11-bit
	public static final byte kgsRESOLUTION_4096	= 0x04; 			//ADC-Auflösung 12-bit
	
	public static final byte kgsPROM_COEFF_0 = 0x00;				//Adresse des Reserved-Register
	public static final byte kgsPROM_COEFF_1 = 0x02;				//Adresse des Koeffizient 1
	public static final byte kgsPROM_COEFF_2 = 0x04;				//Adresse des Koeffizient 2
	public static final byte kgsPROM_COEFF_3 = 0x06;				//Adresse des Koeffizient 3
	public static final byte kgsPROM_COEFF_4 = 0x08;				//Adresse des Koeffizient 4
	public static final byte kgsPROM_COEFF_5 = 0x0A;				//Adresse des Koeffizient 5
	public static final byte kgsPROM_COEFF_6 = 0x0C;				//Adresse des Koeffizient 6
	public static final byte kgsPROM_CRC 	 = 0x0E;				//Adresse des CRC
	
	public static final int kgsWAIT_256  = 1;						//Umwandlungswartezeit der 8-bit Umwandlung
	public static final int kgsWAIT_512  = 3;						//Umwandlungswartezeit der 9-bit Umwandlung
	public static final int kgsWAIT_1024 = 4;						//Umwandlungswartezeit der 10-bit Umwandlung
	public static final int kgsWAIT_2048 = 6;						//Umwandlungswartezeit der 11-bit Umwandlung
	public static final int kgsWAIT_4096 = 10;						//Umwandlungswartezeit der 12-bit Umwandlung
	
	private I2CManager i2c;											//Verweis auf I2CManager
	private byte gResolutionPressure	= kgsRESOLUTION_256;		//Von User eingestellte Umwandlungsauflösung des Drucks
	private byte gResolutionTemperature	= kgsRESOLUTION_256;		//Von User eingestellte Umwandlungsauflösung der Temperatur
	private int[] gCoeffizients			= new int[7];				//Koeffizienten für die Umrechnung speichern
	
	protected Barometer() {}
	
	/**
	 * Initialisiert den Baustein
	 */
	@Override
	public void init() 
	{
		i2c = (I2CManager) Sensors.getI2C();
		if(i2c.write(kgsADDRESS, kgsCMD_RESET))
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gCoeffizients = getCoeffizients();
			byte[] test = Utils.toBytes(getCRC(), 2);
			setEnabled(true);
		}
		else
		{
			Logger.log(Status.ERROR, this.getClass().getSimpleName(), "init failed! Device not functional");
		}
	}
	
	/**
	 * Resettet den Baustein
	 */
	public void reset() 
	{
		i2c.write(kgsADDRESS, kgsCMD_RESET);
	}
	
	/**
	 * Liest den ADC-Wert aus
	 */
	public int getAdcValue() 
	{
		i2c.write(kgsADDRESS, kgsCMD_READ);
		return Utils.toInt(Utils.reverseBytes(i2c.read(kgsADDRESS, 3)));
	}
	
	/**
	 * Umwandlungsauflösung der Druckmessung setzten
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
		i2c.write(kgsADDRESS, (byte)(kgsCMD_CONVERSION_PRESSURE | this.gResolutionPressure));
	}
	
	/**
	 * Umwandlungsauflösung der Temperaturmessung setzten
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
		i2c.write(kgsADDRESS, (byte)(kgsCMD_CONVERSION_TEMPERATURE | this.gResolutionTemperature));
	}
	
	/**
	 * Liest einen bestimmten Koeffizienten aus
	 * @param coeffizient	Koeffizienten-Konstante
	 * @return				gibt Koeffizient zurück
	 */
	public int getCoeffizient(byte coeffizient)
	{
		if(coeffizient != kgsPROM_COEFF_1 && coeffizient != kgsPROM_COEFF_2 && coeffizient != kgsPROM_COEFF_3 && coeffizient != kgsPROM_COEFF_4 && coeffizient != kgsPROM_COEFF_5 && coeffizient != kgsPROM_COEFF_6)
			return -1;
		//i2c.write(kgsADDRESS, (byte)(kgsCMD_READ_PROM | coeffizient));
		return Utils.toInt(Utils.reverseBytes(i2c.read(kgsADDRESS,(byte)(kgsCMD_READ_PROM | coeffizient), 2)));
	}
	
	/**
	 * Liest alle Kalibrierungskoeffizienten aus
	 * @return	Kalibrierungskoeffizienten
	 */
	public int[] getCoeffizients()
	{
		int coeff[] = new int[7];
		coeff[0] = this.getCoeffizient(kgsPROM_COEFF_0);
		coeff[1] = this.getCoeffizient(kgsPROM_COEFF_1);
		coeff[2] = this.getCoeffizient(kgsPROM_COEFF_2);
		coeff[3] = this.getCoeffizient(kgsPROM_COEFF_3);
		coeff[4] = this.getCoeffizient(kgsPROM_COEFF_4);
		coeff[5] = this.getCoeffizient(kgsPROM_COEFF_5);
		coeff[6] = this.getCoeffizient(kgsPROM_COEFF_6);
		byte[] test = Utils.toBytes(coeff[0], 2);
		System.out.println(Utils.byteToHexString(test[1]) + "  " + Utils.byteToHexString(test[0]));
		test = Utils.toBytes(coeff[1], 2);
		System.out.println(Utils.byteToHexString(test[1]) + "  " + Utils.byteToHexString(test[0]));
		test = Utils.toBytes(coeff[2], 2);
		System.out.println(Utils.byteToHexString(test[1]) + "  " + Utils.byteToHexString(test[0]));
		test = Utils.toBytes(coeff[3], 2);
		System.out.println(Utils.byteToHexString(test[1]) + "  " + Utils.byteToHexString(test[0]));
		test = Utils.toBytes(coeff[4], 2);
		System.out.println(Utils.byteToHexString(test[1]) + "  " + Utils.byteToHexString(test[0]));
		test = Utils.toBytes(coeff[5], 2);
		System.out.println(Utils.byteToHexString(test[1]) + "  " + Utils.byteToHexString(test[0]));
		test = Utils.toBytes(coeff[6], 2);
		System.out.println(Utils.byteToHexString(test[1]) + "  " + Utils.byteToHexString(test[0]));
		test = Utils.toBytes(getCRC(), 2);
		System.out.println(Utils.byteToHexString(test[1]) + "  " + Utils.byteToHexString(test[0]));
		
		/*int coeff[] = new int[7];
		coeff[0] = this.getCoeffizient(kgsPROM_COEFF_0);
		coeff[1] = 46372;
		coeff[2] = 43981;
		coeff[3] = 29059;
		coeff[4] = 27842;
		coeff[5] = 31553;
		coeff[6] = 28165;*/
		
		return coeff;
	}
	
	/**
	 * Liest den CRC-Wert aus dem PROM aus
	 * @return	gibt CRC-Wert zurück
	 */
	public int getCRC()
	{
		//i2c.write(kgsADDRESS, (byte)(kgsCMD_READ_PROM | kgsPROM_CRC));
		return Utils.toInt(Utils.reverseBytes(i2c.read(kgsADDRESS, (byte)(kgsCMD_READ_PROM | kgsPROM_CRC), 2)));
	}
	
	/**
	 * Berechnet die Temperatur-Differenz
	 * @param adcValue	ADC-Wert der Temperaturmessung
	 * @return	gibt Temperatur-Differenz zurück
	 */
	public int calculateTemperatureDifference(int adcValue)
	{
		return calculateTemperatureDifference(adcValue, this.gCoeffizients[4]);
	}
	
	/**
	 * Berechnet die Temperatur-Differenz
	 * @param adcValue	ADC-Wert der Temperaturmessung
	 * @param coeff5	Koeffizient 5
	 * @return			gibt Temperatur-Differenz zurück
	 */
	public int calculateTemperatureDifference(int adcValue, int coeff5)
	{
		int dT = adcValue - coeff5 * 256;
		return dT;
	}
	
	/**
	 * Berechnet Temperatur
	 * @param temperatureDifference	Ausgerechnete Temperatur-Differenz
	 * @return	gibt Temperatur zurück in Celsius * 10^2
	 */
	public int calculateTemperature(int temperatureDifference)
	{
		return this.calculateTemperature(temperatureDifference, gCoeffizients[5]);
	}
	
	/**
	 * Berechnet Temperatur
	 * @param temperatureDifference	Ausgerechnete Temperatur-Differenz
	 * @param coeff6				Koeffizient 6
	 * @return	gibt Temperatur zurück in Celsius * 10^2
	 */
	public int calculateTemperature(int temperatureDifference, int coeff6)
	{
		int temp = 2000 + (int)((temperatureDifference*(double)coeff6) / 8388608d);
		return temp;
	}
	
	/**
	 * Berechnet den Pressure-Offset
	 * @param temperatureDifference	Ausgerechnete Temperatur-Differenz
	 * @return	gibt den Pressure-Offset zurück
	 */
	public long calculatePressureOffset(int temperatureDifference)
	{
		return this.calculatePressureOffset(temperatureDifference, gCoeffizients[1], gCoeffizients[3]);
	}
	
	/**
	 * Berechnet den Pressure-Offset
	 * @param temperatureDifference	Ausgerechnete Temperatur-Differenz
	 * @param coeff2				Koeffizient 2
	 * @param coeff4				Koeffizient 4
	 * @return	gibt den Pressure-Offset zurück
	 */
	public long calculatePressureOffset(int temperatureDifference, int coeff2, int coeff4)
	{
		return ((long)coeff2 * 131072l) + ((long)coeff4 * (long)temperatureDifference)/64l;
	}
	
	/**
	 * Berechnet die Pressure-Sensitivity
	 * @param temperatureDifference	Ausgerechnete Temperatur-Differenz
	 * @return	gibt Pressure-Sensitivity
	 */
	public long calculatePressureSensitivity(int temperatureDifference)
	{
		return this.calculatePressureSensitivity(temperatureDifference, gCoeffizients[0], gCoeffizients[2]);
	}
	
	/**
	 * Berechnet die Pressure-Sensitivity
	 * @param temperatureDifference	Ausgerechnete Temperatur-Differenz
	 * @param coeff1				Koeffizient 1
	 * @param coeff3				Koeffizient 3
	 * @return gibt Pressure-Sensitivity zurück
	 */
	public long calculatePressureSensitivity(int temperatureDifference, int coeff1, int coeff3)
	{
		return (long)coeff1 * 65536l + ((long)coeff3 * (long)temperatureDifference)/128l;
	}
	
	/**
	 * Berechnet den Druck
	 * @param adcValue				ADC-Wert der Druckmessung
	 * @param pressureSensitivity	Ausgerechnete Pressure-Sensitivity
	 * @param pressureOffset		Ausgerechnete Pressure-Offset
	 * @return gibt Druck in mbar*10^2 zurück
	 */
	public int calculatePressure(int adcValue, long pressureSensitivity, long pressureOffset)
	{
		return (int)((((adcValue * pressureSensitivity)/2097152l) - pressureOffset) / 32768l);
	}
	
	/**
	 * Berechnet Temperaturwert mit einer erhöhten Genauigkeit
	 * @param temperature			Ausgerechneter Temperaturwert
	 * @param temperatureDifference	Ausgerechnete Temperaturdifferenz
	 * @return	gibt Temperaturwert in Celsius * 10^2 zurück
	 */
	public int calculateTemperatureCompensated(int temperature, int temperatureDifference)
	{
		long t2 = 0;
		if(temperature < 20000)
		{
			t2 = ((long)temperatureDifference*(long)temperatureDifference) / 2147483648l;
		}
		return (int)(temperature - t2);
	}
	
	/**
	 * Berechnet Druckwert mit einer erhöhten Genauigkeit
	 * @param adcPressure			ADC-Wert der Druckmessung
	 * @param temperature			Ausgerechneter Temperaturwert
	 * @param pressureSensitivity	Ausgerechnete Pressure-Sensitivity
	 * @param pressureOffset		Ausgerechneter Pressure-Offset
	 * @return	Druck in mBar * 10^2
	 */
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
	
	/**
	 * Misst den Temperaturwert und gibt ihn zurück
	 * @return	Temperatur in Celsius
	 */
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
	
	/**
	 * Liest den Druckwert aus und berechnet ihn
	 * @return	Druck in mBar
	 */
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
	
	/**
	 * Legt den Thread schlafen je nach momentaner ADC-Auflösung
	 * @param resolution	Momentane ADC-Auflösung
	 */
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
	
	/**
	 * Überprüft die Koeffizienten auf Bitfehler
	 * @param coeff	Koeffizienten die überprüft werden sollen
	 * @param crc	CRC zum überprüfen
	 * @return		?
	 */
	public char checkCRC(int[] coeff, int crc)
	{
		Logger.log("Barometer", "Starting crc");
		int[] coeff2 = new int[8];
		for(int i = 0; i < 7; i++)
			coeff2[i] = coeff[i];
		coeff2[7] = crc & 0xFF00;
		coeff = coeff2;
		
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
		Logger.log("Barometer", "crc done "+Utils.byteToHexString((byte)(n_rem ^ 0x0)) + " read crc: "+Utils.byteToHexString((byte)crc));
		return (char)(n_rem ^ 0x0);
		
		/*Logger.log("Barometer", "Starting crc");
		int cnt; // simple counter
		int n_rem; // crc reminder
		int crc_read; // original value of the crc
		char n_bit;
		
		n_rem = 0x00;
		crc_read=coeff[7]; //save read CRC
		coeff[7]=(0xFF00 & (coeff[7])); //CRC byte is replaced by 0
		
		for (cnt = 0; cnt < 16; cnt++) // operation is performed on bytes
		{// choose LSB or MSB
			if (cnt%2==1) n_rem ^= (short) ((coeff[cnt>>1]) & 0x00FF);
			else n_rem ^= (short) (coeff[cnt>>1]>>8);
			for (n_bit = 8; n_bit > 0; n_bit--)
			{
				if ( (n_rem & 0x8000) == 0x8000 )
				{
					n_rem = (n_rem << 1) ^ 0x3000;
				}
				else
				{
					n_rem = (n_rem << 1);
				}
			}
		}
		
		n_rem= (0x000F & (n_rem >> 12)); // final 4-bit reminder is CRC code
		coeff[7]=crc_read; // restore the crc_read to its original place
		
		Logger.log("Barometer", "crc done "+Utils.byteToHexString((byte)(n_rem ^ 0x0)) + " read crc: "+Utils.byteToHexString((byte)crc_read));
		
		return (char) (n_rem ^ 0x0);*/
	}
}
