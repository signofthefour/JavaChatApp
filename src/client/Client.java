package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Please use cmd: java <hostname> <port>");
			return;
		}
		String hostname;
		int port;
		hostname = args[0];
		port = Integer.parseInt(args[1]);
		
		try {
			Socket socket = new Socket(hostname, port);
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			
			Thread chatClient = new Thread(new ChatClient("", inputStream, outputStream, socket));
			chatClient.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
