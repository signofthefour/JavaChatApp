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
		boolean sendLoginRequest = false;
		while (!client.getSocket().isClosed()) {
			if (!this.client.isLogin() && !sendLoginRequest) {
				sendLoginRequest = true;
				msg += "REQUEST LOGIN\n";
				msg += this.client.getName() + " server\n";
				msg += "\n";
				msg += this.client.getName() + "\n";
				System.out.println(">???");
			}
			else {
				if (scn.hasNext() && client.isLogin()) {
					System.out.println("[" + this.client.getName() + "]: " + (msg = scn.nextLine()));
					msg = "SEND MSG\n" + "tandat mylove\n" + "\n" + msg + "\n"; 
				}
			}
			try {
				send(msg);
				msg = "";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void send(String msg) throws IOException {
		msg = "<start>\n" + msg + "\n<end>\n"; 
		outputStream.flush();
		outputStream.write(msg.getBytes());
	}
	
//	public String loginRequest() {
//		
//	}
}
