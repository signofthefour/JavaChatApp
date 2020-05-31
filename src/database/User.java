package database;

public class User {
	private String name;
	private String password;
	private String email;
	private String sex;
	private String birth;

	private void setName(String _name) { name = _name;}
	private void setPassword(String _password) { password = _password;}
	private void setEmail(String _email) { email = _email;}
	private void setSex(String _sex) { sex = _sex;}
	private void setBirth(String _birth) { birth = _birth;}

	public String getName() {return name;}
	public String getEmail() {return email;}
	public String getSex() {return sex;}
	public String getBirth() {return birth;}

	public boolean verifyAccount (String _name, String _password) {
		return _name == name && _password == password;
	}

	public User(String _name, String _password, String _email, String _sex, String _birth) {
		setName(_name);
		setPassword(_password);
		setEmail(_email);
		setSex(_sex);
		setBirth(_birth);
	}
}
