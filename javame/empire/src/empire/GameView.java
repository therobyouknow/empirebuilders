package empire;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;


class GameView extends Canvas 
{
  private GameModel model;
  private Empire empire;	
  private Display display;
  private CommandListener listener;

  // declare variables to hold game objects
  Image sea           = null;
  Image land          = null;
  Image player1marker = null;
  Image player2marker = null;
  Image player1land   = null;
  Image player2land   = null;

  // game view width and height in pixels
  int width = 0;
  int height = 0;

  int gameObjectWidth = 0;
  int gameObjectHeight = 0;

  int scoreLineHeight = 0;

  int numberOfScoreLines = 2;

  Font scoreLineFont = Font.getFont( Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL );


public static Command nextPlayerCommand;


  /**
   * Construct a new canvas
   */
  public GameView(Empire empire) 
  {
    this.empire = empire;
    display = Display.getDisplay(empire);
  }

  public GameModel getGameModel() { return model; }
  public void setGameModel( GameModel model ) { this.model = model; } 

  public void init() 
  {
    width = getWidth();
    height = getHeight();

    scoreLineHeight = scoreLineFont.getHeight();

    loadGameObjects();

    // work out how many game objects can fit onto the screen

    // all game objects should be the same dimension, so use the
    // sea object (for example) to note the general game object dimensions
    gameObjectWidth = sea.getWidth();
    gameObjectHeight = sea.getHeight();
    int h = (height - scoreLineHeight * numberOfScoreLines ) / gameObjectHeight;
    int w = width / gameObjectWidth;
    model.setDimensions( w, h );
   
    model.generateLandscape();
    model.positionPlayers();
    model.start();
    

    repaint();
  }
	
  /**
    * Cleanup and destroy.
    */
  public void destroy() 
  {
  }

  private void loadGameObjects()
  {    
    // determine whether display is monochrome or colour
    // and select appropriate bitmaps to suit
    String colourMode = null;
    if (display.numColors() > 2)
    {
      colourMode = "colour";
    }
    else
    {
      colourMode = "mono";
    }
    String gameObjectsPath = "/gameobjects/" + colourMode + "/";
    
    try 
    {
      sea           = Image.createImage( gameObjectsPath + "sea.png" );
      land          = Image.createImage( gameObjectsPath + "land.png" );
      player1marker = Image.createImage( gameObjectsPath + "p1marker.png" );
      player2marker = Image.createImage( gameObjectsPath + "p2marker.png" );
      player1land   = Image.createImage( gameObjectsPath + "p1land.png" );
      player2land   = Image.createImage( gameObjectsPath + "p2land.png" );
    }
    catch (java.io.IOException x)
    {
      // flash up error message on mobile, then exit gracefully
    }
  }


    /*
     * Handle a repeated arrow keys as though it were another press.
     * @param keyCode the key pressed.
     */
    protected void keyRepeated(int keyCode) {
        int action = getGameAction(keyCode);
        switch (action) {
        case Canvas.LEFT:
        case Canvas.RIGHT:
        case Canvas.UP:
        case Canvas.DOWN:
            keyPressed(keyCode);
	    break;
        default:
            break;
        }
    }


  public void keyPressed(int keyCode) 
  {

      int action = getGameAction(keyCode);
      int move = 0;
      switch (action)
      { 
	  case Canvas.LEFT:
	    move = GameModel.LEFT;
	    break;
	    
        case Canvas.RIGHT:
	    move = GameModel.RIGHT;
	    break;
	  
        case Canvas.DOWN:
	    move = GameModel.DOWN;
	    break;
	  
        case Canvas.UP:
	    move = GameModel.UP;
	    break;

	  default:
	    return;
	}

	model.move( move );

      synchronized(model) 
      {
	repaint();

      } 

  }
    
  public void nextPlayer( Player currentPlayer )
  {
	if (listener != null) 
{
		    listener.commandAction( nextPlayerCommand, this);
		}
  }




    /**
     * Called when the pointer is pressed. 
     * @param x location in the Canvas
     * @param y location in the Canvas
     */
    protected void pointerPressed(int x, int y) {

    }

    /**
     * @param l the object implementing interface CommandListener
     */
    public void setCommandListener(CommandListener l) {
	super.setCommandListener(l);
        listener = l;
    }



 
  public void paint(Graphics g) 
  {
    synchronized( model )
    {
      // get width and height in units of game objects
      int mw = model.getWidth();
      int mh = model.getHeight();

      Image imageToDraw = null;
      for ( int x = 0; x < mw; x++ )
      {
        for ( int y = 0; y < mh; y++  )
        {
          switch ( model.getGameObjectAt( x, y ) )
          {
            case GameModel.SEA: imageToDraw = sea; break;
            case GameModel.LAND: imageToDraw = land; break;
            case (GameModel.LAND   | GameModel.PLAYER1): imageToDraw = player1land; break;
            case (GameModel.LAND   | GameModel.PLAYER2): imageToDraw = player2land; break;
            default:
          }

          g.drawImage( imageToDraw, x * gameObjectWidth, y * gameObjectHeight, 0 );
        }
      }
    }

    // plot players on screen
    Player player1 = model.getPlayer1();
    Player player2 = model.getPlayer2();

    g.drawImage( player1marker, player1.getX() * gameObjectWidth, player1.getY() * gameObjectWidth, 0 );
    g.drawImage( player2marker, player2.getX() * gameObjectWidth, player2.getY() * gameObjectWidth, 0 );

    Player currentPlayer = model.getCurrentPlayer();
    g.setColor( 0xFFFFFF );
    g.fillRect( 0, height-scoreLineHeight * numberOfScoreLines, width, scoreLineHeight * numberOfScoreLines );
    g.setColor( 0x000000 );
    g.drawString( "Player " + currentPlayer.getNumber() + ": " + currentPlayer.getScore(),
                  0, height-scoreLineHeight*2,
                  Graphics.TOP|Graphics.LEFT
                );
    g.drawString( "Moves Left: " + currentPlayer.getNumberOfMoves(),
                  0, height-scoreLineHeight,
                  Graphics.TOP|Graphics.LEFT
                ); 
  }
} // end of class