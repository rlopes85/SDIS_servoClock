package Servo;

public class ServoClock extends Thread{
	private double a; 	//skew
	private double T;	//Periodo de sincronizaco
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
		slave_clock += (a*T);
		}while(true);
	}
	
	public double getSlaveClock(){
		return slave_clock;
	}
	
	public void reset(){
		this.setA(0.0);
		this.setT(0.0);
		this.slave_clock = 0.0;
	}

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getT() {
		return T;
	}

	public void setT(double t) {
		this.T = t;
	}
}
