package Master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Chamada ao sistema
 * @author Ricardo Lopes
 *
 */
public class SysCall {

    String result = "";

    /**
     * Execução de chamada ao sistema
     * @param command  Sring com o comando a executar pelo SO.
     */
    public SysCall (String command){

        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(command);
            BufferedReader stdImput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String buff;
            while ((buff = stdImput.readLine()) != null) {
                result = buff;
                System.out.println(result + "Success");
            }
            while ((buff = stdError.readLine()) != null){
                result = buff;
                System.out.println(result + "fail");
            }
        } catch (IOException e) {
            System.out.println("Ocurreu uma excepção !!");
            e.printStackTrace();
        }


    }
}
