package Servo;

/**
 * Modelo da fonte local de relógio do servo-clock
 * @author Ricardo Lopes
 */
public class ServoClock extends Thread{
	private double a; 	//skew
	private double T;	//Tick do servo-clock
	public  double slave_clock;
	
	/**
	 *  Construtor do relogio do servodor, inicializa o contador a zero
	 *  e define o skew e o periodo de sincronizacao 
	 * @param a Skew	
	 * @param T Periodo de sincronizacao
	 */
	public ServoClock(double a, double T) {
		this.setA(a);
		this.setT(T);
		this.slave_clock = 0.0;
	}
	@Override
	public void run(){
		do{
            try {
                sleep((long)T);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            slave_clock += (a*T);
		}while(true);

	}

    /**
     * Obter o valor da fonte de relógio do servo-clock
     * @return
     */
	public double getSlaveClock(){
		return slave_clock;
	}

    /**
     * Reiniciar relógio
     */
	public void reset(){
		this.setA(0.0);
		this.setT(0.0);
		this.slave_clock = 0.0;
	}

    /**
     * obter valor de skew
     * @return skew
     */
	public double getA() {
		return a;
	}

    /**
     * Definir valor do "skew"
     * @param a Skew
     */
	public void setA(double a) {
		this.a = a;
	}

    /**
     * Obter valor do tick do servo-clock
     * @return tick do servo-clock
     */
	public double getT() {
		return T;
	}

    /**
     * Definir valor do tick do servo-clock
     * @param t
     */
	public void setT(double t) {
		this.T = t;
	}
}
