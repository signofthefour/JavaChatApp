package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private boolean isLogin = false;

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Please use cmd: java <hostname> <port>");
			return;
		}
		String hostname;
		int port;
		hostname = args[0];
		port = Integer.parseInt(args[1]);
		
		try (Socket socket = new Socket(hostname, port)) {
			System.out.println("Hey...");
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			//TODO: split into two thread do handle in and out simutainously

			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isLogin() { return isLogin; }
}
