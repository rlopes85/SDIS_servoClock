package Servo;

import java.io.*;
import java.net.*;

/**
 * TODO implementar PI para modelo de relógio do slave
 * Implementação do slave do servo clock.
 * @author Ricardo Lopes
 */
public class TCPClient {
    /**
     *
     * @param argv, Recebe como parametros o ip do servidor e o porto na forma ip porto
     *              ex. 192.168.1.34 5000
     * @throws Exception
     */
    public static void main(String argv[]) throws Exception {
        String FromServer;
        String ToServer;

         /*
         fixme rever estrotura dos parametros, passar para aforma mais convencional ip:porto
          */
        InetAddress ip_server = InetAddress.getByName(argv[0]);
        int Port_Number = Integer.valueOf(argv[1]);

        Socket clientSocket = new Socket(ip_server, Port_Number);

        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        PrintWriter outToServer = new PrintWriter(
                clientSocket.getOutputStream(),true);

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));

        while (true) {

            //FromServer = inFromServer.readLine();


                //System.out.println("SEND(Type Q or q to Quit):");

                ToServer = inFromUser.readLine();
            System.out.println(ToServer);
                if (ToServer.equals("TIME")){
                    //System.out.println(ToServer);
                    outToServer.println(ToServer);
                }
            FromServer = inFromServer.readLine();
            System.out.println("RECIEVED:" + FromServer);
        }
    }
}
