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
    private double u    = 0.0;
    private  double clock = 0.0;
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
        histograma = new Histograma(40,1.0,10.0);
    }

    @Override
    public void run(){

        projecto.start();

        do{
            this.calcularErro();
            compensação.setE(e);
            u=compensação.execPI();
            clock=projecto.getSlaveClock();
            y = r*((u)*(clock))/(1+(u)*(clock));
            fb = y;
            //System.out.println("feedb: "+fb+" U-> "+u+" S -> "+ projecto.getSlaveClock()+ " erro: "+ e);
            System.out.println(" erro: "+ e);
            histograma.setData(e);
            try {
                this.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        while (true);
    }

    /**
     *
     */
    public void calcularErro(){
        //calculo do sinal de erro
        e = r - fb;
        //return e;
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
    } //fixme no feedback in

    public double getY(){
        return this.y;
    }

    public double getR(){return this.r;}

    public double getE(){return this.e;}
    /**
     *
     */
    public void reset(){
        this.r  = 0.0;
        this.fb = 0.0;
        this.y  = 0.0;
    }

}
