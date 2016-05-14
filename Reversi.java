/********************************************************
* Reversi.java 
*
* Author: Tiffany Tse
* Creation Date: 20 May 2013  
* Last Modified: 23 May 2013
* Description:  Plays the game of Reversi 
*
*               Requires: GameBoard and PlayPiece 
*
* Build:   javac -classpath '*':'.' Reversi.java
* Dependencies:  GameBoard, PlayPiece, objectdraw.jar, java.awt.*
* 
* Public Methods Defined:
*           begin ()
*           void actionPerformed()
* 	    void beginGame() 
* 	    void onMouseClick() 
* 	    int doReversi() 
*           int scan()
* 
* Public Class Variables:
*       None
*
***********************************************************************/
import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// display text field and copy contents to canvas when clicked
public class Reversi extends WindowController implements ActionListener {
	private JTextField input;
	private JLabel dimension, score, player;
	private JButton buttonScore, buttonNew, buttonPass;
	private JPanel buttonPanel;
	private JPanel topPanel;
	private JPanel playerPanel;
	private JPanel colorPanel;
	
	// private JComboBox<String> colorScheme;
	private JComboBox colorScheme;
	 
	private GameBoard gameboard;
	private PlayPiece[][] tokens;
	private boolean firstPlayer = true;
	private int boardDim;

	/*******************
	* Method - begin()
	*  	set up the  graphical interface
	*       and component listeners
	*********************/
	public void begin() {
		// construct text field
		input = new JTextField( "10" );  
		player = new JLabel( "Player 1" );  
		dimension = new JLabel( "Grid Size" );  
		score = new JLabel( 
			new String().format("Player 1: %3d  Player 2: %3d",0,0 ));  
		
		  
		// Add text field to content pane of window
		buttonScore = new JButton("Calculate Score");
		buttonNew = new JButton("New Game");
		buttonPass = new JButton("Pass");
		buttonScore.addActionListener(this);
		buttonNew.addActionListener(this);
		buttonPass.addActionListener(this);

		// Create the colorScheme ComboBox
		//colorScheme = new JComboBox<String>();
		colorScheme = new JComboBox();
		colorScheme.addItem("Classic");
		colorScheme.addItem("Triton");
		colorScheme.addItem("Pastel");
		colorScheme.addActionListener(this);

		// Create a Button Panel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(buttonScore);
		buttonPanel.add(buttonNew);

		// Create a Top Panel
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		topPanel.add(dimension);
		topPanel.add(input);
		topPanel.add(score);

		// Create a Player Panel
		playerPanel = new JPanel();
		playerPanel.setLayout(
			new BoxLayout(playerPanel,BoxLayout.Y_AXIS));
		playerPanel.add(player);
		playerPanel.add(buttonPass);

		// Create a colorPanel
		colorPanel = new JPanel();
		colorPanel.setLayout(
			new BoxLayout(colorPanel,BoxLayout.Y_AXIS));
		colorPanel.add(colorScheme);

		// add the panels to the main window
		Container contentPane = getContentPane();
		contentPane.add (topPanel, BorderLayout.NORTH);
		contentPane.add (buttonPanel, BorderLayout.SOUTH);
		contentPane.add (playerPanel, BorderLayout.EAST);
		contentPane.add (colorPanel, BorderLayout.WEST);

		contentPane.validate();
	 }
	 
	/*******************
	* Method - actionPerformed()
	*       Event listeners for button pushes, combobox, etc.
	*********************/
	public void actionPerformed (ActionEvent evt )
	{

		// Manage the color scheme 
		if (evt.getSource() == colorScheme )
		{
			Object menuChoice = colorScheme.getSelectedItem();
			if (menuChoice.equals("Classic") )
			{
				GameBoard.setColor(Color.GREEN);
				PlayPiece.setTopColor(Color.BLACK);
				PlayPiece.setBottomColor(Color.WHITE);
			}
			else if (menuChoice.equals("Triton") )
			{
				GameBoard.setColor(Color.WHITE);
				PlayPiece.setTopColor(Color.BLUE);
				PlayPiece.setBottomColor(Color.YELLOW);
			}
			else if (menuChoice.equals("Pastel") )
			{
				GameBoard.setColor(new Color(255,255,204));
				PlayPiece.setTopColor(new Color(255,102,255));
				PlayPiece.setBottomColor(new Color(255,255,102));
			}
		}
		// Start a new Game
		else if (evt.getSource() == buttonNew ) 
		{
			PlayPiece.clearPieces();
			canvas.clear();
			boardDim = Integer.parseInt(input.getText().trim());
			gameboard = new GameBoard(canvas, boardDim);
			tokens = new PlayPiece[boardDim][boardDim];
			for (int i = 0; i < boardDim; i++)
				for (int j = 0; j < boardDim; j++)
					tokens[i][j] = null;
			beginGame();
		}
		// Score the game
		else if (evt.getSource() == buttonScore ) 
		{
			int score1=0, score2=0;
			PlayPiece piece;
			if (tokens == null) return;
			for (int i = 0; i < boardDim; i++)
				for (int j = 0; j < boardDim; j++)
					if ((piece = tokens[i][j]) != null)
						if (piece.topShowing())
							score1++;
						else
							score2++;
	
			score.setText(
				new String().format("Player 1: %3d  Player 2: %3d",
					score1, score2 ));  
					
		}
		// Pass to the next player
		else if (evt.getSource() == buttonPass ) 
		{
			firstPlayer = !firstPlayer;
			if (firstPlayer) 
				player.setText("Player 1"); 
			else
				player.setText("Player 2"); 
		}
	}

	/*******************
	* Method - beginGame() 
	*       Start a new game 
	*********************/
	// Begin Game
	// Place the piece appropriately
	private void beginGame()
	{
		boolean top = true;
		for (int i = boardDim/2-1; i <=boardDim/2; i ++)
		{
			for (int j = boardDim/2 - 1; j <=boardDim/2; j ++)
			{
				tokens[i][j] = gameboard.placeNewPiece(i,j,top);
				top = !top;
			}
			top = !top;
		}
		player.setText("Player 1");
	}

	/*******************
	* Method - onMouseClick() 
	*      	 what to do when the mouse is clicked on the gameboard 
	*********************/
	public void onMouseClick( Location point )
	{
		if (gameboard != null)
		{
			int i = gameboard.getXloc(point);
			int j = gameboard.getYloc(point);
			// System.out.format("Clicked at %d, %d\n",i, j); 
			if ( i >=0 && j >= 0 && tokens[i][j] == null)
			{
				if ( doReversi(i,j, firstPlayer) == 0) return;

				tokens[i][j] = gameboard.placeNewPiece(i,j,firstPlayer);
				firstPlayer = !firstPlayer;
				if (firstPlayer) 
					player.setText("Player 1"); 
				else
					player.setText("Player 2"); 
					
			}
		}
	}

	/*******************
	* Method - int doReversi() 
	*	return # of reverses when playing a piece at a location
	*       return 0 if no valid reverses (illegal move)
	*********************/
	private int doReversi(int x, int y, boolean topOrBottom)
	{
		int reverses = 0;
		for (int incX = -1; incX <= 1; incX ++)
		{
			for (int incY = -1; incY <= 1; incY ++)
			{
				if (! (incX == 0 && incY == 0) )
					reverses += 
						scan(x,y,incX,incY,topOrBottom);
			}
		}
		return reverses;
	}

	/**************************************
	* Method int scan()
	* Scan a "row" 
	*          return number of reverses
	* start at x,y 
	* 
	* incX:  (-1,0,1) (scan left, up/down, scan right)
	* incY:  (-1,0,1) (scan up, left/right, down) 
	* ex: scan left: scan(x, y, -1, 0)
	*     scan up: scan(x,y,0,-1)
	*     scan down right diagonal: scan (x,y,1,1)
	************************************************/
	private int scan(int x,int y,int incX,int incY, boolean topOrBottom)
	{

		boolean reverse = false;
		boolean other = false; // Means other player/color
		boolean closed = false; // Means the line is closed 
		PlayPiece testToken;
		int reverses = 0; 
		int i = x + incX;
		int j = y + incY;
		
		// Scan a line to see if we have a valid closed line. 
		// It doesn't matter which direction we are scanning,
		// This insures we stay on the board
		while ( i >=0  && i < boardDim && j >= 0 && j < boardDim)
		{
			testToken = tokens[i][j];	
			if (testToken == null) break;
			if (testToken.topShowing() == !topOrBottom) 
				other = true;
	  		else if (testToken.topShowing() == topOrBottom) 
			{
				closed = true;
				break;
			}
			i += incX;
			j += incY;
		}
	
		// Check if a valid line, so flipping line, Start
                // at i, j calculated above and flip pieces
		if (other && closed)
		{
			int flipX = i - incX;
			int flipY = j - incY;
			while ( !(flipX == x && flipY == y) )
			{
				reverses ++;
				tokens[flipX][flipY].flipPiece();
				System.out.format("flip, i = %d, j = %d\n", flipX,flipY);
				flipX -= incX;
				flipY -= incY;
			}
		}
		return reverses;
	}

	// Main program
	public static void main(String[] args) 
	{
		new Reversi().startController(600,600);
	}
}
