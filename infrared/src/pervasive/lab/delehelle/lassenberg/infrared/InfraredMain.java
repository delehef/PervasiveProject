/*
 * Copyright (c) 2006-2010 Sun Microsystems, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to 
 * deal in the Software without restriction, including without limitation the 
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 **/

package pervasive.lab.delehelle.lassenberg.infrared;

import com.sun.spot.service.BootloaderListenerService;
import com.sun.spot.util.BootloaderListener;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Infrared Demo

 Deploy on a Spot with an eDemoBoard attached. When you shake the
 * Sun SPOT back and forth, the RGB LEDs will flash in a pattern
 * that will magically spell out words in the air.
 *
 * @author roger (modifications by vipul)
 */

public class InfraredMain
{
    
    protected void startApp() throws MIDletStateChangeException
    {
        System.out.println("Hello, world");
        new BootloaderListener().start();
        BootloaderListenerService.getInstance().start();       // Listen for downloads/commands over USB connection
        System.out.println("Starting infrared");
        
    }
    
    protected void pauseApp()
    {
    }
    
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException
    {
    }
}
