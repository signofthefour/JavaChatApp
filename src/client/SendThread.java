package client;

import java.io.*;
import java.util.Scanner;


public class SendThread implements Runnable {
	private OutputStream outputStream;
	private ChatClient client;
	private String msg = "";
	private Scanner scn = new Scanner (System.in);
	private String receiver = "";
	
	public SendThread(OutputStream out, ChatClient client) {
		this.outputStream = out;
		this.client = client;
	}
	
	public void run() {
		loginRequest();
		while (!client.isLogin()){
			if (client.reLogin()) {
				loginRequest();
				client.reLoginDone();
			}
//			System.out.println("Loading...");
		}
		System.out.println("You can chat");
		sendRequest();
	}
	
	public void send(String msg) throws IOException {
		msg = "<start>\n" + msg + "\n<end>\n\0";
		outputStream.write(msg.getBytes());
	}
	
	public void sendFile(byte[] fileContent, String fileName, String receiver) throws IOException {
		String header = "<start>\n"+"SEND FILE\n" + this.client.getName() + " " + receiver + "\n\n" + Integer.toString(fileContent.length) +  "\n" + fileName +"\n";
		byte[] headerBytes = header.getBytes();
		byte[] tailBytes = "\n<end>\n\0".getBytes();
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream( );
		byteOutputStream.write(headerBytes, 0, headerBytes.length);
		byteOutputStream.write(fileContent);
		byteOutputStream.write("\n<end>\n\0".getBytes());
		byte[] c = byteOutputStream.toByteArray();
		outputStream.write(c);
	}
	
	public void sendRequest() {
		while (!client.getSocket().isClosed() && client.isLogin()) {
			if (client.hasDisconnected()) return;
			msg = "";
			receiver = getReceiverName();
			if (scn.hasNext()) {
				System.out.println("[" + this.client.getName() + "]: " + (msg = scn.nextLine()));
				if ((msg = msg.trim()).length() > 0) {
					if (receiver.equals("server")  && msg.equals("exit")) {
						logoutRequest();
					} else if (msg.equals("file")) {
						try {
							loadFile("/home/nguyendat/Documents/projects/ChatApp/src/client/a.png", "SendThread.java",receiver);
							continue;
						} catch (IOException e) {
							System.out.println("Load file failure.");
						}	
					} else if (msg.equals("gr")) {
						requestNewGroup("", "");
						continue;
					} else if (msg.equals("gr1")) {
						sendToGroup("", "");
						continue;
					}
					else {
						msg = "SEND MSG\n" + this.client.getName() + " " + receiver + "\n" + "\n" + msg + "\n";
					}
				}
			}
			try {
				send(msg);
			} catch (IOException e) {
				break;
			}
		}
	}

	private void sendToGroup(String grName, String message) {
		grName = "group1";
		message = "WOWOWOWOWOWOWOOWWWOOWWOWOWOW";
		String grMsg = "SEND GROUP\n" + this.client.getName() + " " + grName + "\n\n"+ message + "\n";
		try {
			send(grMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	private void requestNewGroup(String name, String member) {
		name = "group1";
		member = "tandat\ndat";
		String msg = "REQUEST CREATE_GR\n" + this.client.getName() + " server\n\n"+ name + "\n" + member + "\n";
		try {
			send(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	public String getReceiverName () {
		return scn.nextLine();
	}
	
	public void loadFile(String filePath, String fileName,String receiver) throws IOException {
		File file = new File(filePath);
        	FileInputStream fin = null;
		byte fileContent[] = null;
        	try {
        	    	// create FileInputStream object
        	 	fin = new FileInputStream(file);
	         	fileContent = new byte[(int)file.length()];
        	 	   // Reads up to certain bytes of data from this input stream into an array of bytes.
           	 	fin.read(fileContent);
           	 	//create string from byte array
        	}
        	catch (FileNotFoundException e) {
            		System.out.println("File not found" + e);
        	}
        	catch (IOException ioe) {
            		System.out.println("Exception while reading file " + ioe);
        	}
        	finally {
            	// close the streams using close method
            		try {
                		if (fin != null) {
                    		fin.close();
                	}
            	}
            	catch (IOException ioe) {
                	System.out.println("Error while closing stream: " + ioe);
            		}
        	}
		if (fileContent != null) {
			sendFile(fileContent, fileName, receiver);
		} else {
			System.out.println("Load file failure");
		}		
	}
	public void loginRequest() {
		Scanner scn = new Scanner(System.in);
		System.out.println("===============LOGIN================");
		System.out.print("Email: ");
		String gmail = scn.nextLine();
		System.out.print("Password: ");
		String password =  scn.nextLine();
		System.out.println("====================================");
		msg = "REQUEST LOGIN\n";
		msg += this.client.getName() + " server\n";
		msg += "\n";
		msg += gmail + "\n";
		msg += password + "\n";
		try {
			send(msg);
			msg = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void logoutRequest() {
		msg = "REQUEST LOGOUT\n";
		msg += this.client.getName() + " server\n";
		msg += "\n";
		msg += this.client.getName() + "\n";
		try {
			send(msg);
			this.client.disconnect();
			msg = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
