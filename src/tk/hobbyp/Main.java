package tk.hobbyp;

/**
 * Created by Arkadeep on 31-Jul-17.
 */

import java.net.*;

import static java.lang.System.out;

class Main
{

    public static void main(String args[]) throws Exception
    {
        DatagramSocket serverSocket = new DatagramSocket(49443);
        InetAddress ip = InetAddress.getLocalHost();

        out.println("Server running at : " + ip.getHostAddress());
        out.println("listening on port : " + serverSocket.getLocalPort());

        AppRequestHandler handler = new AppRequestHandler(serverSocket);
        handler.handleMouseRequests();
    }
}