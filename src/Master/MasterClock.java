package Master;

/**
 * Implementação do Relógio de referência do Master
 * @author Ricardo Lopes
 */
public class MasterClock extends Thread{
    private int m;          //Rate de crescimento relógio
    private int b;          //Offset
    private long initVal;   //Valor inicial do relógio quando inicia programa
    private long tmpTime;
    private long refClock;

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
    public MasterClock(int m, int b){
        this.m = m;     
        this.b = b;
    }
    @Override
    public void run (){
        initVal = MasterTime();
        do {
            tmpTime = MasterTime();
            refClock = (tmpTime - initVal) * m + b;
        } while (true);

    }

    /**
     * Obter relógio local
     * @return valor do relógio
     */
    public long getTime(){

        return  refClock;
    }
}
