package org.mtforce.impatouch;


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class LedDigit 
{
	private String name;
	private List<Point> coordinates = new ArrayList<Point>();
	
	public LedDigit(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addCoordinate(int x, int y)
	{
		coordinates.add(new Point(x, y));
	}
	
	public Point[] getPoints() {
		return coordinates.toArray(new Point[coordinates.size()]);
	}
}
