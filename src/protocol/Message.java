package protocol;

//<METHOD> <COMMAND>
//<sender> <receiver>
//<blank line>
//<body>
//<msgend>

// METHOD:
//		REQUEST: 
//			CMD: LOGIN, LOGOUT
//		SEND: 
//			 CMD: MSG, GROUP

public class Message {
	
	private String msg;
	private String method;
	private String cmd;
	private String sender;
	private String receiver;
	private String body = "";
	
	public Message(String s) {
		this.msg = s;
		handle();
	}
	
	public void handle() {
		String[] lines = msg.split("\n");
		// Get method and cmd
		this.method = lines[0].split(" ")[0];
		this.cmd 	= lines[0].split(" ")[1];
		this.sender = lines[1].split(" ")[0];
		this.receiver=lines[1].split(" ")[1];
		for (int i = 3; i < lines.length; i++) {
			body += lines[1];
		}
	}
	
	public String getMethod() { return this.method; }
	
	public String getCommand() { return this.cmd; }
	
	public String getSender() {return this.sender; }
	
	public String getReceiver() {return this.receiver; }
	
	public String getBody() { return this.body; }
}
