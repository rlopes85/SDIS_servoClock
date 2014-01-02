package Master;

/**
 * Created by rlopes on 12/3/13.
 */
import java.io.*;
import java.net.*;

/**
 * Implementação do Master Clock(relógio de referência).
 * Utilizando sockets TCP. O MC usa o System nanotime da JVM para
 * gerar relogio de referencia.
 * @
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
     *
     * @param argv, Numero do porto do socket do servidor.
     * @throws Exception
     */
    public static void main(String argv[]) throws Exception {
        String fromclient;
        String toclient;
        int Port_Number = Integer.valueOf(argv[0]);

        long tempo_master;
        ServerSocket Server = new ServerSocket (Port_Number);

        System.out.println ("TCPServer Waiting for client on port " + Port_Number);

        while(true) {
            Socket connected = Server.accept();
            System.out.println( " THE CLIENT"+" "+
                    connected.getInetAddress() +":"+connected.getPort()+" IS CONNECTED ");

            BufferedReader inFromUser =
                    new BufferedReader(new InputStreamReader(System.in));

            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader (connected.getInputStream()));

            PrintWriter outToClient =
                    new PrintWriter(
                            connected.getOutputStream(),true);

            while ( true ) {
                tempo_master = MasterTime();
                System.out.println("SEND(Type Q or q to Quit):");
                System.out.println("System nanoTime" + tempo_master);

                toclient = inFromUser.readLine();

                if ( toclient.equals ("q") || toclient.equals("Q") ) {
                    outToClient.println(toclient);
                    connected.close();
                    break;
                }
                else {
                    outToClient.println(toclient);
                }

                fromclient = inFromClient.readLine();

                if ( fromclient.equals("q") || fromclient.equals("Q") ) {
                    connected.close();
                    break;
                }

                else {
                    System.out.println( "RECIEVED:" + fromclient );
                }

            }

        }
    }
}
