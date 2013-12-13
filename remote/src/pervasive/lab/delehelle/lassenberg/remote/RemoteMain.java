package pervasive.lab.delehelle.lassenberg.remote;

import com.sun.spot.service.BootloaderListenerService;
import com.sun.spot.util.BootloaderListener;
import javax.microedition.midlet.MIDletStateChangeException;


public class RemoteMain extends javax.microedition.midlet.MIDlet
{
    
    protected void startApp() throws MIDletStateChangeException
    {
        BootloaderListenerService.getInstance().start();
        System.out.println("Starting remote");
        Remote remote = new Remote();
        while (true) {
            remote.doYourJob();
        }
    }
    
    protected void pauseApp()
    {
    }
    
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException
    {
    }
}
