package server;

import java.io.OutputStream;

import protocol.Message;

public class ChatClientOutHandler implements Runnable {
	
	private OutputStream outputStream;
	private ChatClientHandler chatClient;
	private Message message;

	public ChatClientOutHandler(OutputStream outputStream, ChatClientHandler chatClient) {
		this.outputStream  = outputStream;
		this.chatClient = chatClient;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized (message) {
			while (true) {
				if (message != null) {
					outputStream.write(message.toText().getByte());
				} else {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
