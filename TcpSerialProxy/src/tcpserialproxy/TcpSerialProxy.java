package tcpserialproxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

public class TcpSerialProxy {

    public static void main(String[] args) throws InterruptedException {
        SerialPort serialPort = new SerialPort("COM3");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4659);
        } catch (IOException ex) {
            Logger.getLogger(TcpSerialProxy.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            InputStream inFromClient = null;
            OutputStream outToClient = null;
            try {
                // Wait for a client
                Socket connectionSocket = serverSocket.accept();
                
                // Connect the pipes
                inFromClient = connectionSocket.getInputStream();
                outToClient = connectionSocket.getOutputStream();
                
                // Try to read a command
                int command = inFromClient.read();
                if(command == -1) // Nothing to read
                    continue;
                
                // Send it through serial port
                System.out.println("Received: " + command);
                serialPort.openPort();
                serialPort.setParams(9600, 8, 1, 0);
                serialPort.writeByte((byte) command);
                serialPort.closePort();
                outToClient.write("CCTVVMB".getBytes());
            } catch (IOException ex) {
                Logger.getLogger(TcpSerialProxy.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SerialPortException ex) {
                System.out.println(ex);
            } finally {
                try {
                    serialPort.closePort();
                    inFromClient.close();
                    outToClient.close();
                } catch (SerialPortException | IOException ex) {
                    Logger.getLogger(TcpSerialProxy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
