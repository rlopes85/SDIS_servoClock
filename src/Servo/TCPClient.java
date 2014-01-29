package Servo;

import Master.MasterClock;

import java.io.*;
import java.net.*;

/**
 * TODO implementar PI para modelo de relógio do slave
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
        double skew = 1.0001;
        double T = 1000000; //periodo em usegundos
        double time = 0.0;
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
         * Inicializar obj. compensacao PI
         */
       // PIController pi = new PIController(Kp, Ki);

        /*
         * Inicializar histograma
         */
/*        Histograma hist;// = new Histograma(1,); fixme Definir parametros do istograma
        
        *//*
         * Inicializar relogio do servo
         *//*
        ServoClock servoclock = new ServoClock(skew, T);
        servoclock.start();*/

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

        for (int i=0; i<20;i++){
            //t1 = controlo.projecto.getSlaveClock();
            FromServer = socket.receiveFromServer();
            if((FromServer != null)){
                //System.out.println("iteração: "+i +": "+FromServer);
                //t4 = controlo.projecto.getSlaveClock();
                String [] result = FromServer.split(":");

                ref = Double.valueOf(result[0]);
                t3_t2 = Double.valueOf(result[1]);
                //System.out.println("iteração: "+i +": "+t3_t2);
                //delta = getRTD(t1,t4,t3_t2);
                System.out.println("iteração: "+i +" <-> "+controlo.getR()+" compensa:  "+controlo.compensação.getU());
                controlo.setR(ref);
                controlo.setFb(fdb);
                fdb = controlo.getY();
                System.out.println("feed back:"+fdb);
                System.out.println("erro:"+controlo.getE());

            }

            Thread.sleep(1000);

            //System.out.println(servoclock.getSlaveClock());
        }
        socket.closeConection();
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
