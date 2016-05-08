package org.mtforce.main;

import org.mtforce.enocean.EnOceanPi;
import org.mtforce.enocean.OceanPacket;
import org.mtforce.enocean.OceanPacketReceivedEvent;
import org.mtforce.enocean.RORGDecodeEvent;
import org.mtforce.enocean.RORGDecoder;
import org.mtforce.enocean.Response;
import org.mtforce.impatouch.LedColor;
import org.mtforce.impatouch.LedDriver;
import org.mtforce.sensors.ADC;
import org.mtforce.sensors.DOF9;
import org.mtforce.sensors.LightSensor;
import org.mtforce.sensors.Sensors;
import org.mtforce.sensors.Thermometer;
import com.pi4j.io.serial.Serial;

public class Main 
{
	//TODO: sendPacketForResponse Timeout einfuehren\
	private static boolean on = false;
	public static void main(String[] args) 
	{
		Logger.console(true);
		
		try
		{
			Sensors.initialize();
			
			final LedDriver driver = LedDriver.getInstance();
			driver.initialize();
			driver.setShutdownModeAll(LedDriver.kgsSHDM_NORM_RESET_FEAT);
			
			driver.setGlobalIntensityAll(0);
			driver.setGlobalColor(LedColor.WHITE);
			driver.setAllLedsOnAll(false);
			//driver.setGlobalIntensityAll(15);
			driver.setScanLimitAll(LedDriver.kgsSCAN_LIMIT_7);
			//driver.writeString("TEST");


			Thread ledThread = new Thread(new Runnable(){
				@Override
				public void run() 
				{
					try
					{

						
						/*for(LedColor color : LedColor.values()) {
							driver.setGlobalColor(color);
							driver.writeString("TEST");
							Thread.sleep(1000);
						}
						
						driver.setAllLedsOnAll(false);
						driver.setGlobalColor(LedColor.BLUE);
						for(int i = 65; i <= 90; i++)
						{
							driver.setGlobalColor(LedColor.values()[i%LedColor.values().length]);
							driver.writeChar((i-1)%4, (char)i);
							Thread.sleep(500);
						}
						driver.setGlobalColor(LedColor.MAGENTA);
						driver.setAllLedsOnAll(true);
						Thread.sleep(3000);
						driver.setAllLedsOnAll(false);*/
						while(true)
						{
							synchronized(LedDriver.getInstance()) {
								for(LedColor color : LedColor.values()) {
									LedDriver.getInstance().wait();
									driver.setGlobalColor(color);
									driver.setAllLedsOnAll(true);
								}
							}
						}
					}
					catch(Exception ex) 
					{
						ex.printStackTrace();
					}
				}
				
			});
			ledThread.start();
			
			Thread sensorThread = new Thread(new Runnable(){
				@Override
				public void run() {
					while(true) {
						long startMillis = System.currentTimeMillis();
						DOF9 dof = Sensors.getDof9();
						LightSensor ls = Sensors.getLightSensor();
						ADC adc = Sensors.getAdc();
						adc.setConfiguration((byte) (ADC.kgsCONF_RES_12 | ADC.kgsSTD_CONFIG));
						
						for(int i = 0; i < 100; i++)
						{
							if(dof.isEnabled()) {
								dof.getACCEL_XOUT();
								dof.getACCEL_YOUT();
								dof.getACCEL_ZOUT();
							}
							if(ls.isEnabled())
								ls.getBrightness();
						}
						System.out.println("100 Cycles in " + ((double)(System.currentTimeMillis() - startMillis))/1000d + " sec");
					}
				}
			});
			//sensorThread.start();
			
			final RORGDecoder rorgDecoder = new RORGDecoder();
		
			rorgDecoder.addRORGDecodeEventListener(new RORGDecodeEvent(){

				@Override
				public void thermometerReceived(double temperature) {
					// TODO Auto-generated method stub
					System.out.println("EnOcean Temperatur: "+temperature);
				}

				@Override
				public void buttonReceived(int button1, int button2) {
					// TODO Auto-generated method stub
					synchronized(LedDriver.getInstance())
					{
						LedDriver.getInstance().notifyAll();
					}
				}
				
			});
			
			EnOceanPi en = EnOceanPi.getInstance();
			en.init(Serial.DEFAULT_COM_PORT, 57600);
			en.addOceanPacketReceivedEvent(new OceanPacketReceivedEvent(){

				@Override
				public void packetReceived(OceanPacket packet) {
					// TODO Auto-generated method stub
					//driver.setAllLedsOnAll((on = !on));

					rorgDecoder.decode(packet.getData());
				}

				@Override
				public void responseReceived(Response response) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			/*driver.setGlobalIntensityAll(3);
			driver.setDecodeModeAll(LedDriver.kgsDM_NO_DECODE);
			driver.setScanLimitAll(LedDriver.kgsSCAN_LIMIT_7);
			driver.setGlobalColor(LedColor.RED);
			

			

			/*DOF9 dof = Sensors.getDof9();
			while(true) {
				System.out.println("Feld X: "+dof.getMAGNETO_XOUT());
				System.out.println("Feld Y: "+dof.getMAGNETO_YOUT());
				System.out.println("Feld Z: "+dof.getMAGNETO_ZOUT());
				Thread.sleep(1000);
			}*/

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
