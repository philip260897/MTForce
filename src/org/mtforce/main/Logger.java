package org.mtforce.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Logger {

	public enum Status {
	    WARNING("WARNING"),
	    ERROR("ERROR"), MESSAGE("MESSAGE");

	    private final String text;

	    private Status(final String text) {
	        this.text = text;
	    }
	    @Override
	    public String toString() {
	        return text;
	    }
	}
	static List<String> myList = new ArrayList<String>();
	boolean console = false;
	
	public void console(boolean onoff)
	{
		if(onoff)
		{
			console = true;
		}else
		{
			console = false;
		}
		
	}

	public void log(Status status, String prefix, String Message){
		String logString = "[" + prefix + ":" + status + "] \t" + Message;
		myList.add(logString);
		
		if(console)
		{
			System.out.println(logString);
		}
		
	}
	public void log(String prefix, String Message){
		String logString = "[" + prefix + ":" + Status.MESSAGE + "]\t" + Message;
		myList.add(logString);
		
		if(console)
		{
			System.out.println(logString);
		}
		
	}
	public static void save() throws IOException{
		FileWriter fw;
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		fw = new FileWriter("C:\\Users\\Ben\\log_" + timeStamp + ".txt");
		BufferedWriter bw = new BufferedWriter(fw);
		for (String temp : myList) {
			bw.write(temp);
			bw.newLine();
		}	    
	    bw.close();
	}
}
