package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatClient implements Runnable {
	private String name;
	private InputStream in;
	private OutputStream out;
	private boolean isLogin;
	private Socket socket;
	
	public ChatClient(String name, InputStream in, OutputStream out, Socket socket) {
		this.name = name;
		this.in = in;
		this.out = out;
		this.socket = socket;
	}
	
	public String getName() { return name; }
	public Socket getSocket() { return socket; }
	public InputStream getInputStream() { return in; }
	public OutputStream getOutputStream() { return out; }
	public boolean isLogin() { return isLogin; }
	public void login() {this.isLogin = true; }
	public void disconnect() {
		this.isLogin = false;
	}
	
	@Override
	public void run() {
		SendThread send = new SendThread(out, this);
		ReceiveThread recv = new ReceiveThread(in, this);
		Thread sender = new Thread(send);
		Thread receiver = new Thread(recv);
		sender.start();
		receiver.start();
	}
}
