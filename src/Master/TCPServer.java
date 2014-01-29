package Master;

import java.io.*;
import java.net.*;

/**
 * Implementação do Master Clock(relógio de referência).
 * Utilizando sockets TCP. O MC usa o System nanotime da JVM para
 * gerar relogio de referencia.
 * @author Ricardo Lopes
 */
public class TCPServer {
    /**
     *  Função para obter o tempo de referencia gerado pela JVM
     * @return  nanoTime, tempo de referência com precisão de nano segundos.
     */
    public static long MasterTime(){

        return System.nanoTime();
    }

    /**
     *  Implementação
     * @param argv, Numero do porto do socket do servidor.
     * @throws Exception
     */
    public static void main(String argv[]) throws Exception {
        String fromclient;
        String toclient;

        long overHead;
        int Port_Number = Integer.valueOf(argv[0]);
        MasterClock master = new MasterClock(1,0);//fixme passar parametros como argumentos
        //Initiate  the socket
        ServerSocket Server = new ServerSocket (Port_Number);
        System.out.println ("TCPServer Waiting for client on port " + Port_Number);

        while(true) {
            Socket connected = Server.accept();
            System.out.println( " THE CLIENT"+" "+
                    connected.getInetAddress() +":"+connected.getPort()+" IS CONNECTED ");
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader (connected.getInputStream()));

            PrintWriter outToClient =
                    new PrintWriter(
                            connected.getOutputStream(),true);
            master.start();

            while ( true ) {


                fromclient = inFromClient.readLine();
                //System.out.println(fromclient);
                if (fromclient.compareTo("TIME") == 0){
                    long t2 = master.getTime();
                    toclient = String.valueOf(t2);
                    overHead = master.getTime() - t2;
                    toclient = toclient+":"+overHead;
                    //outToClient.println(toclient);
                    System.out.println("SEND: " +toclient);//fixme debug
                }
                else if(fromclient.compareTo("CLOSE") == 0){
                    toclient = "Fim de comunicação";
                    //outToClient.println(toclient);
                    Server.close();
                    System.exit(1);
                }
                else{
                    toclient = "!Pedido desconhecido!";
                    //outToClient.println(toclient);
                    System.out.println("Pedido desconhecido!");
                }
                outToClient.println(toclient);
            }

        }
    }
}
