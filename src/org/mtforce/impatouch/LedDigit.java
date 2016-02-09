package org.mtforce.impatouch;


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Beschreibung: Diese Klasse enthaelt eine Symboldefinitione. Ein Symbol ist durch koordinaten definiert, welche einer 14-Segment anzeige entsprechen
 * 	
 * 
 * Konstanten: NICHT Komplett
 * Funktionen: NICHT Komplett
 * 
 * TODO: Alle Sensoren hinzufuegen, getter und setter, Modultest
 */
public class LedDigit 
{
	private String name;										//Symbolname
	private List<Point> coordinates = new ArrayList<Point>();	//Koordinaten des Symbols (Welche Segmente muessen leuchten um dieses Symbol darzustellen)
	
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
