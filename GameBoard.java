/********************************************************
*Gameboard.java 
*
* Author: Tiffany Tse
* Creation Date: 20 May 2013  
* Last Modified: 23 May 2013
* Description:  Draws and Colors the Reversi Gameboard 
*
*               Requires: PlayPiece 
*
* Build:   javac -classpath '*':'.' Reversi.java
* Dependencies: PlayPiece, objectdraw.jar, java.awt.*
* 
* Public Methods Defined:
*           int getXloc() 
*           int getYloc() 
* 	    PlayPiece placeNewPiece()
* 	    void setColor()
* 
* Public Class Variables:
*       None
*
***********************************************************************/
import java.awt.*;
import objectdraw.*;
import java.lang.Math;

public class GameBoard 
{
	static private Color bgColor = Color.GREEN;
	static private int boardDim;
	static private int pieceDiameter;	

	static private FilledRect background;
	private FramedRect outer;
	private Line[] horizLines, vertLines;
	private DrawingCanvas myCanvas;
	
	/** Contructor **/
	public GameBoard(DrawingCanvas canvas, int dimension)
	{
		myCanvas = canvas;
		boardDim = dimension;
		horizLines = new Line[dimension];
		vertLines = new Line[dimension];
		double scale = Math.min(canvas.getWidth(), canvas.getHeight());
		pieceDiameter = (int) Math.floor(scale/dimension);

		// draw the gameboard as a single rectangle and a series of
		// Grid lines
		double width = pieceDiameter * dimension;
		double x = (canvas.getWidth() - scale)/2;
		double y = (canvas.getHeight() - scale)/2;
		background = new FilledRect(x,y, width, width, canvas);
		outer = new FramedRect(x,y, width, width ,canvas); 
		background.setColor(bgColor);
		background.sendToBack();
		for (int i = 0; i < dimension; i++)	
		{
			horizLines[i] = new Line(x,y+i*pieceDiameter, 
					x + width, y+i*pieceDiameter, canvas); 
			vertLines[i] = new Line(x + i*pieceDiameter, y,
					x+i*pieceDiameter, y + width, canvas); 
		}

	}

	/***********************
	* Method: getXloc 
        *     return the logical X coordinate of the mouse press
	*     -1 if outside of gameboard.
	***********************/
	public int getXloc(Location point)
	{
		int rval = -1;
		if (background.contains(point))
			rval = (int) Math.floor((point.getX() - background.getX())/
						pieceDiameter); 
		return rval;
	}
	/***********************
	* Method: getYloc 
        *     return the logical Y coordinate of the mouse press
	*     -1 if outside of gameboard.
	************************/
	public int getYloc(Location point)
	{
		int rval = -1;
		if (background.contains(point))
			rval = (int)  Math.floor((point.getY() - background.getY())/
						pieceDiameter); 
		return rval;
	}


	/***********************
	* Method: placeNewPiece 
        *     Put a new playboad on the board, up or down. 
	************************/
	public PlayPiece placeNewPiece(int i, int j, boolean up)
	{
		PlayPiece newPiece;
		double x = i*pieceDiameter + background.getX() + 1 ;
		double y = j*pieceDiameter + background.getY() + 1;
		
		System.out.format("Placing new piece at (%d,%d), %s\n",
				i,j, (up ? "top" : "bottom"));
 
		newPiece = new PlayPiece(new Location(x,y),pieceDiameter - 3 ,up, 
					myCanvas, boardDim);
		background.sendToBack();
		return newPiece;
	}

	/***********************
	* Method:  setColor 
        *     set the color of the current (or future) board 
	************************/
	public static void setColor(Color newColor)
	{
		bgColor = newColor;
		if (background != null)
			background.setColor(newColor);		
	}
		
}
	


