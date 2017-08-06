package tk.hobbyp;

import org.json.JSONObject;
import java.awt.*;
import java.awt.event.InputEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

class AppRequestHandler
{
    private byte[] receivedByte = new byte[1024];

    private DatagramSocket port;
    long timeNow=0,timePrev=0;
    private boolean firstVal=false;

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
                String receivedString;
                DatagramPacket receivePacket = new DatagramPacket(receivedByte, receivedByte.length);
                port.receive(receivePacket);
                receivedString = new String(receivePacket.getData());
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
            }catch (Exception e)
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
}

