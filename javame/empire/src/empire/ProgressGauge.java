package empire;
import javax.microedition.lcdui.*;

class ProgressGauge extends Gauge implements Runnable
{
  private int value = 0;
  CommandListener commandListener = null;

  public ProgressGauge( String title, boolean flag, int maxValue, int value ) 
  { 
    super( title, flag, maxValue, value);
    this.value = value;
  }

  public void start() {}
  public void run() {}
}