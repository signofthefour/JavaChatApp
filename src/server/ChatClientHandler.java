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
import protocol.Message;

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
		outputStream = clientSocket.getOutputStream();
		inputStream = clientSocket.getInputStream();

		reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		String msg = "";
		Message message = null;
		while ((line = reader.readLine()) != null && message == null) {
			if (line.equals("<start>")) {
				line = "";
				while (!(line = reader.readLine()).equals("<end>")) {
					msg += line + "\n";	
				}
				message  = new Message(msg);
				handleMessage(message);
				message = null;
			}
		}
	}
	
	//TODO: create two thread to handle both send and receive msg
	
	public void handleMessage(Message msg) {
		if (msg.getMethod().equals("REQUEST")) {
			if (msg.getCommand().equals("LOGIN")) {
				handleLogin(msg.getBody(), "123");
				System.out.println("Trying to login: " + msg.getBody());
			}
		}
		if (!isLogin()) {
			try {
				outputStream.write("You have to login first.".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("[" + msg.getSender() +"]: " + msg.getBody());
		}
	}
	
	public void handleLogin(String name, String password) {
		this.clientName = name;
		this.clientPassword = password;
		
		String onlineClients;
		
		try {
			System.out.println(name + " login successfully at " + new Date() + "\n");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.onlineNotify(this.clientName + " is online.\n");
		this.chatServer.addClient(this);
		this.loginStatus = true;
	}
	
	public void handleLogOut() {
		try {
			outputStream.write("Log out successfully.\n".getBytes());
			System.out.println("Disconnect with" + clientSocket + "\n");
			
			this.offlineNotify(this.clientName + " is offline.\n");
			this.chatServer.getClientList().remove(this);
			
			outputStream.close();
			inputStream.close();
			reader.close();
			clientSocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onlineNotify(String loginMsg) {
		try {
			if (this.chatServer.getClientList().size() == 0) {
				return;
			} else {
				for (ChatClientHandler client : this.chatServer.getClientList()) {
					client.outputStream.write(loginMsg.getBytes());
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void offlineNotify(String logoutMsg) {
		try 
		{
			if (this.chatServer.getClientList().size() == 0) {
				return;
			} else {
				for (ChatClientHandler client : this.chatServer.getClientList()) {
					client.outputStream.write(logoutMsg.getBytes());
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
