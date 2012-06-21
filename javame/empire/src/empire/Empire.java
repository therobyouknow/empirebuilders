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
import java.util.Random;

public class Empire extends MIDlet implements CommandListener 
{
  // game components
  Display display = null;
  private GameView view = null;
  private Landscape landscape = null;
  Random random = new Random();
  private GameLogic gameLogic = null;

  // menu commands
  private Command exitCommand = new Command("Exit", Command.EXIT, 60);
  private Command aboutCommand = new Command("Help", Command.HELP, 30);
  public Command gameOverCommand = new Command("Game Over", Command.HELP, 10);

  private static final String[] mainMenuOptions = { "Play The Game", "How To Play" };
  private List mainMenu = new List("Empire",
                                    List.IMPLICIT,
                                    mainMenuOptions,
                                    null);

  private static final String[] yesNoDialogOptions = { "No", "Yes" };
  private List yesNoDialog = new List("Are you sure?", List.IMPLICIT, yesNoDialogOptions, null );
  private boolean areYouSure = false;

  ProgressScreen progressScreen = new ProgressScreen( "Progress" ); 

  private Player winner = null;

  private static String howToPlay =
    "You must try to cover land squares on the landscape. "
  + "On each go a die is rolled which determines the number of moves for that player. " 
  + "When you cover a square, it changes to your colour. "
  + "The other player cannot move over you, or your squares. "
  + "But both of you can move over the sea. "  
  + "The number of squares you need to cover is shown on your turn."
  + "This number goes down each time you cover a square."
  + "The winner is the player whose number reaches zero first."
  + "Your own squares act as walls that the other player cannot pass through.\n"
  + "Copyright (c) Robert J. Davis 2002";

  public List getMainMenu() { return mainMenu; }

  public void setWinner( Player winner ) { this.winner = winner; }

  public Empire() 
  {
    display = Display.getDisplay(this);

    landscape = new Landscape();
    gameLogic = new GameLogic();
    view = new GameView(this);

    gameLogic.setRandom( random );
    landscape.setRandom( random );

    gameLogic.setLandscape( landscape );
    gameLogic.setGameView( view );

    view.setLandscape( landscape );
    view.setGameLogic( gameLogic );
  }

 /**
  * Start creates the thread to do the timing.
  * It should return immediately to keep the dispatcher
  * from hanging.
  */
  public void startApp() 
  {
    mainMenu.addCommand(exitCommand);
    progressScreen.addCommand(exitCommand);
    progressScreen.setCommandListener(this);
    mainMenu.setCommandListener(this) ;
    yesNoDialog.setCommandListener(this) ;
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
  public void destroyApp(boolean unconditional) 
  {
    display.setCurrent(null);
  }

  /*
   * Respond to a commands issued on any Screen
   */
  public void commandAction(Command c, Displayable s) 
  {
    if ( c == List.SELECT_COMMAND )
    {
      if ( areYouSure == false )
      {
        int pos = mainMenu.getSelectedIndex();
        if ( pos == 1 )
        {
          showHowToPlay();
        }
  
        if ( pos == 0 )
        {
          view.addCommand(view.help);
          view.init();

          gameLogic.getLandscape().setProgress( progressScreen );
          display.setCurrent(progressScreen);
          progressScreen.ready();
          gameLogic.start();
        }
      }
      else
      {
        int pos = yesNoDialog.getSelectedIndex();
        areYouSure = false;
        if ( pos == 0 )
        {
          display.setCurrent( view ); return;
        }
        else if ( pos == 1 )
        {
          display.setCurrent( mainMenu ); return;
        }
      }
    }
    else if ( c == exitCommand )
    {
      exit();
    }
    else if ( c == aboutCommand )
    {
      // About.showAbout(display);
    }
    else if ( c == view.inGamePlayExitCommand )
    {
      areYouSure = true;
      display.setCurrent( yesNoDialog ); return;
    }
    else if ( c == view.help )
    {
      showHowToPlay();
    }
    else if ( c == gameOverCommand )
    {
      gameOver();
      display.setCurrent( mainMenu );
    }
    else if ( c == ProgressScreen.progressComplete )
    {
        view.addCommand(view.inGamePlayExitCommand);
	  view.setCommandListener(this);
        display.setCurrent(view); 
    }
    else
    {
      System.out.println("Unknown command issued " + c);
    }
  }

  public void exit()
  {
    destroyApp(false);
    notifyDestroyed();
  }

  public void showHowToPlay() 
  {
    Alert alert = new Alert("How To Play Empire");
    alert.setTimeout(Alert.FOREVER);

    // Add the copyright
    alert.setString(howToPlay);
    display.setCurrent(alert);
  }

 public void showNextPlayer( Player player )
 {
   Alert alert = new Alert("Player " + player.getNumber() + "'s turn");
   alert.setTimeout(Alert.FOREVER);

   alert.setString("Press or select 'Done' to continue.");
   display.setCurrent(alert);
 }

 public void gameOver()
 {
   Alert alert = new Alert("Player " + winner.getNumber() + " wins");
   alert.setTimeout(Alert.FOREVER);

   alert.setString("Press or select 'Done' for menu options, including for new game.");
   display.setCurrent(alert);
 }
}