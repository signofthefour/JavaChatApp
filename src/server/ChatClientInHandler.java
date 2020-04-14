package server;

import java.io.InputStream;

public class ChatClientInHandler implements Runnable {
	
	private InputStream inputStream;
	
	public ChatClientInHandler(InputStream inputStream, ) {
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		
	}
	
}
