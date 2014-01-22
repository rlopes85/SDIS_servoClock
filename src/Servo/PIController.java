package Servo;

/**
 *
 * @author Ricardo Lopes
 */
public class PIController {

    private double Kp;          //factor para controlo proporcional
    private double Ki;          //factor para controlo integral
    private double r    = 0.0;  //entrada de referencia do controlador PI
    private double e    = 0.0;  //erro
    private double fb   = 0.0;  //feedback
    private double u    = 0.0;  //resulatado

    private double ePro = 0.0;  //erro Proporcional
    private double eTot = 0.0;  //soma dos erros para uso na integracao()

    /**
     * Constroi um objecto PI com com as contantes dadas para P e I
     * @param Kp coeficiente proporcional
     * @param Ki coeficiente integral
     */
    public PIController(double Kp, double Ki){
        this.Kp = Kp;
        this.Ki = Ki;
    }

    /**
     * Le as entradas, calcula as saidas e escreve para as saidas
     */
    private void calcular(){
        //calculo do sinal de erro
        e = r - fb;

        //integracao do erro
        eTot += e;

        //erro proporcional
        ePro = Kp*e;

        //execução do calculo do PI
        u = (ePro + Ki*eTot);
    }

    /**
     * Executa o calculo da compencacao a aplicar a "planta" (u[n])
     * @return Valor resulatante calculo do PI
     */
    public double execPI(){
        calcular();
        return u;
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

    /**
     * Reset ao erro total, erro proporcional e resultado do PI
     */
    public void reset(){
        this.eTot   = 0.0;
        this.ePro   = 0.0;
        this.u      = 0.0;
    }

}
