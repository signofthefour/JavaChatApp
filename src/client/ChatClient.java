package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.IOException;

public class ChatClient implements Runnable {
	private String name;
	private InputStream in;
	private OutputStream out;
	private Socket socket;
	
	class Control {
		private volatile boolean loginStatus = false;
		private boolean disconnect = false;
		private boolean reLogin = false;
	}
	
	final Control control = new Control();
	
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
	public boolean isLogin() { return control.loginStatus; }
	public void loginSuccess() {control.loginStatus = true; }
	public boolean reLogin() {return control.reLogin; }
	public void reLoginRequest() {control.reLogin = true; }
	public void reLoginDone() {control.reLogin = false; }
	private boolean disconnect = false;
	public void setName(String _name) {name = _name;}
	public void disconnect() {
		control.loginStatus = false;
		control.disconnect = true;
		try{
			socket.close();
		} catch (IOException e) {
			System.out.println("Not Disconnected");
		}
	}
	
	public boolean hasDisconnected() {
		return control.disconnect;
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
