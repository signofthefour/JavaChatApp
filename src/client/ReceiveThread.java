package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReceiveThread {
	private InputStream inputStream;
	private BufferedReader bf;
	String msg;
	
	public ReceiveThread(InputStream in) {
		this.inputStream = in;
		bf = new BufferedReader(new InputStreamReader(in));
	}
	
	public void get() throws IOException {
		String line;
		msg = "";
		if ((line = bf.readLine()) == "<start>") {
			while ((line = bf.readLine()) != "<end>") {
				msg += line + "\n";
			}
		}
	}
	
	public String getMsg() {
		return msg;
	}
}
