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
		while (!client.getSocket().isClosed()) {
			if (!this.client.isLogin()) {
				this.client.login();
				msg += "REQUEST LOGIN\n";
				msg += this.client.getName() + " server\n";
				msg += "\n";
				msg += this.client.getName() + "\n";
			}
			else {
				if (scn.hasNext()) {
					msg += "SEND MSG\n";
					msg += "tandat mylove\n";
					System.out.print("[" + this.client.getName() + "]: ");
					msg += scn.nextLine();
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
		outputStream.write(msg.getBytes());
	}
	
//	public String loginRequest() {
//		
//	}
}
