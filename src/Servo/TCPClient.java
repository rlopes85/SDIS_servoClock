package Servo;

import Master.MasterClock;

import java.io.*;
import java.net.*;

/**
 * Implementação do slave do servo clock.
 * @author Ricardo Lopes
 */
public class TCPClient {
    private static double t1;
    private static double t4;
    private static double t3_t2;
    private static double delta = 0; //RTD


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
        
        
        double Kp;
        double Ki;
        double skew = 1;
        double T = 100; //periodo em milisegundos
        double media = 0.0;
        double norm_erro [];
        double ref; //sinal de referencia
        double fdb = 0;	//sinal de feedback
        double u ;  //sinal de saida do controlo PI
        double y;   //resultado do controlo
        
        if (argv.length < 4){
            System.out.println("Argumentos insuficientes !! \n p.f. iniciar programa na forma TCPCient <ip> <porto> <Kp> >Ki>");
            System.exit(-1);
        }

        /*
         * ganhos passados como argumentos
         */
        Kp = Double.valueOf(argv[2]);
        Ki = Double.valueOf(argv[3]);

        /*
         *Malha de controlo
         */
        ControlLoop controlo = new ControlLoop(Kp,Ki,skew,T);
        /*
         * Socket configuration
         */
        InetAddress ip_server = InetAddress.getByName(argv[0]);
        int Port_Number = Integer.valueOf(argv[1]);

        ClientSocket socket = new ClientSocket(ip_server, Port_Number);
        
        socket.sendToServer(ToServer);
        socket.start();
        controlo.start();
        /*
         * Main loop
         */
        controlo.compensação.setKi(0.1);
        controlo.compensação.setKp(1);
        for (int i=0; i<500;i++){

            t1 = controlo.projecto.getSlaveClock();
            FromServer = socket.receiveFromServer();
            if((FromServer != null)){
                //System.out.println("iteração: "+i +": "+FromServer);

                String [] result = FromServer.split(":");

                ref = (Double.valueOf(result[0]))/1000000;     //converte de nanosegundos
                t3_t2 = (Double.valueOf(result[1]))/1000000;   //para milisegundos
                t4 = controlo.projecto.getSlaveClock();
                delta = getRTD(t1,t4,t3_t2);//rtd
                //System.out.println("delta: "+delta);
                //System.out.println("iteração: "+i +" <-> "+controlo.getR()+" compensa:  "+controlo.compensação.getU());
                //controlo.setR(ref);
                controlo.setR(ref+delta);
                //controlo.histograma.setData(controlo.getE()*1000000);//convert to nanosegundos
                //System.out.println("erro: "+controlo.getE()*1000000);
                //fdb = controlo.getY();
                System.out.println("Slave:"+controlo.getY());
                System.out.println("master:"+controlo.getR());

            }

            Thread.sleep(1000);

            //System.out.println(servoclock.getSlaveClock());
        }
     /*   Object erro [] = controlo.erro.toArray();
        for (int i=0; i < erro.length; i++){
            media += (((Double) erro[i]).doubleValue());//rms
        }
        media = (media)/2; //rms
        System.out.println("media: "+media);
        norm_erro = new double[erro.length];

        for (int i=0; i < erro.length; i++){
            norm_erro[i] = (((Double) erro[i]).doubleValue())-media;
            controlo.histograma.setData(norm_erro[i]);
        }
*/

        System.out.println("Media:"+controlo.histograma.getMedia());
        socket.closeConection();
        controlo.histograma.graphIt();
        //System.exit(1);
        /*
         * ------------------------------------------------

        //socket.sleep((long) 100.0);
        FromServer = socket.receiveFromServer();
        System.out.println(FromServer);
        String [] result = FromServer.split(":");
        
        t3_t2 = Double.valueOf(result[1]);
        
        delta = getRTD(t1,t4,t3_t2);
        
        ref = Double.valueOf(result[0]) + delta;
        pi.setFb(0.0);
        pi.setR(ref);
        
        u = pi.execPI();
        y = u*servoclock.getSlaveClock();
        pi.setFb(y);
        /* TODO refinar malha de controlo
         * ---------------------------
         */
    }
}
