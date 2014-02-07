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
                System.out.println("Waiting for a connection...");
                Socket connectionSocket = serverSocket.accept();
                inFromClient = connectionSocket.getInputStream();
                outToClient = connectionSocket.getOutputStream();
                int command = inFromClient.read();
                if(command == -1)
                    continue;
                System.out.println("Received: " + command);
                System.out.println("Port opened: " + serialPort.openPort());
                System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
                System.out.println((byte)command + " successfully writen to port: " + serialPort.writeByte((byte) command));
                System.out.println("Port closed: " + serialPort.closePort());
                outToClient.write("CCTVVMB".getBytes());
            } catch (IOException ex) {
                Logger.getLogger(TcpSerialProxy.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SerialPortException ex) {
                System.out.println(ex);
            } finally {
                try {
                    inFromClient.close();
                } catch (IOException ex) {
                    Logger.getLogger(TcpSerialProxy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
