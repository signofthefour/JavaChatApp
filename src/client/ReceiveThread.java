package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import protocol.Message;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;

public class ReceiveThread implements Runnable {
	private InputStream inputStream;
	private BufferedReader bf;
	private ChatClient client;
	Message message = new Message();
	String msg = "";
	
	public ReceiveThread(InputStream in, ChatClient client) {
		this.inputStream = in;
		this.client = client;
		bf = new BufferedReader(new InputStreamReader(in));
	}
	
	@Override 
	public void run() {
		String line, msg;
		while (true) {
			msg = "";
			try {
				if((line = bf.readLine()).equals("<start>")) {
					while (!((line = bf.readLine()).equals("<end>"))) {
						msg += line + "\n";
					}
					message = new Message(msg);
					handleMsg(message);
					if (client.isLogin()) break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while (client.isLogin()) {
			if (client.hasDisconnected()) break;
			msg = "";
			try {
				while ((line = bf.readLine()) == null) ;
				if(line.equals("<start>")) {
					boolean isHeaderEnd = false;
					boolean hasFile = false;
					char[] fileContentChar = null;
					byte[] fileContent = null;
					try {
						while (!((line = bf.readLine()).equals("<end>"))) {
							if (!isHeaderEnd && line.contains("FILE")) {
								hasFile = true;
							}
							int length;
							if (isHeaderEnd && hasFile) {
								length = Integer.parseInt(line);
								msg += line + "\n"; // length of file
								line = bf.readLine();
								msg += line + "\n"; // Name of file+
								fileContentChar =  new char[length];
								bf.read(fileContentChar);
								fileContent = new String(fileContentChar).getBytes();
							}
							if (line == "\n") isHeaderEnd = true;
							if (fileContent == null) msg += line + "\n";
						}
					}
					catch (IOException e) {
						client.disconnect();
						return;
					}
					if (fileContent != null) message.createNew(msg, fileContent);
					else message.createNew(msg);
					handleMsg(message);
				}
			} catch (IOException e) {
				client.disconnect();
				return;
			}

		}

	}
	
	public String getMsg() {
		return msg;
	}

	public void handleMsg(Message msg) {
		if (msg.getMethod().equals("NOTI")){
			if (message.getMethod().equals("NOTI")) {
				if (message.getCommand().equals("200")) {
					this.client.loginSuccess();
					this.client.setName(message.getReceiver());
					System.out.println("Name: "+ this.client.getName());
					System.out.println("New notification:\n" + message.getBody() + "From: " + msg.getSender()+ "\n");
				} else {
					client.reLoginRequest();
					System.out.println(message.getBody() + "\n");
				}
			}
		}
		else if (msg.getMethod().equals("RECV")) {
			if (msg.getCommand().equals("MSG")){
				System.out.println("[" + message.getSender() + "]: " + message.getBody());
				return;
			}
			if (msg.getCommand().equals("GROUP")) {
				System.out.println("[" + message.getSender() + " to " + message.getReceiver() + "]: " + message.getBody());
				return;
			}
			if (msg.getCommand().equals("FILE")){
				try {
					byte[] fileContent = msg.getFileContent();
					try (FileOutputStream fos = new FileOutputStream("/home/nguyendat/Documents/projects/ChatApp/src/client/hello.png")) {
   						fos.write(fileContent);
						System.out.println("[" + message.getSender() + "]: " + message.getBody());
   						fos.close(); 
						// There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
					}
					inputStream.close();
				} catch (IOException e) {
					System.out.println("Not good");
				}
				return;
			}
		}
	}
}
