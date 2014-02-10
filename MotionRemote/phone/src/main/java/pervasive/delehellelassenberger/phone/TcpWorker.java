package pervasive.delehellelassenberger.phone;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

public class TcpWorker extends AsyncTask<Byte, Integer, Integer> {
    @Override
    protected Integer doInBackground(Byte... bytes) {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("192.168.12.95", 4659);
            for(Byte b : bytes) {
                System.out.println("Sending" + b);
                clientSocket.getOutputStream().write(b);
                if(b == 6)
                {
                    byte[] buf = new byte[2];
                    int read = 0;
                    while(read <1)
                        read = clientSocket.getInputStream().read(buf);
                    return new Integer(buf[0]);
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }
}
