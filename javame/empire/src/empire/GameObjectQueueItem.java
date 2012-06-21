package empire;
import javax.microedition.lcdui.Image;

public class GameObjectQueueItem
{
  private int x = 0;
  private int y = 0;
  private Image gameObject = null;

  int getX() { return x; }
  int getY() { return y; }
  Image getGameObject() { return gameObject; }

  void set( int x, int y, Image gameObject )
  {
    this.x = x;
    this.y = y;
    this.gameObject = gameObject; 
  }
}