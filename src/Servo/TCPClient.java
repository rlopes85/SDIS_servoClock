package Servo;

import Master.MasterClock;

import java.io.*;
import java.net.*;

/**
 * Implementação do ciente TVP e  servo-clock.
 * @author Ricardo Lopes
 */
public class TCPClient {
    private static double t1;
    private static double t4;
    private static double t3_t2;
    private static double delta = 0; //RTD

    /**
     * Cáclulo de Round Trip Delay(RTD).
     * @param mt1       tempo do inicio do pedido
     * @param mt4       tempo em que foi obtida a resposta
     * @param mt3_t2    overhead de processamento no servidor
     * @return RTD
     */
    public static double getRTD(double mt1, double mt4, double mt3_t2){

        return ((mt4-mt1) - (mt3_t2))/2;
    }




    /**
     *
     * @param argv, Recebe como parametros o ip do servidor e o porto na forma ip porto
     *              ex. 192.168.1.34 5000 e ganho proporcional e Integral
     * @throws Exception
     */
    public static void main(String argv[]) throws Exception {

        String FromServer;
        String ToServer = "TIME";
        
        
        double Kp;          //ganho proporcional
        double Ki;          //ganho integral
        double skew = 1;    //skew
        double T = 100;     //periodo de amostragem do relogio do servo em milisegundos
        long T_up = 1000;   //periodo de pedidos ao servidor
        int nAmostras;
        double ref; //sinal de referencia
        /*
         * Verificação dos argumentos
         */
        if (argv.length < 5){
            System.out.printf("Argumentos insuficientes !! \n\r p.f. iniciar programa na forma TCPCient <ip> <porto> <Kp> >Ki> <N Amostras>");
            System.exit(-1);
        }

        /*
         * ganhos passados como argumentos
         */
        Kp = Double.valueOf(argv[2]);
        Ki = Double.valueOf(argv[3]);

        /*
         * Nº amostras do para o Histograma
         */
        nAmostras = Integer.parseInt(argv[4]);
        /*
         *Malha de controlo
         */
        ControlLoop controlo = new ControlLoop(Kp,Ki,skew,T);
        /*
         * Socket configuration
         */
        InetAddress ip_server = InetAddress.getByName(argv[0]);
        int Port_Number = Integer.valueOf(argv[1]);

        ClientSocket socket = new ClientSocket(ip_server, Port_Number,T_up);
        
        socket.sendToServer(ToServer);
        socket.start();
        controlo.start();
        /*
         * Main loop
         */
        controlo.compensação.setKi(0.1);
        controlo.compensação.setKp(1);
        for (int i=0; i<nAmostras;i++){

            t1 = controlo.projecto.getSlaveClock();
            FromServer = socket.receiveFromServer();

            if((FromServer != null)){
                System.out.println("Recebeu: "+FromServer);
                String [] result = FromServer.split(":");

                ref = (Double.valueOf(result[0]))/1000000;     //converte de nanosegundos
                t3_t2 = (Double.valueOf(result[1]))/1000000;   //para milisegundos
                t4 = controlo.projecto.getSlaveClock();
                delta = getRTD(t1,t4,t3_t2);//rtd

                controlo.setR(ref+delta);

                //System.out.println("Slave:"+controlo.getY());
                //System.out.println("master:"+controlo.getR());

            }

            Thread.sleep(T_up);

            //System.out.println(servoclock.getSlaveClock());
        }


        System.out.println("Media:"+controlo.histograma.getMedia());
        socket.closeConection();
        controlo.histograma.graphIt();

    }
}
