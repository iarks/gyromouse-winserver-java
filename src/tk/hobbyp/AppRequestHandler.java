package tk.hobbyp;

import org.json.JSONObject;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.locks.ReentrantLock;

class AppRequestHandler
{
    private byte[] receivedByte = new byte[1024];

    private DatagramSocket port;
    long timeNow=0,timePrev=0;
    private boolean firstVal=false;
    private String receivedString;
    ReentrantLock clipboard = new ReentrantLock();

    AppRequestHandler(DatagramSocket port)
    {
        this.port = port;
//        System.out.println("HERE IN CONSTRUCTOR");
    }


    void handleMouseRequests()
    {
//        System.out.println("HERE before label");
        label:
        while(true)
        {
            try
            {
                receivedString=null;
                DatagramPacket receivePacket = new DatagramPacket(receivedByte, receivedByte.length);
                port.receive(receivePacket);
                receivedString = new String(receivePacket.getData(),"UTF-8");
                receivedString = receivedString.trim();
                System.out.println(receivedString);
                String dx = "0.0", dy = "0.0";
                try
                {

                    JSONObject jsonObj = new JSONObject(receivedString);
                    dx = jsonObj.getString("X");
                    dy = jsonObj.getString("Y");
                    System.out.println("X: " + dx);
                    System.out.println("Y: " + dy);
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
                float dyf,dxf;
                switch (dx)
                {
                    case "EOT":
                        firstVal = true;
                        break;
                    case "S":
                        dyf = Float.parseFloat(dy);
                        scrollPage(dyf);
                        break;
                    case "RD":
                        rightDown();
                        break;
                    case "RU":
                        rightUp();
                        break;
                    case "K":
                        System.out.println(dy);
                        System.out.println(dy.getBytes("UTF-8").toString());
//                        Thread th = new Thread(new CopyPaste(dy,clipboard));
//                        th.start();
                        break;
                    case "BS":
                        backSpace();
                        break;
                    case "EN":
                        enter();
                        break;
                    case "U":
                        dyf = Float.parseFloat(dy);
                        pressUnicode((int)dyf);
                        break;
                    default:
                        dxf = Float.parseFloat(dx);
                        dyf = Float.parseFloat(dy);
                        if (!firstVal)
                        {
                            moveCursor(dxf * 25, dyf * 25);
                        }
                        else
                        {
                            firstVal = false;
                        }
                }
            }
            catch (Exception e)
            {
                continue label;
            }
        }
    }

    private void moveCursor(float dx, float dy)
    {
        Point p = MouseInfo.getPointerInfo().getLocation();
        int x = p.x;
        int y = p.y;
        try
        {
            Robot robot = new Robot();
            robot.mouseMove(x + (int) dx, y + (int) dy);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void scrollPage(float dy)
    {
        try
        {
            Robot robot = new Robot();
            robot.mouseWheel(-(int)dy);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void scrollClick()
    {
        try
        {
            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON2_MASK);
            robot.mouseRelease(InputEvent.BUTTON2_MASK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void rightDown()
    {
        try
        {
            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON3_MASK);
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }

    }

    private void rightUp()
    {
        try
        {
            Robot robot = new Robot();
            robot.mouseRelease(InputEvent.BUTTON3_MASK);
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }

    }

    private void backSpace()
    {
        Robot rob = null;
        try
        {
            rob = new Robot();
            rob.keyPress(KeyEvent.VK_BACK_SPACE);
            rob.keyRelease(KeyEvent.VK_BACK_SPACE);
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }


    }

    private void enter()
    {
        Robot rob = null;
        try
        {
            rob = new Robot();
            rob.keyPress(KeyEvent.VK_ENTER);
            rob.keyRelease(KeyEvent.VK_ENTER);
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }

    }

    private void pressUnicode(int key_code)
    {
        Robot r = null;
        try
        {
            r = new Robot();
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }

        r.keyPress(KeyEvent.VK_ALT);

        for(int i = 3; i >= 0; --i)
        {
            // extracts a single decade of the key-code and adds
            // an offset to get the required VK_NUMPAD key-code
            int numpad_kc = key_code / (int) (Math.pow(10, i)) % 10 + KeyEvent.VK_NUMPAD0;
            System.out.println(numpad_kc);
            r.keyPress(numpad_kc);
            r.keyRelease(numpad_kc);
        }

        r.keyRelease(KeyEvent.VK_ALT);
    }
}

