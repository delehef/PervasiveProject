package pervasive.lab.delehelle.lassenberg.remote;

import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.IAccelerometer3D;

public class Remote
{

    boolean acceleratedLeft = false;
    boolean acceleratedRight = false;
    boolean readyToGo = true;

    private IAccelerometer3D accelerometer;

    public Remote()
    {
        accelerometer = (IAccelerometer3D)Resources.lookup(IAccelerometer3D.class);
    }

    public void swingThis()
    {
        int i = 0;
        double threshold = 1.0;

        try
        {
            double xAcceleration = accelerometer.getAccelX();
            double zAcceleration = accelerometer.getAccelZ();

            if (xAcceleration > threshold)
            {
                System.out.println("Gauche");
                Thread.sleep(500);
            }

            if (xAcceleration < -threshold)
            {
                System.out.println("Droite");
                Thread.sleep(500);
            }
            
            if (zAcceleration > threshold+0.5)
            {
                System.out.println("V+");
                System.out.println(zAcceleration);
                Thread.sleep(500);
            }

            if (zAcceleration < -threshold)
            {
                System.out.println("V-");
                System.out.println(zAcceleration);
                Thread.sleep(500);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
