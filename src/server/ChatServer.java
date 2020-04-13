package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class ChatServer {
	private ServerSocket serverSocket;
	private ArrayList<ChatClientHandler> clientList = new ArrayList<ChatClientHandler>(0);
	
	public ChatServer(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		System.out.println("About to accept client...");
	}
	
	public void start() throws IOException {
		while (true) {
			Socket clientSocket = this.serverSocket.accept();
			System.out.println("New request...");
			ChatClientHandler chatClientHandler = new ChatClientHandler(clientSocket, this);
			chatClientHandler.start();
		}
	}
	
	public ArrayList<ChatClientHandler> getClientList() {
		return clientList;
	}
	
	public void addClient(ChatClientHandler newClient) {
		this.clientList.add(newClient);
	}
}
