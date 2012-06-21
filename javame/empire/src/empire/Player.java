package empire;

import javax.microedition.lcdui.Image;

public class Player
{
  private int x = 0;
  private int y = 0;
  private byte id = 0;
  private byte number = 0;
  private byte numberOfMoves = 0;
  private Image symbol = null;
  private Image claimedLandscape = null;

  public static final byte playerShift = 3;

  //static Vector players = new Vector();

  //public static Player newInstance( int number )
  Player( int number )
  { 
    
    this.number = (byte)number;
    this.id = (byte)(this.number << playerShift); 
  }

 // protected Player() {};

  public byte getNumber() { return number; }

  public byte getNumberOfMoves() { return numberOfMoves; }
  public void setNumberOfMoves( byte numberOfMoves ) { this.numberOfMoves = numberOfMoves; }

  public byte getId() { return id; }
  public int getX() { return x; }
  public int getY() { return y; }
  public int score;
  public int getScore() { return score; }
  public void setX( int x ) { this.x = x; }
  public void setY( int y ) { this.y = y; }
  public void setScore( int score ) { this.score = score; }
  public void setSymbol( Image symbol ) { this.symbol = symbol; }
  public void setClaimedLandscape( Image claimedLandscape ) { this.claimedLandscape = claimedLandscape; }
  public Image getSymbol() { return symbol; }
  public Image getClaimedLandscape() { return claimedLandscape; }
}