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
	Message message = new Message();
	String msg = "";
	
	public ReceiveThread(InputStream in, ChatClient client) {
		this.inputStream = in;
		this.client = client;
		bf = new BufferedReader(new InputStreamReader(in));
	}
	
	@Override 
	public void run() {
		String line, msg;
		while (true) {
			msg = "";
			try {
				if((line = bf.readLine()).equals("<start>")) {
					while (!((line = bf.readLine()).equals("<end>"))) {
						msg += line + "\n";
					}
					message = new Message(msg);
					if (message.getCommand().equals("200")) {
						this.client.loginSuccess();
						System.out.println("[" + message.getSender() + "]: " + message.getBody());
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while (client.isLogin()) {
			if (client.hasDisconnected()) break;
			msg = "";
			try {
				while ((line = bf.readLine()) == null) ;
				if(line.equals("<start>")) {
					try {
						while (!((line = bf.readLine()).equals("<end>"))) {
							msg += line + "\n";
						}
					}
					catch (IOException e) {
						client.disconnect();
						return;
					}
					message = new Message(msg);
					System.out.println("[" + message.getSender() + "]: " + message.getBody());
				}
			} catch (IOException e) {
				client.disconnect();
				return;
			}

		}

	}
	
	public String getMsg() {
		return msg;
	}
}
