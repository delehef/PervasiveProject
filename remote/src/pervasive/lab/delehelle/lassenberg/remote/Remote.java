package pervasive.lab.delehelle.lassenberg.remote;

import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.IAccelerometer3D;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;

public class Remote
{

    boolean acceleratedLeft = false;
    boolean acceleratedRight = false;
    boolean readyToGo = true;

    private IAccelerometer3D accelerometer;

    public Remote()
    {
        accelerometer = (IAccelerometer3D) Resources.lookup(IAccelerometer3D.class);
    }

    public void doYourJob()
    {
        int i = 0;
        double threshold = 1.0;

        try
        {
            double xAcceleration = accelerometer.getAccelX();
            double zAcceleration = accelerometer.getAccelZ();

            if (Math.abs(xAcceleration) > Math.abs(zAcceleration))
            {
                if (xAcceleration > threshold)
                {
                    System.out.println("Gauche");
                    sendMessage(10);
                    Thread.sleep(500);
                }
                else if (xAcceleration < -threshold)
                {
                    System.out.println("Droite");
                    sendMessage(20);
                    Thread.sleep(500);
                }
            }
            else if (Math.abs(xAcceleration) <= Math.abs(zAcceleration))
            {
                if (zAcceleration > threshold + 0.5)
                {
                    System.out.println("V+");
                    sendMessage(30);
                    Thread.sleep(500);
                }
                else if (zAcceleration < -threshold)
                {
                    System.out.println("V-");
                    sendMessage(40);
                    Thread.sleep(500);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessage(int msg)
    {
        DatagramConnection dgConnection = null;
        Datagram dg = null;

        try
        {
            dgConnection = (DatagramConnection) Connector.open("radiogram://broadcast:10");
            dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
            dg.reset();
        }
        catch (IOException ex)
        {
            System.out.println("Could not open radiogram broadcast connection");
            ex.printStackTrace();
            return;
        }

        try
        {
            System.out.println("Sending " + msg);
            dg.writeInt(msg);
            dgConnection.send(dg);
            System.out.println("Broadcast is going through");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
