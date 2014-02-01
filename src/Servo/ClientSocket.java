package Servo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocket extends Thread{
	private Socket clientSocket;
	private PrintWriter outToServer;
	private BufferedReader inFromServer;
	private long T;
	public String ToServer = null;
	public String FromServer;
	
	/**
	 * Alocacao de um covo socket 
	 * @param ip IP do servidor
	 * @param port	Porto a que se vai ligar
	 * @throws IOException
	 */
	public ClientSocket(InetAddress ip, int port, long T) throws IOException{
        clientSocket = new Socket(ip, port);
        this.T = T;
	}
    public void run(){
    	
    	//inFromUser = new BufferedReader(new InputStreamReader(System.in));

        try {
			outToServer = new PrintWriter(clientSocket.getOutputStream(),true);
			inFromServer = new BufferedReader(new InputStreamReader(
	                clientSocket.getInputStream()));
	    	
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        
		while (true) {

                outToServer.println(ToServer);

            try {
                //fixme tracar caso em que retorna null
				FromServer = inFromServer.readLine();
			} catch (IOException e) {
				System.out.println("!Erro de ligação impossivel ler mensagem do servidor!");
				e.printStackTrace();
			}

            try {
                this.sleep(T);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

    /**
     * fechar ligação com o servidor
     */
    public void closeConection(){
        this.sendToServer("CLOSE");
    }

    /**
     * Definir mensagem a enviar para o servidor
     * @param toSend String a enviar
     */
	public void sendToServer(String toSend){
		ToServer = toSend;
	}

    /**
     * Obter a resposta do servidor
     * @return message from server
     */
	public String receiveFromServer(){
		return FromServer;
	}
}
