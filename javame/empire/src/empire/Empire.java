/*
 * @(#)Empire.java	
 */

/*
 * Empire.java
 *
 * Created on 
 */

package empire;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.io.*;
//import About;

/**
 *
 * @author  riggs
 * @version
 */
public class Empire extends MIDlet implements CommandListener {
    Display display = null;
    private GameView view = null;

    private Command exitCommand = new Command("Exit", Command.EXIT, 60);
    private Command aboutCommand = new Command("Help", Command.SCREEN, 30);

    private GameModel model = null;

public Empire() 
{
  display = Display.getDisplay(this);
  model = new GameModel();
  view = new GameView(this);
  view.setGameModel( model );
  model.setGameView( view );
}

/**
  * Start creates the thread to do the timing.
  * It should return immediately to keep the dispatcher
  * from hanging.
  */
public void startApp() 
{
  view.init();
  view.addCommand(exitCommand);
  view.addCommand(aboutCommand);
  view.setCommandListener(this);

  display.setCurrent(view);
  model.setGameView(view);
}

    /**
     * Pause signals the thread to stop by clearing the thread field.
     * If stopped before done with the iterations it will
     * be restarted from scratch later.
     */
    public void pauseApp() {}

    /**
     * Destroy must cleanup everything.  
     * Only objects exist so the GC will do all the cleanup
     * after the last reference is removed.
     */
    public void destroyApp(boolean unconditional) {
	display.setCurrent(null);
/*
	canvas.destroy();
	if (score != null)
	    score.close();
*/
    }


/*
 * Respond to a commands issued on any Screen
 */
public void commandAction(Command c, Displayable s) 
{
  if ( c == exitCommand )
  {
    destroyApp(false);
    notifyDestroyed();
  }
  else if ( c == aboutCommand )
  {
    // About.showAbout(display);
  }
  else
  {
    System.out.println("Unknown command issued " + c);
  }
}

}