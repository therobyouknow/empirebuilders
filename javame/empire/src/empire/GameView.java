package empire;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;


class GameView extends Canvas implements CommandListener
{
  private GameLogic gameLogic;
  private Landscape landscape;
  private Empire empire;	
  private Display display;
  private CommandListener listener;

  // declare variables to hold game objects
  Image sea           = null;
  Image land          = null;

  // game view width and height in pixels
  int width = 0;
  int height = 0;
  int landscapePixelOffsetX = 0;
  int landscapePixelOffsetY = 0;

  // size of game objects on screen
  int gameObjectWidth = 0;
  int gameObjectHeight = 0;

  // score display variables
  int scoreLineHeight = 0;
  int numberOfScoreLines = 2;
  Font scoreLineFont = Font.getFont( Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL );

  // scroll bars variables
  int scrollbarThickness = 0; int scrollbarLength = 0;
  Scrollbar verticalScrollbar = null;
  Scrollbar horizontalScrollbar = null;

  // landscape window
  int landscapeWindowX = 0;
  int landscapeWindowY = 0;
  int landscapeWindowSizeX = 0;
  int landscapeWindowSizeY = 0;

  private Command exitCommand = new Command("Exit", Command.EXIT, 60);
  private Command subMenuCommand = new Command("Menu", Command.HELP, 30);

  // use array and initialise object to be used and reused to be deterministic
  private final int maxQueueLength = 10;
  private final int queueStart = 0;
  private GameObjectQueueItem paintQueue[] = new GameObjectQueueItem[maxQueueLength];
  private int queuePosition = queueStart;
  private boolean repaintWholeScreen = false;

  //
  private static int MODE_MOVE_PLAYER = 0;
  private static int MODE_BROWSE_LANDSCAPE = 1;
  int mode = MODE_MOVE_PLAYER;
  
  public void queueGameObjectDraw( int x, int y, Image gameObject )
  {
/*
    if ( queuePosition < maxQueueLength )
    {
      (paintQueue[queuePosition]).set( x, y, gameObject ); paintQueue++;
    }
    else
    {
      repaintWholeScreen = true;
      queuePosition = queueStart;
    }
*/
  }


//public static Command nextPlayerCommand;


  /**
   * Construct a new canvas
   */
  public GameView(Empire empire) 
  {
    this.empire = empire;
    display = Display.getDisplay(empire);
    this.addCommand(subMenuCommand);
    this.addCommand(exitCommand);
  }

  public GameLogic getGameLogic() { return gameLogic; }
  public void setGameLogic( GameLogic gameLogic ) { this.gameLogic = gameLogic; } 
  public void setLandscape( Landscape landscape ) { this.landscape = landscape; }

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

    int scrollbarThickness = 11; 

    landscapePixelOffsetX = scrollbarThickness + 1;
    landscapePixelOffsetY = scrollbarThickness + 1;
    int horizontalScrollbarLength = width - landscapePixelOffsetX;
    
    landscapeWindowSizeY = (height - (scoreLineHeight * numberOfScoreLines) - landscapePixelOffsetY ) / gameObjectHeight;
    landscapeWindowSizeX = (width - landscapePixelOffsetX) / gameObjectWidth;

    landscapeWindowX = landscapeWindowSizeX; landscapeWindowY = 0;

    int landscapeSizeX = landscapeWindowSizeX * 5; int landscapeSizeY = landscapeWindowSizeY * 5;

    landscape.setDimensions( landscapeSizeX, landscapeSizeY );

    int verticalScrollbarLength = landscapeWindowSizeY * gameObjectHeight;
    verticalScrollbar = new Scrollbar( 0, scrollbarThickness, scrollbarThickness, verticalScrollbarLength, Scrollbar.VERTICAL,
                                       landscapeWindowY, landscapeWindowSizeY, 0, landscapeSizeY );

    horizontalScrollbar = new Scrollbar( scrollbarThickness, 0, scrollbarThickness, horizontalScrollbarLength, Scrollbar.HORIZONTAL,
                                         landscapeWindowX, landscapeWindowSizeX, 0, landscapeSizeX );

    landscape.generate();
    gameLogic.positionPlayers();
    gameLogic.start();
    
    repaint();
  }
	
  /**
    * Cleanup and destroy.
    */
  public void destroy() {}

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

      Player player1 = gameLogic.getPlayer( 0 );
      Player player2 = gameLogic.getPlayer( 1 );

      player1.setSymbol( Image.createImage( gameObjectsPath + "p1marker.png" ) );
      player2.setSymbol( Image.createImage( gameObjectsPath + "p2marker.png" ) );

      player1.setClaimedLandscape( Image.createImage( gameObjectsPath + "p1land.png" ) );
      player2.setClaimedLandscape( Image.createImage( gameObjectsPath + "p2land.png" ) );
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
  protected void keyRepeated(int keyCode) 
  {
    int action = getGameAction(keyCode);
    switch (action) 
    {
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

    if ( mode == MODE_MOVE_PLAYER )
    {
    switch (action)
    { 
      // translate up,down,left,right moves to moves on
      // game landscape

      case Canvas.LEFT:
	  move = GameLogic.LEFT;
        System.out.println(">left");
	  break;
  
      case Canvas.RIGHT:
	  move = GameLogic.RIGHT;
        System.out.println(">right");
	  break;
	  
      case Canvas.DOWN:
	  move = GameLogic.DOWN;
        System.out.println(">down");
	  break;
	  
      case Canvas.UP:
	  move = GameLogic.UP;
        System.out.println(">up");
	  break;

      case Canvas.FIRE:
        mode = MODE_BROWSE_LANDSCAPE;
        return;

      default:
    }
    gameLogic.move( move );
    }
    else
    {
     // translate up,down,left,right moves to moves on
      // game landscape
    switch (action)
    { 
      case Canvas.LEFT:
        if ( (landscapeWindowX - landscapeWindowSizeX) >= 0 ) 
        {
          landscapeWindowX -= landscapeWindowSizeX;
          System.out.println(">left");
        }
	  break;
  
      case Canvas.RIGHT:
        if ( (landscapeWindowX + landscapeWindowSizeX) < landscape.getWidth()  ) 
        {
          landscapeWindowX += landscapeWindowSizeX;
          System.out.println(">right");
        }
	  break;
	  
      case Canvas.DOWN:
        if ( (landscapeWindowY + landscapeWindowSizeY) < landscape.getHeight() )
        {
          landscapeWindowY += landscapeWindowSizeY;
          System.out.println(">down");
        }
	  break;
	  
      case Canvas.UP:
	  if ( (landscapeWindowY - landscapeWindowSizeY) >= 0  )
        {
          landscapeWindowY -= landscapeWindowSizeY;
          System.out.println(">up");
        }
	  break;

      case Canvas.FIRE:
        mode = MODE_MOVE_PLAYER;
        return;

      default:
    }
    horizontalScrollbar.setValue( landscapeWindowX );
    verticalScrollbar.setValue( landscapeWindowY );
    }

    synchronized(gameLogic) 
    {
	repaint();
    } 
  }
   
  public void commandAction(Command c, Displayable s) 
  {
    if ( c == exitCommand )
    {
      System.out.print("exit");
      empire.exit();
    }
    else if ( c == subMenuCommand )
    {
    }
  }
 
  // inform user that it is next player's go 
  public void nextPlayer( Player currentPlayer )
  {
    empire.showNextPlayer();
  }

  // Called when the pointer is pressed (for devices with pointing device)
  // not used for this game
  protected void pointerPressed(int x, int y) {}

  public void paint(Graphics g) 
  {
    synchronized( gameLogic )
    {
      horizontalScrollbar.draw( g );
      verticalScrollbar.draw( g );

      System.out.println( "lwsx: " + landscapeWindowSizeX );
  
      // get width and height in units of game objects
      int mw = landscapeWindowSizeX; //landscape.getWidth();
      int mh = landscapeWindowSizeY; //landscape.getHeight();

      Image imageToDraw = null;
      for ( int x = 0; x < mw; x++ )
      {
        for ( int y = 0; y < mh; y++  )
        {
          if ( landscape.isSea( landscapeWindowX + x, landscapeWindowY + y ) )
          {
            imageToDraw = sea;
          }
          else if ( landscape.isLand( landscapeWindowX + x, landscapeWindowY + y ) )
          {
            Player player = null;
            if ( landscape.isClaimed( landscapeWindowX + x, landscapeWindowY + y ) )
            {
              imageToDraw = (gameLogic.getPlayer(landscape.getPlayerWhoClaimed( landscapeWindowX + x, landscapeWindowY + y )-1)).getClaimedLandscape();
            }
            else
            {
              imageToDraw = land;
            }
          }      

          g.drawImage( imageToDraw, landscapePixelOffsetX + x * gameObjectWidth, landscapePixelOffsetY + y * gameObjectHeight, 0 );
        }
      }
    }

    for ( int i = 0; i < gameLogic.getNumberOfPlayers(); i++ )
    {
      Player player = gameLogic.getPlayer( i );
      if (    player.getX() >= landscapeWindowX && player.getX() < (landscapeWindowX + landscapeWindowSizeX )
           && player.getY() >= landscapeWindowY && player.getY() < (landscapeWindowY + landscapeWindowSizeY )
         )
      {
        g.drawImage( player.getSymbol(), 
                     landscapePixelOffsetX + ( player.getX() - landscapeWindowX ) * gameObjectWidth,
                     landscapePixelOffsetY + ( player.getY() - landscapeWindowY ) * gameObjectWidth, 
                     0 );
      }
    }

    // clear scoreline area to blank lcd colour 
    g.setColor( 0xFFFFFF );
    g.fillRect( 0, height-scoreLineHeight * numberOfScoreLines, width, scoreLineHeight * numberOfScoreLines );

    // display player score and goes
    Player currentPlayer = gameLogic.getCurrentPlayer();
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