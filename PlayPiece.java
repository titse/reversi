/********************************************************
* PlayPiece.java 
*
* Author: Tiffany Tse
* Creation Date: 20 May 2013  
* Last Modified: 23 May 2013
* Description:  Draws, Colors, flips a reversi game piece
*
*
* Build:   javac -classpath '*':'.' Reversi.java
* Dependencies: objectdraw.jar, java.awt.*
* 
* Public Methods Defined:
*           flipPiece() 
*           setTopColor() 
*           setBottomColor() 
*           boolean topShowing()
*           clearPieces
* 
* Public Class Variables:
*       None
*
***********************************************************************/
import java.awt.*;
import objectdraw.*;
public class PlayPiece
{
	static private Color topColor = Color.BLACK, bottomColor=Color.WHITE;
	static private PlayPiece[] allPieces;
	static private int maxPieces,inPlay=0;

	private FilledOval top, bottom;
	private boolean topIsUp;

	/******************************
	 * Constructor
	*******************************/
	public PlayPiece(Location where, int Diameter, boolean up, 
			DrawingCanvas canvas, int boardDimension) 
	{
		if (allPieces == null) 
		{
			maxPieces = boardDimension*boardDimension;
			allPieces = new PlayPiece[boardDimension * boardDimension];
		}
		if (inPlay < maxPieces)
		{
			top = new FilledOval(where, Diameter, Diameter,canvas);
			bottom = new FilledOval(where, Diameter, Diameter,canvas);
			top.setColor(topColor);
			bottom.setColor(bottomColor);
			allPieces[inPlay] = this;
			inPlay++;

			topIsUp = up;
			if (topIsUp)
				top.sendToFront();
			else
				bottom.sendToFront();
		}
	}

	/*****************************
	 * method: boolean topShowing()
	 *     is top up
	 ******************************/
	public boolean topShowing()
	{
		return topIsUp;
	}

	/*******************************
	 * method: flipPiece()
	 *******************************/
	public void flipPiece()
	{
		topIsUp = !topIsUp;
		if (topIsUp)
			top.sendToFront();
		else
			bottom.sendToFront();
	}
		
	/*****************************
	 * Color set and get Methods
	 *****************************/
	public static Color getTopColor() {
		return topColor;
	}	
	public static Color getBottomColor() {
		return bottomColor;
	}	

	/**********
	 * Set Color for all existing pieces
	 * All new pieces will have the new color schemes applied
	 **********/
	public static void setTopColor(Color newColor) {
		topColor = newColor;
		if (allPieces == null) return;
		for (int i = 0; i < inPlay; i++)
			allPieces[i].top.setColor(topColor);
	}	
	public static void setBottomColor(Color newColor) {
		bottomColor = newColor;
		if (allPieces == null) return;
		for (int i = 0; i < inPlay; i++)
			allPieces[i].bottom.setColor(bottomColor);
	}	
	/**************
	 * Clear Pieces
	 **************/
	public static void clearPieces()
	{
		if (allPieces == null) return;
		for (int i = 0; i < inPlay; i++)
		{
			allPieces[i].bottom.removeFromCanvas();
			allPieces[i].top.removeFromCanvas();
			allPieces[i] = null;
		}
		inPlay = 0;
	}
	
	
}	
