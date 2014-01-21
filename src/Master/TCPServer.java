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

        long overHead = 0;
        int Port_Number = Integer.valueOf(argv[0]);
        Master_Clock master = new Master_Clock(1,0);//fixme passar parametros como argumentos
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

                if (fromclient.compareTo("TIME") == 0){
                    toclient = String.valueOf(master.getTime());
                    outToClient.println(toclient);
                    System.out.println("SEND: " + toclient);
                }
                else{
                    System.out.println("Pedido desconhecido!");//fixme enviar resposta de erro
                }
            }

        }
    }
}
