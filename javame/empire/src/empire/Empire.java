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


public class Empire extends MIDlet implements CommandListener {
    Display display = null;
    private GameView view = null;

    private Command exitCommand = new Command("Exit", Command.EXIT, 60);
    private Command aboutCommand = new Command("Help", Command.HELP, 30);
    private Command subMenuCommand = new Command("Menu", Command.HELP, 30);

    private GameModel model = null;

private static final String[] mainMenuOptions = {
                        "How To Play", "Play The Game"
                    };


    private List mainMenu = new List("Empire",
                                            List.IMPLICIT,
                                            mainMenuOptions,
                                            null);

  private static String howToPlay =
    "You must try to cover ?? squares on the landscape. "
  + "On each go a die is rolled which determines the number of moves for that player. " 
  + "When you cover a square, it changes to your colour. "
  + "The other player cannot move over you, or your squares. "
  + "But both of you can move over the sea. "
  + "You must stop your opponent from getting 150 squares. "
  + "Your own squares act as walls that the other player cannot pass through.\n"
  + "Copyright (c) Robert J. Davis 2002";


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
	//mainMenu.addCommand(aboutCommand);
	mainMenu.addCommand(exitCommand);

  mainMenu.setCommandListener(this) ;
  display.setCurrent(mainMenu);


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
  if ( c == List.SELECT_COMMAND )
  {
    int pos = mainMenu.getSelectedIndex();
    System.out.println( pos );

    if ( pos == 0 )
    {
      showHowToPlay();
    }
  
    if ( pos == 1 )
    {
	view.addCommand(subMenuCommand);
	view.addCommand(exitCommand);
	view.setCommandListener(this);
      view.init();
      display.setCurrent(view); 
    }
  }
  else if ( c == exitCommand )
  {
    destroyApp(false);
    notifyDestroyed();
  }
  else if ( c == aboutCommand )
  {
    // About.showAbout(display);
  }
  else if ( c == subMenuCommand )
  {
    //
  }
  else if ( c == GameView.nextPlayerCommand )
  {
    showNextPlayer();
  }
  else
  {
    System.out.println("Unknown command issued " + c);
  }

}

 public void showHowToPlay() 
 {
   Alert alert = new Alert("How To Play Empire");
   alert.setTimeout(Alert.FOREVER);

   // Add the copyright
   alert.setString(howToPlay);
   display.setCurrent(alert);

 }

 public void showNextPlayer()
 {
        Alert alert = new Alert("Next Player");
alert.setTimeout(Alert.FOREVER);

        alert.setString("Next player");
   display.setCurrent(alert);
 }
}