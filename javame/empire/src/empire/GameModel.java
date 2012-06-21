/*
 * @(#)GameModel.java	
 */
package empire;

import javax.microedition.lcdui.*;
import java.util.Random;

/**
 *
 * Created on 
 *
 * @author  
 * @version
 */
public class GameModel  {
    private byte[][] array;

    // Move directions
    public static final int LEFT = 0;
    public static final int RIGHT = 3;
    public static final int UP = 1;
    public static final int DOWN = 2;

    // Bit definitions for pieces of each board position

    public static final byte SEA = 0;  
    public static final byte LAND = 1; 
    public static final byte MARKER = 2; 
    public static final byte PLAYER1 = 1 << 3;   
    public static final byte PLAYER2 = 2 << 3; 

    public static final int islandOdds = 20;

    private int width = 0;
    private int height = 0;   

    private Player player1 = new Player( 1 );
    private Player player2 = new Player( 2 );
   
    private GameView view = null;

    private Player currentPlayer = null;

    private static final int numberOfPlayers = 2;

    RandomGeneratorWithRange random = new RandomGeneratorWithRange();

    public GameModel() {
    }

  public void setGameView( GameView view ) { this.view = view; }
  public GameView getGameView() { return view; }

  public Player getCurrentPlayer() { return currentPlayer; }

  public void setDimensions(int w, int h)
  {
    width = w; height = h;
    array = new byte[w][h];

    for ( int x = 0; x < width; x++ )
    {
      for ( int y = 0; y < height; y++  )
      {
        array[x][y] = SEA;
      }
    }
  }

  public int getWidth() { return width; }
  public int getHeight() { return height; }

  public byte getGameObjectAt( int x, int y )
  {
    if (( x < width ) && ( y < height ))
    {
      return array[x][y];
    }
    else // out of bounds
    {
      return 0x00;
    } 
  }

  public void generateLandscape()
  {
    int maxNumberOfLandscapeUnits = (width * height) / 2;
    int numberOfLandscapeUnits = 0; 
    int island = 0;

    int positionX = random.nextInt( width ); 
    int positionY = random.nextInt( height );

    while( numberOfLandscapeUnits < maxNumberOfLandscapeUnits )
    {
      if ( array[positionX][positionY]==SEA ) 
      {
        array[positionX][positionY]=LAND; numberOfLandscapeUnits+=1;
      }

      // island
      island = random.nextInt( islandOdds );
      if ( island == 1 ) 
      {
        positionX = random.nextInt( width ); 
        positionY = random.nextInt( height ); 
      }

      positionX += (random.nextInt( 3 ) - 1 );

      if (positionX < 0) positionX = 0;
      if (positionX > (width-1)) positionX = width-1;
 
      positionY += (random.nextInt( 3 ) - 1 );

      if (positionY < 0) positionY = 0;
      if (positionY > (height-1)) positionY = height-1;
    }
  }

  public void positionPlayers()
  {
    int p1x = random.nextInt( width ); int p1y = random.nextInt( height );
   
    player1.setX( p1x ); player1.setY( p1y );

    // if player already on land, claim it and set score to 1
    if ( array[p1x][p1y] == LAND )
    {
      array[p1x][p1y] = (byte)(array[p1x][p1y] & PLAYER1);
      player1.setScore( 1 );
    }

    // make sure that player 2 position is not the same as player 1
    int p2x = 0; int p2y = 0;
    do
    {
      p2x = random.nextInt( width ); p2y = random.nextInt( height );
    }
    while( p2x == p1x && p2y == p2y );

    player2.setX( p2x ); player2.setY( p2y );

    // if player already on land, claim it and set score to 1
    if ( array[p2x][p2y] == LAND ) 
    {
      array[p2x][p2y] = (byte)(array[p2x][p2y] & PLAYER2);
      player2.setScore( 1 );
    }

  }

  public Player getPlayer1() { return player1; }
  public Player getPlayer2() { return player2; }

  public void move( int move )
  {
    int currentX = currentPlayer.getX();
    int currentY = currentPlayer.getY();

    int newX = currentX;
    int newY = currentY;

    switch( move )
    {
      case LEFT:
        newX -= 1;
      break;      

      case RIGHT:
        newX += 1;
      break; 

      case UP:
        newY -= 1;
      break;

      case DOWN:
        newY += 1;
      break;

      default:
    }

    if (    newX >=0 && newX < width 
         && newY >=0 && newY < height
       )
    {
      if ( otherPlayerTerritory( currentPlayer, newX, newY ) == false )
      {
        currentPlayer.setX( newX ); currentPlayer.setY( newY );

        // check if land not already claimed by current player
        if ( array[newX][newY] == LAND )
        {
          // land not already claimed, so claim it
          currentPlayer.setScore( currentPlayer.getScore() + 1 );
          array[newX][newY] = (byte)(array[newX][newY] | currentPlayer.getId());
        } // end if check that current player on land
        
        byte numberOfMoves = currentPlayer.getNumberOfMoves();
        currentPlayer.setNumberOfMoves( (byte)(numberOfMoves - 1) );
        if ( numberOfMoves == 0 )
        {
    System.out.println( currentPlayer.getNumberOfMoves() );
          nextPlayer();
          view.nextPlayer( currentPlayer );
        }
      } // end if check that land claimed by other player
    } // end check that move is within landscape
  }

  private void nextPlayer()
  {
    // change current player
    if ( currentPlayer == player1 ) 
    { 
      currentPlayer = player2;  
    }
    else if ( currentPlayer == player2 ) currentPlayer = player1;
    
    currentPlayer.setNumberOfMoves( (byte)(1 + random.nextInt( 6 )) );
  }

  public void start()
  {
    currentPlayer = player1;
    currentPlayer.setNumberOfMoves( (byte)(1 + random.nextInt( 6 )) );
  }

  private boolean otherPlayerTerritory( Player player, int x, int y )
  {
    if (    player1.getX() == player2.getX()
         && player1.getY() == player2.getY() )
    {
      return true;
    }

    if ( player == player1 )
    {
      if ( (array[x][y] & player2.getId() ) == player2.getId() )
      {
        System.out.println( "array: " + array[x][y] + " player2: " + player2.getId() );
        return true;
      }
    } 
    else 
    {
      if ( player == player2 )
      {
        if ( (array[x][y] & player1.getId() ) == player1.getId() )
        {
          System.out.println( "array: " + array[x][y] + " player1: " + player1.getId() );
          return true;
        }
      }
    }
    return false;
  }
}

