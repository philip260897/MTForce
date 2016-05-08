package org.mtforce.impatouch;


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Beschreibung: Diese Klasse enthielt eine Symboldefinitionen. Ein Symbol ist durch Koordinaten definiert, welche einer 14-Segment anzeige entsprechen
 */
public class LedDigit 
{
	private String name;										//Symbolname
	private List<Point> coordinates = new ArrayList<Point>();	//Koordinaten des Symbols (Welche Segmente müssen leuchten um dieses Symbol darzustellen)
	
	public LedDigit(String name)
	{
		this.name = name;
	}
	
	/**
	 * Name des Symbols
	 * @return Gibt Name des Symbols zurück
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Fügt neue Symbolkoordinate hinzu
	 * @param x	x-Wert der Koordinate
	 * @param y z-Wert der Koordinate
	 */
	public void addCoordinate(int x, int y)
	{
		coordinates.add(new Point(x, y));
	}
	
	/**
	 * Gibt alle Koordinaten zurück
	 * @return	Array aller Koordinaten
	 */
	public Point[] getPoints() {
		return coordinates.toArray(new Point[coordinates.size()]);
	}
}
