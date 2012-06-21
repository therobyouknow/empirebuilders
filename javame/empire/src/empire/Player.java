package empire;

public class Player
{
  private int x = 0;
  private int y = 0;
  private byte id = 0;
  private byte number = 0;

  public byte numberOfMoves = 0;

  public Player( int number )
  { 
    this.number = (byte)number;
    this.id = (byte)(this.number << 3); 
  }

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
}