package client;

import java.io.IOException;
import java.io.OutputStream;

public class SendThread extends Thread {
	private OutputStream outputStream;
	
	public SendThread(OutputStream out) {
		this.outputStream = out;
	}
	
	public void send(String msg) throws IOException {
		outputStream.write(msg.getBytes());
	}
}
