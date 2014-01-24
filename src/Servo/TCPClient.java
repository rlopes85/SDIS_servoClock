package Servo;

import Master.Master_Clock;

import java.io.*;
import java.net.*;

/**
 * TODO implementar PI para modelo de relógio do slave
 * Implementação do slave do servo clock.
 * @author Ricardo Lopes
 */
public class TCPClient {
    private long t1;
    private long t4;
    private long t3_t2;
    private long delta = 0; //RTD


    public double getRTD(String s){

        delta = ((t4-t1) - (t3_t2))/2;
        return delta;
    }



    /**
     *
     * @param argv, Recebe como parametros o ip do servidor e o porto na forma ip porto
     *              ex. 192.168.1.34 5000 e ganho proporcional e Integral
     * @throws Exception
     */
    public static void main(String argv[]) throws Exception {

        String FromServer;
        String ToServer;


        long Kp;
        long Ki;

        if (argv.length < 4){
            System.out.println("Argumentos insuficientes !! \n p.f. iniciar programa na forma TCPCient <ip> <porto> <Kp> >Ki>");
            System.exit(-1);
        }

        /*
         * ganhos passados como argumentos
         */
        Kp = Long.valueOf(argv[2]);
        Ki = Long.valueOf(argv[3]);

        /*
         * Inicializar obj. compensacao PI
         */
        PIController pi = new PIController(Kp, Ki);

        /*
         * Inicializar histograma
         */
        Histograma hist;// = new Histograma(1,); fixme Definir parametros do istograma
        Master_Clock server = new Master_Clock(1,0);

        server.start();
        /*
         * Socket configuration
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
