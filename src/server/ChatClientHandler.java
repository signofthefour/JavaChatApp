package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Date;

public class ChatClientHandler extends Thread{
	private ChatServer server;
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
		this.server = server;
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
		
		try {
			outputStream.write((name + "Login successfully at" + new Date() + "\n").getBytes());
			this.server.getClientList().add(this);
			String online = "";
			if (this.server.getClientList().size() != 0) {
				for (ChatClientHandler chatClient : this.server.getClientList()) {
					online += chatClient.getClientName() + '\n';
				}
				outputStream.write(("Online: " + online).getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(name + "Login successfully at" + new Date() + "\n");
		this.loginStatus = true;
		// TODO: Send all login client to the new and the new to all client
	}
	
	public void handleLogOut() {
		try {
			outputStream.write("Log out successfully.\n".getBytes());
			System.out.println("Disconnect with" + clientSocket+ "\n");
			this.server.getClientList().remove(this);
			outputStream.close();
			inputStream.close();
			reader.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
