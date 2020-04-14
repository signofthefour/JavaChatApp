package server;

import java.io.IOException;
import java.io.OutputStream;

import protocol.Message;

public class ChatClientOutHandler implements Runnable {
	
	private OutputStream outputStream;
	private Message message;

	public ChatClientOutHandler(OutputStream outputStream, Message msg) {
		this.outputStream  = outputStream;
		this.message = msg;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (message != null) {
				try {
					outputStream.write(message.toText().getBytes());
					message = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				continue;
			}
		}
	}
}
