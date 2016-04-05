package org.mtforce.main;

import org.mtforce.enocean.EnOceanPi;
import org.mtforce.enocean.OceanPacket;
import org.mtforce.enocean.OceanPacketReceivedEvent;
import org.mtforce.enocean.RORGDecodeEvent;
import org.mtforce.enocean.RORGDecoder;
import org.mtforce.enocean.Response;
import org.mtforce.impatouch.LedColor;
import org.mtforce.impatouch.LedDriver;
import org.mtforce.sensors.Sensors;
import org.mtforce.sensors.Thermometer;

import com.pi4j.io.serial.Serial;

public class Main 
{
	//TODO: sendPacketForResponse Timeout einfuehren\
	
	public static void main(String[] args) 
	{
		Logger.console(true);
		
		try
		{
			Sensors.initialize();
			
			LedDriver driver = LedDriver.getInstance();
			
			driver.initialize();
			driver.setGlobalColor(LedColor.CYAN);
			driver.setAllLedsOnAll(false);
			driver.setGlobalIntensityAll(15);
			driver.writeString("TEST");
			
			for(LedColor color : LedColor.values()) {
				driver.setGlobalColor(color);
				driver.writeString("TEST");
				Thread.sleep(0);
			}
			
			driver.setAllLedsOnAll(false);
			driver.setGlobalColor(LedColor.BLUE);
			for(int i = 65; i <= 90; i++)
			{
				driver.setGlobalColor(LedColor.values()[i%LedColor.values().length]);
				driver.writeChar((i-1)%4, (char)i);
				Thread.sleep(200);
			}
			//driver.setAllLedsOnAll(false);
			driver.sendTest();
			LedDriver driver2 = LedDriver.getInstance();
			System.out.println(driver2 == driver);
			
			final RORGDecoder decoder = new RORGDecoder();
			decoder.addRORGDecodeEventListener(new RORGDecodeEvent(){
				@Override
				public void thermometerReceived(double temperature) {
					System.out.println("EnOcean Thermometer: "+temperature);
				}

				@Override
				public void buttonReceived(int button1, int button2) {
					System.out.println("Button1: "+button1);
					System.out.println("Button2: "+button2);
				}			
			});
			
			EnOceanPi pi = EnOceanPi.getInstance();
			pi.init(Serial.DEFAULT_COM_PORT, 57600);
			if(pi.isEnabled())
			{
				pi.addOceanPacketReceivedEvent(new OceanPacketReceivedEvent()
				{
					@Override
					public void packetReceived(OceanPacket packet) {
						packet.println();
						
						decoder.decode(packet.getData());
					}

					@Override
					public void responseReceived(Response response) {
						
					}
				});
				
				OceanPacket packet = new OceanPacket();
				packet.setPacketType(EnOceanPi.PACKETTYPE_COMMON_COMMAND);
				packet.setData((byte)0x02);
				packet.setDataOptional(null);
				packet.generateHeader();
				packet.println();
				Response resp = pi.sendPacketForResponse(packet);
			}
			
			
			//driver.setAllLedsOnAll(false);
	
			/*int nprom[] = {0x3132,0x3334,0x3536,0x3738,0x3940,0x4142,0x4344,0x4500}; 
			Sensors.getBarometer().checkCRC(nprom, 0x4500);
			Barometer bar = Sensors.getBarometer();
			if(bar.isEnabled())
			{
				System.out.println("Temperatur: "+bar.getTemperature());
			}
			
			/*ADC adc = Sensors.getAdc();
			if(adc.isEnabled())
			{
				//adc.setConfiguration(ADC.kgsSTD_CONFIG);
				adc.setConfiguration((byte)(ADC.kgsCONF_SELECT_CH4 | ADC.kgsSTD_CONFIG | ADC.kgsCONF_RES_12));
				Thread.sleep(100);
				//System.out.println(Utils.byteToHexString((byte)adc.getChannelSelection()));
				System.out.println(adc.getChannelSelection() == ADC.kgsCONF_SELECT_CH4 ? "OK!" : "FAILED!");
				System.out.println("Voltage: "+adc.getVoltage());
			}*/
			
			/*DOF9 dof = Sensors.getDof9();
			if(dof.isEnabled())
			{
				for(int i = 0; i < 10000; i++) {
					//System.out.println(dof.getGYRO_XOUT() + " " + dof.getGYRO_YOUT() + " " + dof.getGYRO_ZOUT());
					System.out.println(dof.getACCEL_XOUT() + " " + dof.getACCEL_YOUT() + " " + dof.getACCEL_ZOUT());
					Thread.sleep(50);
				}
			}*/
			
			//testThermometer();
			
			/*LightSensor s = Sensors.getLightSensor();
			while(true)
			{
				System.out.println(s.getBrightness());
				Thread.sleep(1000);
			}*/
			
			//System.in.read();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private static void testThermometer()
	{
		Logger.log("Thermometer", "Checking availability...", false);
		Thermometer therm = Sensors.getThermometer();
		if(therm.isEnabled())
		{
			System.out.println("OK!");
			
			therm.setConfiguration(Thermometer.kgsCONF_DEFAULT);
			Logger.log("Thermometer", "Testing configuration ALERT_CNT...", false);
			therm.setConfiguration(Thermometer.kgsCONF_ALERT_CNT);
			System.out.println(therm.isAlertOutputControlSet() ? "OK!" : "FAILED!");
			
			Logger.log("Thermometer", "Testing configuration ALERT_MOD...", false);
			therm.setConfiguration(Thermometer.kgsCONF_ALERT_MOD);
			System.out.println(therm.isAlertOutputModeSet() ? "OK!" : "FAILED!");
			
			Logger.log("Thermometer", "Testing configuration ALERT_POL...", false);
			therm.setConfiguration(Thermometer.kgsCONF_ALERT_POL);
			System.out.println(therm.isAlertOutputPolaritySet() ? "OK!" : "FAILED!");
			
			Logger.log("Thermometer", "Testing configuration ALERT_SEL...", false);
			therm.setConfiguration(Thermometer.kgsCONF_ALERT_SEL);
			System.out.println(therm.isAlertOutputSelectSet() ? "OK!" : "FAILED!");
			
			Logger.log("Thermometer", "Testing configuration HYSTERESIS...", false);
			therm.setConfiguration(Thermometer.kgsCONF_HYST_15);
			System.out.println(therm.getHysteresis() == therm.kgsCONF_HYST_15 ? "OK!" : "FAILED!");
			
			therm.setConfiguration(Thermometer.kgsCONF_ALERT_MOD);
			Logger.log("Thermometer", "Testing configuration ALERT_STAT function...", false);
			double temp = therm.getTemperature();
			therm.setTCritical(((int)temp) - 2);
			System.out.println((!therm.isAlertOutputStatusSet() ? "OK!" : "FAILED!"));
			
			therm.setConfiguration(Thermometer.kgsCONF_DEFAULT);
			
			Logger.log("Thermometer", "Testing resolution register...", false);
			therm.setResolution(Thermometer.kgsRES_5);
			System.out.println(therm.getResolution() == Thermometer.kgsRES_5 ? "OK!" : "FAILED!");
			
			Logger.log("Thermometer", "Testing TCritical register...", false);
			therm.setTCritical(20);
			System.out.println(therm.getTCriticalLimit() == 20 ? "OK!" : "FAILED!");
			
			Logger.log("Thermometer", "Testing TUpper register...", false);
			therm.setTUpperLimit(20);
			System.out.println(therm.getTUpperLimit() == 20 ? "OK!" : "FAILED!");
			
			Logger.log("Thermometer", "Testing TLower register...", false);
			therm.setTLowerLimit(20);
			System.out.println(therm.getTLowerLimit() == 20 ? "OK!" : "FAILED!");
			
			Logger.log("Thermometer", "Testing TCritical function...", false);
			temp = therm.getTemperature();
			boolean succ = false;
			therm.setTCritical(((int)temp) - 2);
			if(therm.isTemperatureCritical())
				succ = true;
			therm.setTCritical(((int)temp) + 2);
			System.out.println((!therm.isTemperatureCritical() && succ ? "OK!" : "FAILED!"));
			
			Logger.log("Thermometer", "Testing TUpper function...", false);
			temp = therm.getTemperature();
			succ = false;
			therm.setTUpperLimit(((int)temp) - 2);
			if(therm.isTemperatureUpper())
				succ = true;
			therm.setTUpperLimit(((int)temp) + 2);
			System.out.println((!therm.isTemperatureUpper() && succ ? "OK!" : "FAILED!"));
			
			Logger.log("Thermometer", "Testing TLower function...", false);
			temp = therm.getTemperature();
			succ = false;
			therm.setTLowerLimit(((int)temp) + 2);
			if(therm.isTemperatureLower())
				succ = true;
			therm.setTLowerLimit(((int)temp) - 2);
			System.out.println((!therm.isTemperatureLower() && succ ? "OK!" : "FAILED!"));
		}
		else
		{
			System.out.println("FAILED!");
		}
	}
}
