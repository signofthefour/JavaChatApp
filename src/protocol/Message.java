package protocol;

//<start>
//<METHOD> <COMMAND>
//<sender> <receiver>
//<blank line>
//(body)...
//<end>

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
	
	public Message() {
		msg = "";
		method = "";
		cmd = "";
		sender = "";
		receiver = "";
		body = "";
	}
	public Message(String s) {
		this.msg = s;
		init();
	}
	
	public void createNew(String msg) {
		this.msg = msg;
		init();
	}
	
	public void init() {
		System.out.println("=================");
		System.out.println(msg);
		String[] lines = msg.split("\n");
		if (lines.length < 3) return;
		// Get method and command
		this.method = lines[0].split(" ")[0];
		this.cmd 	= lines[0].split(" ")[1];
		this.sender = lines[1].split(" ")[0];
		this.receiver=lines[1].split(" ")[1];
		body = "";
		for (int i = 3; i < lines.length; i++) {
			body += lines[i] + '\n';
		}
	}
	
	public String getMethod() { return this.method; }
	
	public String getCommand() { return this.cmd; }
	
	public String getSender() {return this.sender; }
	
	public String getReceiver() {return this.receiver; }
	
	public String getBody() { return this.body; }
}
