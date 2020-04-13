package client;

import java.io.IOException;
import java.io.OutputStream;

public class SendThread extends Thread {
	private OutputStream outputStream;
	private Client client;
	private String msg;
	
	public SendThread(OutputStream out,Client client) {
		this.outputStream = out;
		this.client = client;
	}
	
	public void work() {
		if (!this.client.isLogin()) {
			msg += "<start>\n";
			msg += "REQUEST LOGIN\n";
			msg += "client server\n";
			msg += "\n";
			msg += "tandat\n";
			msg += "<end>\n";
		}
		
		try {
			outputStream.write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(String msg) throws IOException {
		msg = "<start>\n" + msg + "\n<end>"; 
		outputStream.write(msg.getBytes());
	}
	
//	public String loginRequest() {
//		
//	}
}
