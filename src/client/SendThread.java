package client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;


public class SendThread implements Runnable {
	private OutputStream outputStream;
	private ChatClient client;
	private String msg = "";
	private Scanner scn = new Scanner (System.in);
	
	public SendThread(OutputStream out, ChatClient client) {
		this.outputStream = out;
		this.client = client;
	}
	
	public void run() {
		loginRequest();
		while (!client.isLogin()){
//			System.out.println("Loading...");
		}
		System.out.println("You can chat");
		sendRequest();
	}
	
	public void send(String msg) throws IOException {
		msg = "<start>\n" + msg + "\n<end>\n"; 
		outputStream.write(msg.getBytes());
	}
	
	public void sendRequest() {
		while (!client.getSocket().isClosed() && client.isLogin()) {
			msg = "";
			
			if (scn.hasNext()) {
				System.out.println("[" + this.client.getName() + "]: " + (msg = scn.nextLine()));
				if ((msg = msg.trim()).length() > 0) {
					msg = "SEND MSG\n" + this.client.getName() + " mylove\n" + "\n" + msg + "\n";
				}
			}
			try {
				send(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loginRequest() {
		msg += "REQUEST LOGIN\n";
		msg += this.client.getName() + " server\n";
		msg += "\n";
		msg += this.client.getName() + "\n";
		try {
			send(msg);
			msg = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
//	public String loginRequest() {
//		
//	}
}
