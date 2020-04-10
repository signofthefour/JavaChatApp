package server;

import java.io.*;
import java.net.*;
import java.util.List;


public class ChatServer {
	private ServerSocket serverSocket;
	private List<ChatClientHandler> clientList;
	
	public ChatServer(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		System.out.println("About to accept client...");
	}
	
	public void start() throws IOException {
		while (true) {
			Socket clientSocket = this.serverSocket.accept();
			ChatClientHandler chatClientHandler = new ChatClientHandler(clientSocket, this);
			chatClientHandler.start();
		}
	}
	
	public List<ChatClientHandler> getClientList() {
		return clientList;
	}
}
