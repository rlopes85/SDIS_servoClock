package Servo;

import java.util.ArrayList;


/**
 * @author Ricardo Lopes
 */
public class ControlLoop extends Thread {
    PIController compensação;
    ServoClock projecto;
    Histograma histograma = null;
    ArrayList   erro;

    private double r    = 0.0;  //entrada de referencia do controlador PI
    private double fb   = 0.0;  //feedback
    private double e    = 0.0;  //erro (diferença entre o sinal de referencia e o feedback
    private double y    = 0.0;  //saida do controlo
    /**
     *
     * @param k_p
     * @param k_i
     * @param skew
     * @param period
     */
    public ControlLoop(double k_p, double k_i, double skew, double period) {
        projecto = new ServoClock(skew, period);
        compensação = new PIController(k_p,k_i);
        erro = new ArrayList();

    }

    @Override
    public void run(){

        projecto.start();

        do{
            compensação.setE(this.calcularErro());
            y = (compensação.execPI())*(projecto.getSlaveClock());
        }
        while (true);
    }

    /**
     *
     */
    public double calcularErro(){
        //calculo do sinal de erro
        e = r - fb;
        return e;
    }
    /**
     * Passagem do valor de referencia (r[n])
     * @param ref
     */
    public void setR(double ref){
        this.r = ref;
    }


    /**
     *  Passagem do valor de feedback (y[n])
     * @param feedback o valor de feedback desejado
     */
    public void setFb(double feedback){
        this.fb = feedback;
    }

    public double getY(){
        return y;
    }

    /**
     *
     */
    public void reset(){
        this.r  = 0.0;
        this.fb = 0.0;
        this.y  = 0.0;
    }

}
