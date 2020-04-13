package server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	public static void main (String[] args) throws IOException {
		// Create server socket
		int port = 8818;
		try {
			ServerSocket chatServerSocket = new ServerSocket(port);
			
			ChatServer chatServer = new ChatServer(chatServerSocket);
			chatServer.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
