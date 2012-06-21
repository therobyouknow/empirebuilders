package empire;

import javax.microedition.lcdui.Graphics;

public class Scrollbar
{
  private int topLeftX = 0;
  private int topLeftY = 0;

  private int thickness = 0;
  private int length = 0;

  private int orientation = 0;  

  private int visible = 0;
  private int value = 0;
  private int minimum = 0;
  private int maximum = 0;

  static final int HORIZONTAL = 0;
  static final int VERTICAL = 1;

  private static final int margin = 2;

  Scrollbar( int topLeftX, int topLeftY, int thickness, int length, int orientation, 
             int value, int visible, int minimum, int maximum )
  {
    this.topLeftX = topLeftX;
    this.topLeftY = topLeftY;
    this.thickness = thickness;
    this.length = length;
    this.orientation = orientation;

    this.value = value;
    this.visible = visible;
    this.minimum = minimum;
    this.maximum = maximum;
  }

  void setValue( int value ) 
  {
    this.value = value; 
  }

  void draw( Graphics g )
  {
    if ( orientation == HORIZONTAL )
    {
      g.setColor( 0xFFFFFF );
      g.fillRect( topLeftX, topLeftY, length-1, thickness-1 );
      g.setColor( 0x000000 );
      g.drawRect( topLeftX, topLeftY, length-1, thickness-1 );
      g.fillRect( topLeftX + margin + (value * (length - margin * 2 )) / maximum,
                  topLeftY + margin,
                  (visible * (length - margin * 2))/maximum,
                  thickness-margin * 2
                );
    }

    if ( orientation == VERTICAL )
    {
      g.setColor( 0xFFFFFF );
      g.fillRect( topLeftX, topLeftY, thickness-1, length-1 );
      g.setColor( 0x000000 );
      g.drawRect( topLeftX, topLeftY, thickness-1, length-1 );
      g.fillRect( topLeftX + margin, 
                  topLeftY + margin + (value * (length - margin * 2)) / maximum,
                  thickness-margin * 2,
                  (visible * (length - margin * 2))/maximum
                );
    }
  }
}