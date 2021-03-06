package org.mtforce.sensors;

import org.mtforce.interfaces.I2CManager;
import org.mtforce.main.Logger;
import org.mtforce.main.Utils;
import org.mtforce.main.Logger.Status;

public class DOF9 extends Sensor 
{
	/**
	 * Beschreibung: Gyrosensor
	 * 
	 * Konstanten: Komplett
	 * Funktionen: NICHT Komplett
	 * 
	 * TODO: Modultest, INIT!!!! 
	 */
	public static final byte kgsREG_SELF_TEST_X_GYRO = 0x00;
	public static final byte kgsREG_SELF_TEST_Y_GYRO = 0x01;
	public static final byte kgsREG_SELF_TEST_Z_GYRO = 0x02;
	public static final byte kgsREG_SELF_TEST_X_ACCEL = 0x0D;
	public static final byte kgsREG_SELF_TEST_Y_ACCEL = 0x0E;
	public static final byte kgsREG_SELF_TEST_Z_ACCEL = 0x0F;
	public static final byte kgsREG_XG_OFFSET_H = 0x13;
	public static final byte kgsREG_XG_OFFSET_L = 0x14;
	public static final byte kgsREG_YG_OFFSET_H = 0x15;
	public static final byte kgsREG_YG_OFFSET_L = 0x16;
	public static final byte kgsREG_ZG_OFFSET_H = 0x17;
	public static final byte kgsREG_ZG_OFFSET_L = 0x18;
	public static final byte kgsREG_SMPLRT_DIV = 0x19;
	public static final byte kgsREG_CONFIG = 0x1A;
	public static final byte kgsREG_GYRO_CONFIG = 0x1B;
	public static final byte kgsREG_ACCEL_CONFIG = 0x1C;
	public static final byte kgsREG_ACCEL_CONFIG_2 = 0x1D;
	public static final byte kgsREG_LP_ACCEL_ODR = 0x1E;
	public static final byte kgsREG_WOM_THR = 0x1F;
	public static final byte kgsREG_FIFO_EN = 0x23;
	public static final byte kgsREG_I2C_MST_CTRL = 0x24;
	public static final byte kgsREG_I2C_SLV0_ADDR = 0x25;
	public static final byte kgsREG_I2C_SLV0_REG = 0x26;
	public static final byte kgsREG_I2C_SLV0_CTRL = 0x27;
	public static final byte kgsREG_I2C_SLV1_ADDR = 0x28;
	public static final byte kgsREG_I2C_SLV1_REG = 0x29;
	public static final byte kgsREG_I2C_SLV1_CTRL = 0x2A;
	public static final byte kgsREG_I2C_SLV2_ADDR = 0x2B;
	public static final byte kgsREG_I2C_SLV2_REG = 0x2C;
	public static final byte kgsREG_I2C_SLV2_CTRL = 0x2D;
	public static final byte kgsREG_I2C_SLV3_ADDR = 0x2E;
	public static final byte kgsREG_I2C_SLV3_REG = 0x2F;
	public static final byte kgsREG_I2C_SLV3_CTRL = 0x30;
	public static final byte kgsREG_I2C_SLV4_ADDR = 0x31;
	public static final byte kgsREG_I2C_SLV4_REG = 0x32;
	public static final byte kgsREG_I2C_SLV4_DO = 0x33;
	public static final byte kgsREG_I2C_SLV4_CTRL = 0x34;
	public static final byte kgsREG_I2C_SLV4_DI = 0x35;
	public static final byte kgsREG_I2C_MST_STATUS = 0x36;
	public static final byte kgsREG_INT_PIN_CFG = 0x37;
	public static final byte kgsREG_INT_ENABLE = 0x38;
	public static final byte kgsREG_INT_STATUS = 0x3A;
	public static final byte kgsREG_ACCEL_XOUT_H = 0x3B;
	public static final byte kgsREG_ACCEL_XOUT_L = 0x3C;
	public static final byte kgsREG_ACCEL_YOUT_H = 0x3D;
	public static final byte kgsREG_ACCEL_YOUT_L = 0x3E;
	public static final byte kgsREG_ACCEL_ZOUT_H = 0x3F;
	public static final byte kgsREG_ACCEL_ZOUT_L = 0x40;
	public static final byte kgsREG_TEMP_OUT_H = 0x41;
	public static final byte kgsREG_TEMP_OUT_L = 0x42;
	public static final byte kgsREG_GYRO_XOUT_H = 0x43;
	public static final byte kgsREG_GYRO_XOUT_L = 0x44;
	public static final byte kgsREG_GYRO_YOUT_H = 0x45;
	public static final byte kgsREG_GYRO_YOUT_L = 0x46;
	public static final byte kgsREG_GYRO_ZOUT_H = 0x47;
	public static final byte kgsREG_GYRO_ZOUT_L = 0x48;
	public static final byte kgsREG_EXT_SENS_DATA_00 = 0x49;
	public static final byte kgsREG_EXT_SENS_DATA_01 = 0x4A;
	public static final byte kgsREG_EXT_SENS_DATA_02 = 0x4B;
	public static final byte kgsREG_EXT_SENS_DATA_03 = 0x4C;
	public static final byte kgsREG_EXT_SENS_DATA_04 = 0x4D;
	public static final byte kgsREG_EXT_SENS_DATA_05 = 0x4E;
	public static final byte kgsREG_EXT_SENS_DATA_06 = 0x4F;
	public static final byte kgsREG_EXT_SENS_DATA_07 = 0x50;
	public static final byte kgsREG_EXT_SENS_DATA_08 = 0x51;
	public static final byte kgsREG_EXT_SENS_DATA_09 = 0x52;
	public static final byte kgsREG_EXT_SENS_DATA_10 = 0x53;
	public static final byte kgsREG_EXT_SENS_DATA_11 = 0x54;
	public static final byte kgsREG_EXT_SENS_DATA_12 = 0x55;
	public static final byte kgsREG_EXT_SENS_DATA_13 = 0x56;
	public static final byte kgsREG_EXT_SENS_DATA_14 = 0x57;
	public static final byte kgsREG_EXT_SENS_DATA_15 = 0x58;
	public static final byte kgsREG_EXT_SENS_DATA_16 = 0x59;
	public static final byte kgsREG_EXT_SENS_DATA_17 = 0x5A;
	public static final byte kgsREG_EXT_SENS_DATA_18 = 0x5B;
	public static final byte kgsREG_EXT_SENS_DATA_19 = 0x5C;
	public static final byte kgsREG_EXT_SENS_DATA_20 = 0x5D;
	public static final byte kgsREG_EXT_SENS_DATA_21 = 0x5E;
	public static final byte kgsREG_EXT_SENS_DATA_22 = 0x5F;
	public static final byte kgsREG_EXT_SENS_DATA_23 = 0x60;
	public static final byte kgsREG_I2C_SLV0_DO = 0x63;
	public static final byte kgsREG_I2C_SLV1_DO = 0x64;
	public static final byte kgsREG_I2C_SLV2_DO = 0x65;
	public static final byte kgsADDRESS = 0x68;
	public static final byte kgsREG_I2C_SLV3_DO = 0x66;
	public static final byte kgsREG_I2C_MST_DELAY_CTRL = 0x67;
	public static final byte kgsREG_SIGNAL_PATH_RESET = 0x68;
	public static final byte kgsREG_MOT_DETECT_CTRL = 0x69;
	public static final byte kgsREG_USER_CTRL = 0x6A;
	public static final byte kgsREG_PWR_MGMT_1 = 0x6B;
	public static final byte kgsREG_PWR_MGMT_2 = 0x6C;
	public static final byte kgsREG_FIFO_COUNTH = 0x72;
	public static final byte kgsREG_FIFO_COUNTL = 0x73;
	public static final byte kgsREG_FIFO_R_W = 0x74;
	public static final byte kgsREG_WHO_AM_I = 0x75;
	public static final byte kgsREG_XA_OFFSET_H = 0x77;
	public static final byte kgsREG_XA_OFFSET_L = 0x78;
	public static final byte kgsREG_YA_OFFSET_H = 0x7A;
	public static final byte kgsREG_YA_OFFSET_L = 0x7B;
	public static final byte kgsREG_ZA_OFFSET_H = 0x7D;
	public static final byte kgsREG_ZA_OFFSET_L = 0x7E;
	public static final byte kgsREG_MAGNET_HX_L = 0x03;
	public static final byte kgsREG_MAGNET_HX_H = 0x04;
	public static final byte kgsREG_MAGNET_HY_L = 0x05;
	public static final byte kgsREG_MAGNET_HY_H = 0x06;
	public static final byte kgsREG_MAGNET_HZ_L = 0x07;
	public static final byte kgsREG_MAGNET_HZ_H = 0x08;
	
	

			

	private I2CManager i2c;
	private int Vref = 3300; 	//Versorgung
	private int VzeroG = 1650;	//Vref bei Zero G
	private double ADCword = Math.pow(2, 16);
	
	int RoomTemp_Offset = 0;
	int Temp_Sensitivity = 0;
	
	protected DOF9() {}
	
	@Override
	public void init() {
		i2c = (I2CManager)Sensors.getI2C();
		if(i2c.write(this.kgsADDRESS, kgsREG_CONFIG, (byte)0x00))
		{
			setEnabled(true);
		}
		else
		{
			Logger.log(Status.ERROR, this.getClass().getSimpleName(), "init failed! Device not functional");
		}
	}
	
	/**
	 * Rechnet Rx Komponente aus 
	 * @return
	 */
	public double getRx(){
		int ADCRx = getACCEL_XOUT();
		byte Sensitivity = getGYRO_FS_SEL();
		double Rx=(ADCRx*Vref/ADCword - VzeroG)/Sensitivity;
		return Rx;
	}
	/**
	 * Rechnet Ry Komponente aus 
	 * @return
	 */
	public double getRy(){
		int ADCRy = getACCEL_YOUT();
		byte Sensitivity = getGYRO_FS_SEL();
		double Ry=(ADCRy*Vref/ADCword - VzeroG)/Sensitivity;
		return Ry;
	}
	/**
	 * Rechnet Rz Komponente aus 
	 * @return
	 */
	public double getRz(){
		int ADCRz = getACCEL_XOUT();
		byte Sensitivity = getGYRO_FS_SEL();
		double Rz=(ADCRz*Vref/ADCword - VzeroG)/Sensitivity;
		return Rz;
	}
	/**
	 * Berechnet aus Rx, Ry, Rz die R Komponente
	 * @return
	 */
	public double getR(){
		double R = Math.sqrt(Math.pow(getRx(), 2) + Math.pow(getRy(), 2) + Math.pow(getRz(), 2));
		return R; 
	}
	/**
	 * Berechnet die X Lage
	 * @return
	 */
	public double getX(){
		double angle;
		double R=getR();
		angle = Math.acos(getRx()/R);
		return angle;
	}
	/**
	 * Berechnet die Y Lage
	 * @return
	 */
	public double getY(){
		double angle;
		double R=getR();
		angle = Math.acos(getRy()/R);
		return angle;
	}
	/**
	 * Berechnet die X Lage
	 * @return
	 */
	public double getZ(){
		double angle;
		double R=getR();
		angle = Math.acos(getRz()/R);
		return angle;
	}
	/*****************
	 * MAGNETOMETER OUTPUT
	 * @return
	 */
	public int getMicroTesla_X()
	{
		return (int) (getMAGNETO_XOUT() * 0.15);
	}
	
	public int getMicroTesla_Y()
	{
		return (int) (getMAGNETO_YOUT() * 0.15);
	}
	
	public int getMicroTeslaZ()
	{
		return (int) (getMAGNETO_ZOUT() * 0.15);
	}
	
	public int getMAGNETO_XOUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_MAGNET_HX_H);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_MAGNET_HX_L);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
	
	public int getMAGNETO_YOUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_MAGNET_HY_H);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_MAGNET_HY_L);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
	
	public int getMAGNETO_ZOUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_MAGNET_HZ_H);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_MAGNET_HZ_L);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
	/*****************
	 * CONFIG REGISTER
	 * @return
	 */
	
	/** Gibt FIFO-Konfiguration Zur�ck
	 * @return
	 */
	public byte getFIFO()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 6, 6);
		return packet;
	}
	/**
	 * When set to �1�, when the fifo is full, 
	 * additional writes will not be written to fifo.
	 *  
	 * When set to �0�, when the fifo is full, 
	 * additional writes will be written to the fifo, 
	 * replacing the oldest data.
	 * @return
	 */
	public void setFIFO(byte fifo)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, fifo);
	}
	/**
	 * 
	 * @return packet
	 */
	public byte getEXT_SYNC_SET()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 3, 5);
		return packet;
	}
	/**
	 * 	0		function disabled
		1		TEMP_OUT_L[0]
		2		GYRO_XOUT_L[0]
		3		GYRO_YOUT_L[0]
		4		GYRO_ZOUT_L[0]
		5		ACCEL_XOUT_L[0]
		6		ACCEL_YOUT_L[0]
		7		ACCEL_ZOUT_L[0]
		
		Fsync will be latched to capture short strobes. 
		This will be done such that if Fsync toggles, 
		the latched value toggles, but won�t toggle again 
		until the new latched value is captured by the sample 
		rate strobe. This is a requirement for working with some 
		3rd party devices that have fsync strobes shorter than our sample rate.
	 * @param EXT_SYNC_SET
	 */
	public void setEXT_SYNC_SET(byte EXT_SYNC_SET)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, EXT_SYNC_SET);
	}
	/**
	 * Gibt die DLPF-Konfiguration zur�ck
	 * @return
	 */
	public byte getDLPF_CFG()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 0, 2);
		return packet;
	}
	/**
	 * For the DLPF to be used, fchoice[1:0] must be set to 2�b11, fchoice_b[1:0] is 2�b00.
	 * @param DLPF_CFG
	 */
	public void setDLPF_CFG(byte DLPF_CFG)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, DLPF_CFG);
	}
	/*************************
	 * Gyroscope Configuration
	 */
	/**
	 * Returniert X Gyro self-test
	 * @return
	 */
	public byte getXGYRO_Cten()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 7, 7);
		return packet;
	}
	/**
	 * setzt X Gyro self-test
	 * @param XGYRO_Cten
	 */
	public void setXGYRO_Cten(byte XGYRO_Cten)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, XGYRO_Cten);
	}
	/**
	 * Returniert Y Gyro self-test
	 * @return
	 */
	public byte getYGYRO_Cten()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 6, 6);
		return packet;
	}
	/**
	 * Setzt Y Gyro self-test
	 * @param YGYRO_Cten
	 */
	public void setYGYRO_Cten(byte YGYRO_Cten)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, YGYRO_Cten);
	}
	/**
	 * Returniert Z Gyro self-test
	 * @return
	 */
	public byte getZGYRO_Cten()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 5, 5);
		return packet;
	}
	/**
	 * Setzt Z Gyro self-test
	 * @param ZGYRO_Cten
	 */
	public void setZGYRO_Cten(byte ZGYRO_Cten)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, ZGYRO_Cten);
	}
	/**
	 * Returniert die Full Scale Select- Konfiguration
	 * @return
	 */
	public byte getGYRO_FS_SEL()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 4, 3);
		return packet;
	}
	/**
	 * Setzt die Full Scale
	 * Gyro Full Scale Select:
		00 = +250dps
		01= +500 dps
		10 = +1000 dps
		11 = +2000 dps
	 * @param GYRO_FS_SEL
	 */
	public void setGYRO_FS_SEL(byte GYRO_FS_SEL)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, GYRO_FS_SEL);
	}
	/**
	 * Returniert die FChoice Konfiguration
	 * @return
	 */
	public byte getFchoice_b()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 1, 0);
		return packet;
	}
	/**
	 * Setzt die FChoice Konfiguration
		 * Used to bypass DLPF as shown in table 1 above. 
		 * NOTE: Register is Fchoice_b (inverted version of Fchoice), 
		 * table 1 uses Fchoice (which is the inverted version of this register).
	 * @param Fchoice_b
	 */
	public void setFchoice_b(byte Fchoice_b)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, Fchoice_b);
	}
	
	
	/*****************************
	 * Accelerometer Configuration
	 * 
	 */
	/**
	 * Returniert X Accel self-test
	 * @return
	 */
	public byte getAx_st_en()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 7, 7);
		return packet;
	}
	/**
	 * Setzt den X Accel self-test
	 * @param ax_st_en
	 */
	public void setAx_st_en(byte ax_st_en)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, ax_st_en);
	}
	/**
	 * Returniert Y Accel self-test
	 * @return
	 */
	public byte getAy_st_en()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 6, 6);
		return packet;
	}
	/**
	 * Setzt Y Accel self-test
	 * @param ay_st_en
	 */
	public void setAy_st_en(byte ay_st_en)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, ay_st_en);
	}
	/**
	 * Returniert Z Accel self-test
	 * @return
	 */
	public byte getAz_st_en()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 5, 5);
		return packet;
	}
	/**
	 * Setzt den Z Accel self-test
	 * @param az_st_en
	 */
	public void setAz_st_en(byte az_st_en)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, az_st_en);
	}
	/**
	 * Returniert die Acceleration Fullscale
	 * @return
	 */
	public byte getACCEL_FS_SEL()
	{
		byte packet = i2c.read(kgsADDRESS, kgsREG_CONFIG);
		packet = Utils.isolateBits(packet, 4, 3);
		return packet;
	}
	/**
	 * Accel Full Scale Select:
		�2g (00)
		�4g (01)
		�8g (10)
		�16g (11)
	 * @param ACCEL_FS_SEL
	 */
	public void setACCEL_FS_SEL(byte ACCEL_FS_SEL)
	{
		i2c.write(kgsADDRESS, kgsREG_CONFIG, ACCEL_FS_SEL);
	}

	/******************************
	 * 
	 * Accelerometer Measurements
	 */
	public int getACCEL_XOUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_ACCEL_XOUT_L);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_ACCEL_XOUT_H);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
	/**
	 * Bekommt den aktuellen Beschleungigungswert in Y-Richtung
	 * @return
	 */
	public int getACCEL_YOUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_ACCEL_YOUT_L);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_ACCEL_YOUT_H);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
	/**
	 * Bekommt den aktuellen Beschleungigungswert in Z-Richtung
	 * @return
	 */
	public int getACCEL_ZOUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_ACCEL_ZOUT_L);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_ACCEL_ZOUT_H);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
	/**
	 * Temperatur Kalkulation
	 * @param TEMP_OUT
	 * @return
	 */
	public int getTemp(int TEMP_OUT)
	{
		int TEMP_degC = ((TEMP_OUT - RoomTemp_Offset)/Temp_Sensitivity)+21;
		return TEMP_degC;
	}
	/*************************
	 * Temperature Measurement
	 * ((TEMP_OUT � RoomTemp_Offset)/Temp_Sensitivity) + 21degC
	 * @return
	 */
	public int getTEMP_OUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_TEMP_OUT_L);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_TEMP_OUT_H);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
	/**
	 * Gyro Measurement
	 * @return
	 */
	
	/**
	 * X-Axis gyroscope output
	 * @return
	 */
	public int getGYRO_XOUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_GYRO_XOUT_L);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_GYRO_XOUT_H);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
	/**
	 * Y-Axis gyroscope output
	 * @return
	 */
	public int getGYRO_YOUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_GYRO_YOUT_L);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_GYRO_YOUT_H);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
	/**
	 * Z-Axis gyroscope output
	 * @return
	 */
	public int getGYRO_ZOUT()
	{
		byte[] packet = new byte[2];
		packet[0] = i2c.read(kgsADDRESS, kgsREG_GYRO_ZOUT_L);
		packet[1] = i2c.read(kgsADDRESS, kgsREG_GYRO_ZOUT_H);
		int iPacket = Utils.toInt(packet);
		return iPacket;
	}
}
