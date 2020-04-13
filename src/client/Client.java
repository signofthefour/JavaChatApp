package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
		
		try (Socket socket = new Socket(hostname, port)) {
			System.out.println("Hey...");
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String msg = "";
			msg += "<start>\n";
			msg += "REQUEST LOGIN\n";
			msg += "client server\n";
			msg += "\n";
			msg += "tandat\n";
			msg += "<end>\n";
			
			outputStream.write(msg.getBytes());

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
}
