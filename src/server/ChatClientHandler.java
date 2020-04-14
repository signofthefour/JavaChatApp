package server;
//TODO: Split into two thread to handle in and out simutainously
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import protocol.Message;

import server.ChatServer;

public class ChatClientHandler extends Thread{
	class ChatQueue {
		private volatile Queue<Message> messageQueue = new LinkedList<Message>();
		
		public Message next() {
			return this.messageQueue.poll();
		}
		
		public void add(Message message) {
			this.messageQueue.add(message);
		}
		
		public boolean hasNext() {
			return this.messageQueue.peek() != null;
		}
	}
	
	final ChatQueue chatQueue = new ChatQueue();
	
	private ChatServer 	chatServer;
	private Socket 		clientSocket;
	private String 		clientName;
	private String 		clientPassword;
	OutputStream 		outputStream;
	InputStream 		inputStream;
	BufferedReader 		reader;
	private boolean 	loginStatus = false;
	private Thread chatClientInHandler;
	private Thread chatClientOutHandler;
	
	public boolean isLogin() { return loginStatus; }
	public String getClientName() { return clientName; }
	public boolean isConnected() { return clientSocket.isConnected(); }
	
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
		
		ChatClientInHandler chatClientInput = new ChatClientInHandler(inputStream, this);
		ChatClientOutHandler chatClientOutput = new ChatClientOutHandler(outputStream);
		
		chatClientInHandler = new Thread(chatClientInput);
		chatClientOutHandler = new Thread(chatClientOutput);
		
		chatClientInHandler.start();
		chatClientOutHandler = new Thread();
		
		
		// TODO: synchronize the chat queue
		synchronized(chatClientInHandler) {
			while (this.isConnected()) {
				if (this.chatQueue.hasNext()) {
					handleMessage(this.chatQueue.next());
				} else  {
					try {
						chatClientInHandler.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void handleMessage(Message msg) {
		// REQUEST: LOGIN
		if (msg.getMethod().equals("REQUEST")) {
			if (msg.getCommand().equals("LOGIN")) {
				handleLogin(msg.getSender(), "123");
			}
		}
		// NOT LOGIN YET
		else if (!isLogin()) {
			try {
				outputStream.write("You have to login first.".getBytes());
				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		// HAS BEEN LOGIN 
		} else {
			try {
				System.out.println("[" + msg.getSender() +"]: " + msg.getBody());
				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void handleLogin(String name, String password) {
		this.clientName = name;
		this.clientPassword = password;
		
		String onlineClients;
		
		try {
			ArrayList<ChatClientHandler> onlineList  = this.chatServer.getClientList();
			outputStream.write("SEND 200\n".getBytes());
			if (onlineList.size() == 0) {
				outputStream.write("Noone online\n".getBytes());
			}
			else {
				onlineClients = "";
				for (ChatClientHandler chatClient : onlineList) {
					onlineClients += chatClient.getClientName() + "\n";
				}
				outputStream.write(("Online: \n" + onlineClients).getBytes());
			}
			System.out.println(name + " login successfully at " + new Date() + "\n");
			this.outputStream.flush();
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
			outputStream.flush();
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
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
