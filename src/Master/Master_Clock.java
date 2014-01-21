package Master;

/**
 * Implementação do Relógio de referência
 * @author Ricardo Lopes
 */
public class Master_Clock extends Thread{
    public int m, b;
    protected long initVal, tmpTime;
    public long refClock;

    /**
     * Função para obter o tempo de referencia gerado pela JVM
     * @return  nanoTime, tempo de referência com precisão de nano segundos.
     */
    public static long MasterTime(){

        return System.nanoTime();
    }

    /**
     * Contrutor do relógio de referência
     * @param m Rate de crescimento do relógio.
     * @param b Offset
     */
    public Master_Clock(int m, int b){
        this.m = m;
        this.b = b;
    }
    public void run (){
        initVal = MasterTime();
        do {
            tmpTime = MasterTime();
            refClock = (tmpTime - initVal) * m + b;
        } while (true);

    }
    public long getTime(){

        return  refClock;
    }
}
