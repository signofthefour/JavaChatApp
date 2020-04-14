package server;

import java.io.OutputStream;

public class ChatClientOutHandler implements Runnable {
	
	private OutputStream outputStream;

	public ChatClientOutHandler(OutputStream outputStream) {
		this.outputStream  = outputStream;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
