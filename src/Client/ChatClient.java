package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {

	public static void main(String[] args) {
		if (args.length < 2) return;
		String hostname;
		int port;
		hostname = args[0];
		port = Integer.parseInt(args[1]);
		
		try (Socket socket = new Socket(hostname, port)) {
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String msg = "";
			msg += "REQUEST LOGIN\n";
			msg += "client server\n";
			msg += "tandat\n";
			msg += "<msgend>\n";
			outputStream.write(msg.getBytes());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
