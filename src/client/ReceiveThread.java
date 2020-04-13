package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import protocol.Message;

public class ReceiveThread implements Runnable {
	private InputStream inputStream;
	private BufferedReader bf;
	private ChatClient client;
	Message message;
	String msg;
	
	public ReceiveThread(InputStream in, ChatClient client) {
		this.inputStream = in;
		this.client = client;
		bf = new BufferedReader(new InputStreamReader(in));
	}
	
	@Override 
	public void run() {
		String line;
		if (!client.getSocket().isClosed()) {
			try {
//				msg = "";
//				if ((line = bf.readLine()) == "<start>") {
//					while ((line = bf.readLine()) != "<end>") {
//						msg += line + "\n";
//					}
//				}
//				message = new Message(msg);
//				System.out.println("[" + message.getSender() + "]: " + message.getBody());
				while (!client.isLogin()) {
					System.out.println("==========================");
					if ((line = bf.readLine()) != null) {
						System.out.println("[server]: " + line);
						if (line.contains("200")) {
							System.out.println("Connected in receiver...");
							this.client.loginSuccess();
						} else {
							System.out.println("[server]: Please login.");
						}
					}
				}
				while (client.isLogin()) {
					if ((line = bf.readLine()) != null) {
						System.out.println("[server]: " + line);
					}
				}
				line = "";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getMsg() {
		return msg;
	}
}
