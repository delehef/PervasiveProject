package pervasive.lab.delehelle.lassenberg.infrared;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.peripheral.Spot;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.sensorboard.peripheral.InfraRed;
import com.sun.spot.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.midlet.MIDletStateChangeException;

public class Infrared extends javax.microedition.midlet.MIDlet {

    public static final byte[] SAMSUNG_VOL_PLUS
            = {
                71, 70, 9, 25, 9, 26, 9, 26, 9, 9, 9, 9, 9, 8, 8, 9, 9, 8, 9, 26, 9, 26, 9, 26, 9, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 26, 9, 26, 9, 26, 9, 9, 9, 8, 9, 8, 9, 8, 9, 9, 9, 9, 9, 8, 9, 8, 9, 26, 9, 26, 9, 26, 9, 25, 10, 26, 9, 26, 9, 8, 9, 8, 9, 9, 8
            };
    public static final byte[] SAMSUNG_VOL_MINUS
            = {
                71, 70, 9, 26, 9, 26, 9, 26, 9, 8, 9, 8, 9, 8, 9, 8, 9, 9, 9, 26, 9, 26, 9, 26, 9, 9, 9, 9, 9, 9, 9, 8, 9, 8, 9, 26, 9, 26, 9, 8, 9, 26, 9, 9, 9, 9, 9, 8, 9, 9, 9, 9, 9, 9, 9, 26, 9, 9, 8, 26, 9, 26, 9, 26, 9, 26, 9, 26, 9, 9, 9, 9, 9, 8, 9
            };
    public static final byte[] SAMSUNG_CHAN_UP
            = {
                71, 70, 9, 26, 9, 26, 10, 25, 10, 8, 9, 8, 9, 8, 9, 8, 9, 8, 9, 26, 9, 26, 9, 25, 9, 8, 9, 8, 10, 8, 9, 8, 9, 8, 9, 8, 9, 26, 9, 8, 9, 8, 9, 26, 9, 8, 9, 8, 9, 8, 9, 25, 9, 8, 9, 26, 9, 25, 9, 8, 9, 26, 9, 26, 9, 26, 9, 26, 10, 8, 9, 9, 9, 8, 9
            };
    public static final byte[] SAMSUNG_CHAN_DOWN
            = {
                71, 70, 9, 26, 9, 26, 9, 26, 9, 8, 9, 9, 9, 9, 9, 9, 9, 8, 9, 26, 9, 26, 9, 26, 9, 8, 9, 9, 9, 9, 9, 8, 9, 9, 9, 8, 9, 8, 9, 9, 9, 9, 9, 26, 9, 9, 9, 8, 9, 8, 9, 26, 9, 26, 9, 26, 9, 26, 9, 9, 9, 25, 9, 26, 9, 26, 9, 26, 9, 8, 9, 8, 8, 9, 9
            };

    // some commands from the Apple IR Remote
    public static final long APPLE_PLUS = 0x087ee110bL;  // Apple custom code ID = 0x87EE
    public static final long APPLE_MINUS = 0x087ee110dL;  // address = 0x11
    public static final long APPLE_FORWARD = 0x087ee1107L;  // command = 0x0b, 0x0d, 0x07 & 0x08
    public static final long APPLE_BACK = 0x087ee1108L;

    // some commands from a NEC TV remote:  ~address address ~command command
    public static final long NEC_VOL_PLUS = 0x0bf40e51aL;  // ~address = 0xbf
    public static final long NEC_VOL_MINUS = 0x0bf40e11eL;  //  address = 0x40
    public static final long NEC_CHAN_UP = 0x0bf40e41bL;  // ~command = 0xe5, 0xe1, 0xe4 & 0xe0
    public static final long NEC_CHAN_DOWN = 0x0bf40e01fL;  //  command = 0x1a, 0x1e, 0x1b & 0x1f

    // some commands from a SONY TV remote   addr<5> cmd<7>
    public static final int SONY_VOL_PLUS = 0x092;         // addr = 1  cmd = 0x12
    public static final int SONY_VOL_MINUS = 0x093;         // addr = 1  cmd = 0x13
    public static final int SONY_CHAN_UP = 0x090;         // addr = 1  cmd = 0x10
    public static final int SONY_CHAN_DOWN = 0x091;         // addr = 1  cmd = 0x11

    private static final boolean USE_TV_CMDS = true;   // set to false to send Apple Remote commands

    private InfraRed ir;
    private ITriColorLEDArray leds;

    // IR transmit methods
    private long getPlusCmd() {
        return USE_TV_CMDS ? NEC_VOL_PLUS : APPLE_PLUS;
    }

    private long getMinusCmd() {
        return USE_TV_CMDS ? NEC_VOL_MINUS : APPLE_MINUS;
    }

    // IR receive methods
    private static final boolean DISPLAY_IR_TIMING = false;

    private int volume = 3;
    private int lastCommand = 0;

    private void displayVolume() {
        leds.setRGB(0, 20, 0);
        for (int i = 0; i < 8; i++) {
            leds.getLED(i).setOn(i < volume);
        }
    }

    private void increaseVolume() {
        ir.writeIR(SAMSUNG_VOL_PLUS);
    }

    private void decreaseVolume() {
        ir.writeIR(SAMSUNG_VOL_MINUS);
    }

    private void increaseChannel() {
        ir.writeIR(SAMSUNG_CHAN_UP);
    }

    private void decreaseChannel() {
        ir.writeIR(SAMSUNG_CHAN_DOWN);
    }

    private boolean isAppleRemote(long word) {
        return (word & 0x0ffff0000L) == 0x087ee0000L;
    }

    private boolean isSamsung(byte[] val) {
        // Samsung commands begin with roughly 70 70 (2 * 4500 ms high then down), but it's 70 71 on my SPOT
        return (Math.abs(val[0] - val[1]) <= 2 && Math.abs(val[0] - 70) <= 2);
    }

    private boolean compare(byte[] val, byte[] reference) {
        if (reference.length != val.length) {
            System.out.println("Not the same length: " + val.length + " vs. " + reference.length);
            return false;
        }

        boolean OK = true;
        for (int i = 0; i < val.length; i++) {
            if (Math.abs(reference[i] - val[i]) >= 2) {
                OK = false;
            }
        }

        return OK;
    }

    public void readIRSensor() {
        try {
            boolean OK = true;
            byte[] val = ir.readIR();
            if (val == null || val.length == 0) {
                System.out.println("No command read");
            } else {
                if (isSamsung(val)) // Then it's a damned Samsung
                {
                    if (compare(val, SAMSUNG_VOL_MINUS)) {
                        decreaseVolume();
                    } else if (compare(val, SAMSUNG_VOL_PLUS)) {
                        increaseVolume();
                    } else if (compare(val, SAMSUNG_CHAN_UP)) {
                        System.out.println("Channel up");
                    } else if (compare(val, SAMSUNG_CHAN_DOWN)) {
                        System.out.println("Channel down");
                    } else {
                        for (int i = 0; i < val.length; i++) {
                            System.out.print(val[i] + " ");
                        }
                    }
                } else {

                    long cmd = ir.decodeNEC(val);
                    if (cmd > 0) {
                        System.out.println("NEC Command = " + Long.toString(cmd, 16));
                        lastCommand = 0;
                        int c = (int) (cmd & 0xff);
                        if (isAppleRemote(cmd)) {
                            if (c == (APPLE_PLUS & 0xff)) {
                                increaseVolume();
                            } else if (c == (APPLE_MINUS & 0xff)) {
                                decreaseVolume();
                            }
                        } else {
                            if (c == (NEC_VOL_PLUS & 0xff)) {
                                increaseVolume();
                            } else if (c == (NEC_VOL_MINUS & 0xff)) {
                                decreaseVolume();
                            }
                        }
                    } else if (cmd == InfraRed.NEC_REPEAT && lastCommand > 0) {
                        System.out.println("  A repeat command");
                        if (lastCommand == 1) {
                            increaseVolume();
                        } else if (lastCommand == 2) {
                            decreaseVolume();
                        }
                    } else if (cmd == InfraRed.NEC_BAD_FORMAT) {
                        cmd = decodeSONY(val);
                        lastCommand = 0;
                        if (cmd > 0) {
                            System.out.println("SONY Command = " + Long.toString(cmd, 16));
                            int c = (int) (cmd & 0x7f);
                            if (c == (SONY_VOL_PLUS & 0x7f)) {
                                increaseVolume();
                            } else if (c == (SONY_VOL_MINUS & 0x7f)) {
                                decreaseVolume();
                            }
                            Utils.sleep(100);   // ignore retransmits
                        }
                    } else {
                        lastCommand = 0;
                    }
                }
            }
        } catch (InterruptedException iex) {
            System.out.println("IR read interrupted by IR write");
        } catch (Exception ex) {
            System.out.println("Error reading IR sensor: " + ex);
        }
    }

    // SONY format
    public static final int SONY_INITIAL_HEADER = (2470 + 63) / 64;
    public static final int SONY_OFF = (556 + 63) / 64;
    public static final int SONY_ZERO = (644 + 63) / 64;
    public static final int SONY_ONE = (1243 + 63) / 64;
    public static final int SONY_BAD_FORMAT = -2;

    /**
     * Take an IR message specified by a byte array and decode it using the SONY
     * format. Typically passed the byte array returned by readIR().
     *
     * @param val a byte array specifying pulse durations in 64 microsecond time
     * periods
     * @return the decoded message value or SONY_BAD_FORMAT (= -2) if the
     * message was not formatted properly
     */
    public int decodeSONY(byte val[]) {
        int result = SONY_BAD_FORMAT;           // assume bad format
        int len = val.length;
        if (Math.abs(val[0] - SONY_INITIAL_HEADER) <= (SONY_INITIAL_HEADER / 4)) {
            if (len == 25) {
                int word = 0;
                for (int i = 12; i > 0; i--) {
                    word <<= 1;
                    if (Math.abs(val[2 * i - 1] - SONY_OFF) > (SONY_OFF / 4)) {
                        System.out.println("decodeSONY: questionable off for bit " + i + " : " + (val[2 * i - 1] * 64));
                    }
                    int w = val[2 * i];
                    if (Math.abs(w - SONY_ONE) <= (SONY_ONE / 4)) {
                        word += 1;
                    } else if (Math.abs(w - SONY_ZERO) > (SONY_ZERO / 4)) {
                        System.out.println("decodeSONY: questionable on for bit " + i + " : " + (w * 64));
                    }
                }
                result = word & 0x0fff;
            } else {
                System.out.println("decodeSONY: Bad command length: " + len);
            }
        } else {
            System.out.println("decodeSONY: Initial pulse on not as expected: " + (val[0] * 64));
        }
        return result;
    }

    /**
     * Encode a number using the SONY format into a byte array that can be given
     * to writeIR().
     *
     * @param cmd the number to encode
     * @return a byte array specifying the pulse durations of the message
     */
    public byte[] encodeSONY(long cmd) {
        byte val[] = new byte[25];
        int j = 0;
        val[j++] = (byte) SONY_INITIAL_HEADER;
        for (int i = 0; i < 12; i++) {
            val[j++] = (byte) SONY_OFF;
            val[j++] = (byte) (((cmd & 0x1) == 0) ? SONY_ZERO : SONY_ONE);
            cmd >>= 1;
        }
        return val;
    }

    public void writeSONY(long cmd) {
        byte msg[] = encodeSONY(cmd);
        for (int i = 0; i < 3; i++) {   // need to repeat twice w/ 45msec delay between sends
            ir.writeIR(msg);
            Utils.sleep(45);
        }
    }

    // MIDlet initialization
    protected void startApp() throws MIDletStateChangeException {
        Spot.getInstance().getSleepManager().disableDeepSleep();

        ir = (InfraRed) Resources.lookup(InfraRed.class);
        if (ir == null) {
            System.out.println("\nSorry this SPOT does not have an InfraRed sensor\n");
            notifyDestroyed();
        }

        leds = (ITriColorLEDArray) Resources.lookup(ITriColorLEDArray.class);
        startReceiverThread();
        startSerialThread();
    }

    public void startSerialThread() {
        new Thread() {
            public void run() {
                final char VOLUME_UP = 0;
                final char VOLUME_DOWN = 1;
                final char CHAN_UP = 2;
                final char CHAN_DOWN = 3;
                
                OutputStream output = null;
                InputStream input = null;
                try {
                    output = Connector.openOutputStream("serial://usb");
                    input = Connector.openInputStream("serial://usb");
                    int command;
                    while (true) {
                        command = input.read();
                        switch((char)command){
                            case VOLUME_UP:
                                increaseVolume();
                                break;

                            case VOLUME_DOWN:
                                decreaseVolume();
                                break;
                            
                            case CHAN_UP:
                                increaseChannel();
                                break;

                            case CHAN_DOWN:
                                decreaseChannel();
                                break;
                        }

                        output.write(outString.getBytes());
                        output.flush();
                        output.close();
                    }
                } catch (IOException e) {
                    System.out.println("Error initializing USB: " + e.getMessage());
                    return;
                }
            }
        }.start();
    }

    public void startReceiverThread() {
        new Thread() {
            public void run() {
                int tmp = -1;
                RadiogramConnection dgConnection = null;
                Datagram dg = null;

                try {
                    dgConnection = (RadiogramConnection) Connector.open("radiogram://:10");
                    dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
                } catch (IOException e) {
                    System.out.println("Could not open radiogram receiver connection");
                    e.printStackTrace();
                    return;
                }

                System.out.println("Connection ready");
                while (true) {
                    try {
                        dg.reset();
                        System.out.println("Reading...");
                        dgConnection.receive(dg);
                        tmp = dg.readInt();
                        System.out.println("Received: " + tmp + " from " + dg.getAddress());
                        switch (tmp) {
                            case 10:
                                decreaseChannel();
                                break;

                            case 20:
                                increaseChannel();
                                break;

                            case 30:
                                increaseVolume();
                                break;

                            case 40:
                                decreaseVolume();
                                break;
                        }
                        increaseVolume();
                    } catch (IOException e) {
                        System.out.println("Nothing received");
                    }
                }
            }
        }.start();
    }

    protected void pauseApp() {
        // This will never be called by the Squawk VM
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // Only called if startApp throws any exception other than MIDletStateChangeException
    }
}
