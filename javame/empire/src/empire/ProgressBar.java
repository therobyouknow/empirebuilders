package empire;

import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class ProgressBar
{
  private int maxTicks = 0;
  private int screenheight = 0;
  private int screenwidth = 0;
  private Canvas c = null;
  private Graphics g = null;

  public ProgressBar ( Canvas c, Graphics g, int scrnwidth, int scrnheight )
  {
    this.c = c;
    this.screenwidth = scrnwidth;
    this.screenheight = scrnheight;
  }

  public void setMaxTicks( int maxTicks )
  {
    this.maxTicks = maxTicks;
  }

  public void tick( int tick )
  {
    int barThickness = screenheight/10;
    int margin = 2;

    //System.out.println( tick + "\n" );

    //if ( g == null ) System.out.println( "null" );

    g.setColor( 0x000000 );
    g.drawRect( 0, (screenheight - barThickness) / 2, screenwidth, barThickness);
    g.fillRect( margin, ((screenheight - barThickness) / 2) + margin, (tick / maxTicks) * (screenwidth - margin), screenheight/10 -2 );
    c.repaint();
  }
}