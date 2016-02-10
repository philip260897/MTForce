package org.mtforce.sensors;

public class DOF9 extends Sensor 
{
	/**
	 * Beschreibung: Gyrosensor
	 * 
	 * Konstanten: NICHT Komplett
	 * Funktionen: NICHT Komplett
	 * 
	 * TODO: Modultest
	 */

	public static final byte I2C_SLV4_DI = 0x35;
	public static final byte I2C_MST_STATUS = 0x36;
	public static final byte INT_PIN_CFG = 0x37;
	public static final byte INT_ENABLE = 0x38;
	public static final byte INT_STATUS = 0x3A;
	public static final byte ACCEL_XOUT_H = 0x3B;
	public static final byte ACCEL_XOUT_L = 0x3C;
	public static final byte ACCEL_YOUT_H = 0x3D;
	public static final byte ACCEL_YOUT_L = 0x3E;
	public static final byte ACCEL_ZOUT_H = 0x3F;
	public static final byte ACCEL_ZOUT_L = 0x40;
	public static final byte TEMP_OUT_H = 0x41;
	public static final byte TEMP_OUT_L = 0x42;
	public static final byte GYRO_XOUT_H = 0x43;
	public static final byte GYRO_ZOUT_L = 0x48;
	public static final byte EXT_SENS_DATA_00 = 0x49;
	public static final byte EXT_SENS_DATA_01 = 0x4A;
	public static final byte EXT_SENS_DATA_02 = 0x4B;
	public static final byte EXT_SENS_DATA_03 = 0x4C;
	public static final byte EXT_SENS_DATA_04 = 0x4D;
	public static final byte EXT_SENS_DATA_05 = 0x4E;
	public static final byte EXT_SENS_DATA_06 = 0x4F;
	public static final byte EXT_SENS_DATA_07 = 0x50;
	public static final byte EXT_SENS_DATA_08 = 0x51;
	public static final byte EXT_SENS_DATA_09 = 0x52;
	public static final byte EXT_SENS_DATA_10 = 0x53;
	public static final byte EXT_SENS_DATA_11 = 0x54;
	public static final byte EXT_SENS_DATA_12 = 0x55;
	public static final byte EXT_SENS_DATA_13 = 0x56;
	public static final byte EXT_SENS_DATA_14 = 0x57;
	public static final byte EXT_SENS_DATA_15 = 0x58;
	public static final byte EXT_SENS_DATA_16 = 0x59;
	public static final byte EXT_SENS_DATA_17 = 0x5A;
	public static final byte EXT_SENS_DATA_18 = 0x5B;
	public static final byte EXT_SENS_DATA_19 = 0x5C;
	public static final byte EXT_SENS_DATA_20 = 0x5D;
	public static final byte EXT_SENS_DATA_21 = 0x5E;
	public static final byte EXT_SENS_DATA_22 = 0x5F;
	public static final byte EXT_SENS_DATA_23 = 0x60;
	public static final byte I2C_SLV0_DO = 0x63;
	public static final byte I2C_SLV1_DO = 0x64;
	public static final byte I2C_SLV2_DO = 0x65;
	public static final byte ADDRESS = 0x68;

	
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
