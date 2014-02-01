package Servo;

/**
 * Modelo do controlador PI
 * @author Ricardo Lopes
 */
public class PIController {

    private double Kp;          //factor para controlo proporcional
    private double Ki;          //factor para controlo integral
    private double e    = 0.0;  //erro
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
        this.e  = 0.0;
    }

    /**
     * Le as entradas, calcula as saidas e escreve para as saidas
     */
    private void calcular(){

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
     * Definição do valor de entrada docontrolados
     * @param err Diferença entre o sinal de referencia e o feedback da
     *          saída da malha de controlo(malha fechada
     */
    public void setE(double err){
        this.e = err;
    }

    /**
     * Atribuir novo ganho proporcional
     * @param P Ganho proporcional
     */
    public void setKp(double P){this.Kp = P;}

    /**
     * Atribuir novo ganho integral
     * @param I ganho integral
     */
    public void setKi(double I){this.Ki = I;}

    /**
     * Obter o erro
     * @return Erro
     */
    public double getE(){return this.e;}

    /**
     * Obter saída do PI(U)
     * @return U
     */
    public double getU(){return this.u;}
    /**
     * Reset ao erro total, erro proporcional e resultado do PI
     */
    public void reset(){
        this.eTot   = 0.0;
        this.ePro   = 0.0;
        this.u      = 0.0;
    }

}
