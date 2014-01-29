package Servo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocket extends Thread{
	private Socket clientSocket;
	//private BufferedReader inFromUser;
	private PrintWriter outToServer;
	private BufferedReader inFromServer;
	
	public String ToServer = null;
	public String FromServer;
	
	/**
	 * Alocacao de um covo socket 
	 * @param ip IP do servidor
	 * @param port	Porto a que se vai ligar
	 * @throws IOException
	 */
	public ClientSocket(InetAddress ip, int port) throws IOException{
        clientSocket = new Socket(ip, port);

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
        	//ToServer = inFromUser.readLine();
            //System.out.println(ToServer);
                //if (ToServer.equals("TIME")){
                    //System.out.println(ToServer);
                    outToServer.println(ToServer);
                //}
            try {
                //fixme tracar caso em que retorna null
				FromServer = inFromServer.readLine();
			} catch (IOException e) {
				System.out.println("!Erro de ligação impossivel ler mensagem do servidor!");
				e.printStackTrace();
			}
            //System.out.println("RECIEVED:" + FromServer);
            try {
                this.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

    /**
     *
     */
    public void closeConection(){
        this.sendToServer("CLOSE");
    }

    /**
     *
     * @param toSend
     */
	public void sendToServer(String toSend){
		ToServer = toSend;
	}

    /**
     *
     * @return message from server
     */
	public String receiveFromServer(){
		return FromServer;
	}
}
