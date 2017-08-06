package tk.hobbyp;

import org.json.JSONObject;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Arkadeep on 02-Aug-17.
 */
class AppRequestHandler
{
    private byte[] receivedByte = new byte[1024];

    private DatagramSocket port;
    long timeNow=0,timePrev=0;
    boolean firstVal=false;

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
                if(dx.equals("EOT"))
                {
                    firstVal=true;
                }
                else
                {
                    float dxf, dyf;
                    dxf = Float.parseFloat(dx);
                    dyf = Float.parseFloat(dy);
                    if(!firstVal)
                    {
                        moveCursor(dxf*25, dyf*25);
                    }
                    else
                    {
                        firstVal=false;
                    }
                }
            }
            catch (Exception exception)
            {
                exception.getMessage();
                continue label;
            }
            finally
            {
                timePrev=timeNow;
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
}

