package empire;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

// this class implements a none interactive gague control that
// moves automatically.
class ProgressScreen extends Form
{
  ProgressGauge progressGauge = null; 
  Thread progressThread = new Thread( progressGauge );
  CommandListener commandListener = null;

  public static Command progressComplete = new Command("Progress Complete", Command.SCREEN, 70 );

  ProgressScreen( String message ) 
  {
    super( message ); 
    progressGauge = new ProgressGauge( "Building Landscape", false, 100, 0 );
    append( progressGauge );
    progressGauge.setValue( 0 ); 
    progressThread.start();
  }

  public void setCommandListener( CommandListener commandListener ) 
  { 
    this.commandListener = commandListener;
  }

  public void setValue( int value ) { progressGauge.setValue( value ); }

  public void done()
  {
    //try{ progressThread.join(); } catch( InterruptedException ie ) {}
    commandListener.commandAction( progressComplete, this);
  }

  public void ready() 
  {
  }
}
