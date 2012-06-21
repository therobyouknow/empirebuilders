/*
 * @(#)GameModel.java	
 */
package empire;

import javax.microedition.lcdui.*;
import java.util.Random;
import java.lang.Math;

/**
 *
 * Created on 
 *
 * @author  
 * @version
 */
public class GameLogic  
{
  // Move directions
  public static final int LEFT = 0;
  public static final int RIGHT = 3;
  public static final int UP = 1;
  public static final int DOWN = 2;
  private GameView view = null;
  private Landscape landscape = null;

  private static final int maxNumberOfPlayers = 2;
  private Player player1 = new Player( 1 );
  private Player player2 = new Player( 2 );
  private Player[] players = { player1, player2 };
  private Player currentPlayer = null;
  private int currentPlayerIndex = 0;

  private Random random = null;
 
  public void setRandom( Random random ) { this.random = random; }

  public void setGameView( GameView view ) { this.view = view; }
  public GameView getGameView() { return view; }

  public void setLandscape( Landscape landscape ) { this.landscape = landscape; }

  public Player getPlayer( int p )
  {
    if ( p < players.length )
    {
      return players[p];
    }
    return null;
  }


  public Player getCurrentPlayer() { return currentPlayer; }
  public int getNumberOfPlayers() { return players.length; }

  public void positionPlayers()
  {
    int px = 0; int py = 0; int q = 0; int p = 0;
    boolean playerPositioned = false;
    boolean anotherPlayerAlreadyPositionedAtCoords = false;

    // position players, making sure that they are not positioned
    // on top of one another    

    // position first player
    px = Math.abs(random.nextInt()) % landscape.getWidth();
    py = Math.abs(random.nextInt()) % landscape.getHeight();
    players[0].setX( px ); players[0].setY( py );
    if ( landscape.isLand( px, py ) )
    {
      landscape.claim( px, py, players[0] );
      players[0].setScore( 1 );
    }

    // positioned first player already, now need to make
    // sure each following player does not overlap the others
    for ( p = 1; p < players.length; p++ )
    { 
      while( playerPositioned == false )
      {
        px = Math.abs(random.nextInt()) % landscape.getWidth();
        py = Math.abs(random.nextInt()) % landscape.getHeight();
        for ( q = 0; q < players.length && anotherPlayerAlreadyPositionedAtCoords == false; q++ )
        {
          // avoid current player being looked at
          if ( p != q )
          {
            if ( players[q].getX() == px && players[q].getY() == py )
            {
              anotherPlayerAlreadyPositionedAtCoords = true;
            }
          }
        }
        if ( anotherPlayerAlreadyPositionedAtCoords == false ) 
        {
          players[p].setX( px ); players[p].setY( py );
          playerPositioned = true;
        }
      }
     
      // if player already on land, claim it and set score to 1
      if ( landscape.isLand( px, py ) )
      {
        landscape.claim( px, py, players[p] );
        players[p].setScore( 1 );
      }
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

    // check move within bounds of landscape
    if (    newX >=0 && newX < landscape.getWidth() 
         && newY >=0 && newY < landscape.getHeight()
       )
    {
      if ( landscape.isClaimed( newX, newY, currentPlayer ) == false )
      {
        currentPlayer.setX( newX ); currentPlayer.setY( newY );

        // check if land not already claimed by current player
        if ( landscape.isClaimed( newX, newY, currentPlayer ) == false )
        {
          // land not already claimed, so claim it
          currentPlayer.setScore( currentPlayer.getScore() + 1 );
          landscape.claim( newX, newY, currentPlayer );
        } // end if check that current player on land
        
        byte newNumberOfMoves = (byte)(currentPlayer.getNumberOfMoves() - 1);
        currentPlayer.setNumberOfMoves( newNumberOfMoves );

        if ( newNumberOfMoves == 0 )
        {
          nextPlayer();
        }
      } // end if check that land claimed by other player
    } // end check that move is within landscape
  }

  private void nextPlayer()
  {
    if ( currentPlayerIndex < (players.length-1) )
    {
      currentPlayerIndex++;
    }
    else
    {
      currentPlayerIndex = 0;
    }
    currentPlayer = players[currentPlayerIndex];
    currentPlayer.setNumberOfMoves( (byte)(1 + Math.abs(random.nextInt() % 6 )) );
          view.nextPlayer( currentPlayer );
  }

  public void start()
  {
    currentPlayer = players[currentPlayerIndex];
    currentPlayer.setNumberOfMoves( (byte)(1 + Math.abs(random.nextInt() % 6 )) );
  }
}

