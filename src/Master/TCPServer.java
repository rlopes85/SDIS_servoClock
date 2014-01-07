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
     *  TODO rever formato das mensagens
     * @param argv, Numero do porto do socket do servidor.
     * @throws Exception
     */
    public static void main(String argv[]) throws Exception {
        String fromclient;
        String toclient;
        int Port_Number = Integer.valueOf(argv[0]);
        SysCall time= new SysCall("date +\"%N\"");
        System.out.println( "Syscall result: " + time.result);

        long init_time;
        ServerSocket Server = new ServerSocket (Port_Number);

        System.out.println ("TCPServer Waiting for client on port " + Port_Number);

        while(true) {
            Socket connected = Server.accept();
            System.out.println( " THE CLIENT"+" "+
                    connected.getInetAddress() +":"+connected.getPort()+" IS CONNECTED ");
                    init_time = MasterTime();
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader (connected.getInputStream()));

            PrintWriter outToClient =
                    new PrintWriter(
                            connected.getOutputStream(),true);

            while ( true ) {


                fromclient = inFromClient.readLine();

                if (fromclient.compareTo("TIME") == 0){
                    toclient = String.valueOf(init_time - MasterTime());
                    outToClient.println(toclient);
                    System.out.println("SEND: " + toclient);
                }
            }

        }
    }
}
