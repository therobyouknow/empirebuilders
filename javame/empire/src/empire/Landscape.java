package empire;

import java.lang.Math;
import java.util.Random;

public class Landscape
{  
  private byte array[][] = null;

  private int width = 0;
  private int height = 0; 

  public static final byte SEA = 0;  
  public static final byte LAND = 1; 

  public static final int islandOdds = 50;

  private static int landSqrsPercent = 55;
  private int numberOfLandSqrs = 0;

  byte mask = (byte)(255 - ((1 << Player.playerShift) - 1));

  private Random random = null;
 
  // future editions to this class will employ abstract interface to
  // decouple from any particular progress indicator dialog
  private ProgressScreen progress = null;

  public void setProgress( ProgressScreen progress ) { this.progress = progress; }

  public void setRandom( Random random ) { this.random = random; }

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

  public boolean isLand( int x, int y ) { return (( array[x][y] & LAND ) == LAND); }
  public boolean isSea( int x, int y ) { return ( array[x][y] == 0); }

  public void claim( int x, int y, Player player )
  {
    if (( x < width ) && ( y < height )) 
    {
      if ( (array[x][y] & LAND) == LAND )
      {
        array[x][y] = (byte)(array[x][y] | player.getId());
      }
    } 
  }

  public boolean isClaimedByAnotherPlayer( int x, int y, Player player )
  {
    if ( array[x][y] == SEA ) return false;

    if ( ( array[x][y] & mask ) != (byte)0
          &&
          (byte)(array[x][y] & player.getId()) != player.getId()
       )
    {
      return true;
    }

    return false;   
  }

  public boolean isClaimed( int x, int y )
  {
    if ( array[x][y] == SEA ) return false;

    if ( (array[x][y] & mask) != (byte)0 )
    {
      return true;
    }
    
    return false;   
  }

  public int getPlayerWhoClaimed( int x, int y )
  {
    return (int)(array[x][y] >> Player.playerShift);
  }

  public void generate()
  {
    for ( int x = 0; x < width; x++ )
    {
      for ( int y = 0; y < height; y++  )
      {
        array[x][y] = SEA;
      }
    }

    int maxNumberOfLandscapeUnits = (width * height) / 2;
    int numberOfLandscapeUnits = 0; 
    int island = 0;

    int positionX = Math.abs(random.nextInt()) % width; 
    int positionY = Math.abs(random.nextInt()) % height;

    while( numberOfLandscapeUnits < numberOfLandSqrs )
    {
      progress.setValue( ( numberOfLandscapeUnits * 100 ) / numberOfLandSqrs  );
      // adding a landscape unit, so make sure that
      // the location where it is being added is not already land
      // to ensure that the correct number of landscape units is created
      if ( array[positionX][positionY]==SEA ) 
      {
        array[positionX][positionY]=LAND; numberOfLandscapeUnits+=1;
      }

      // island
      island = Math.abs(random.nextInt()) % islandOdds;
      if ( island == 1 ) 
      {
        positionX = Math.abs(random.nextInt()) % width; 
        positionY = Math.abs(random.nextInt()) % height; 
      }

      positionX += ((random.nextInt() % 3) );

      if (positionX < 0) positionX = 0;
      if (positionX > (width-1)) positionX = width-1;
 
      positionY += ((random.nextInt() % 3) );

      if (positionY < 0) positionY = 0;
      if (positionY > (height-1)) positionY = height-1;
    }
    progress.done();
  }

  public void setDimensions(int w, int h)
  {
    width = w; height = h;
    array = new byte[w][h];
    numberOfLandSqrs = (w * h * landSqrsPercent) / 100;
  }

  public int getNumberOfLandSqrs() { return numberOfLandSqrs; }
}