package Servo;



import java.util.ArrayList;
import java.util.List;


/**
 * @author Ricardo Lopes
 */
public class ControlLoop extends Thread {
    PIController compensação;
    ServoClock projecto;
    Histograma histograma = null;


    private double r    = 0.0;  //entrada de referencia do controlador PI
    private double fb   = 0.0;  //feedback
    private double e    = 0.0;  //erro (diferença entre o sinal de referencia e o feedback
    private double y    = 0.0;  //saida do controlo
    private double u    = 0.0;
    private double clock = 0.0;
    private long Ts;
    /**
     * Cconstrói uma nova malha de controlo com projeto servo-clock e compensação PI
     * @param k_p ganho proporcional
     * @param k_i
     * @param skew
     * @param period
     */
    public ControlLoop(double k_p, double k_i, double skew, double period) {
        Ts = (long) period;
        projecto = new ServoClock(skew, period);
        compensação = new PIController(k_p,k_i);

        histograma = new Histograma(200,0,200);
    }

    @Override
    public void run(){

        projecto.start();

        do{
            this.calcularErro();
            compensação.setE(e);
            u = compensação.execPI();
            clock = projecto.getSlaveClock();
            y = r*((u)*(clock))/(1+(u)*(clock)); //ft da malha de controlo
            fb = y;

            histograma.setData(e*1000000); //adiciana valores ao histograma em nanosegundos

            try {
                this.sleep(Ts);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        while (true);
    }

    /**
     * Calcla o erro do servo-clock em relação ao relógio de referẽncia
     */
    public void calcularErro(){
        //calculo do sinal de erro
        e = r - fb;

    }
    /**
     * Passagem do valor do relógio de referência (r[n])
     * @param ref valor de referência
     */
    public void setR(double ref){
        this.r = ref;
    }

    /**
     * Obter valor do Servo-Clock já compensado
     * @return resultado da ft da malha de controlo
     */
    public double getY(){
        return this.y;
    }

    /**
     * Obter valor do relógio de referência
     * @return relógio de referências
     */
    public double getR(){return this.r;}

    /**
     * Obter valor do erro
     * @return erro
     */
    public double getE(){return this.e;}
    /**
     * Efectua reset a malha de controlo
     */
    public void reset(){
        this.r  = 0.0;
        this.fb = 0.0;
        this.y  = 0.0;
    }

}
