package Servo;

/**
 * Created by rlopes on 12/3/13.
 */
import java.io.*;
import java.net.*;

/**
 *
 */
public class TCPClient {
    /**
     *
     * @param argv
     * @throws Exception
     */
    public static void main(String argv[]) throws Exception {
        String FromServer;
        String ToServer;


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

            FromServer = inFromServer.readLine();

            if ( FromServer.equals("q") || FromServer.equals("Q")) {
                clientSocket.close();
                break;
            }

            else {
                System.out.println("RECIEVED:" + FromServer);
                System.out.println("SEND(Type Q or q to Quit):");

                ToServer = inFromUser.readLine();

                if (ToServer.equals("Q") || ToServer.equals("q")) {
                    outToServer.println (ToServer) ;
                    clientSocket.close();
                    break;
                }

                else {
                    outToServer.println(ToServer);
                }
            }
        }
    }
}
