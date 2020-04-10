package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import server.ChatServer;

public class ChatClientHandler extends Thread{
	private ChatServer chatServer;
	private Socket clientSocket;
	private String clientName;
	private String clientPassword;
	OutputStream outputStream;
	InputStream inputStream;
	BufferedReader reader;
	private boolean loginStatus = false;
	
	public boolean isLogin() { return loginStatus; }
	public String getClientName() { return clientName; }
	
	public ChatClientHandler(Socket clientSocket, ChatServer server) {
		this.clientSocket = clientSocket;
		this.chatServer = server;
	}
	
	@Override
	public void run() {
		try {
			handleSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleSocket() throws IOException {
		System.out.println("Connected with: " + clientSocket);
		outputStream = clientSocket.getOutputStream();
		inputStream = clientSocket.getInputStream();

		reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		
		while ( (line = reader.readLine()) != null) {
			handleAction(line);
			if (!isLogin()) {
				outputStream.write("You have to login before chat anything\n".getBytes());
				continue;
			}
		}
	}
	
	public void handleAction(String line) {
		String[] token = line.split(" ");
		if (token.length == 3 && token[0].equals("login")) {
			handleLogin(token[1], token[2]);
		}
		if (token.length == 1 && token[0].equals("quit")) {
			handleLogOut();
		}
	}
	
	public void handleLogin(String name, String password) {
		this.clientName = name;
		this.clientPassword = password;
		
		String onlineClients;
		
		try {
			System.out.println(name + " login successfully at" + new Date() + "\n");
			ArrayList<ChatClientHandler> onlineList  = this.chatServer.getClientList();
			if (onlineList.size() == 0) {
				outputStream.write("Noone online\n".getBytes());
			}
			else {
				onlineClients = "";
				for (ChatClientHandler chatClient : onlineList) {
					onlineClients += chatClient.getClientName() + '\n';
				}
				outputStream.write(("Online: \n" + onlineClients).getBytes());
			}
			this.chatServer.addClient(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.loginStatus = true;
}
	
	public void handleLogOut() {
		try {
			outputStream.write("Log out successfully.\n".getBytes());
			System.out.println("Disconnect with" + clientSocket+ "\n");
//			this.chatServer.getClientList().remove(this);
			outputStream.close();
			inputStream.close();
			reader.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
