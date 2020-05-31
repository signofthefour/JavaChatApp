package database;

import java.util.*;
import java.sql.*;

public class DatabaseControl {

    	public User checkLogin(String email, String password) throws SQLException,
            ClassNotFoundException {
        	String jdbcURL = "jdbc:mysql://localhost:3306/chatServer";
	        String dbUser = "root";
	        String dbPassword = "shinichikudo0408";

	        Class.forName("com.mysql.jdbc.Driver");
	        Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
	        String sql = "SELECT * FROM users WHERE email = ? and password = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setString(1, email);
	        statement.setString(2, password);

        	ResultSet result = statement.executeQuery();

	        User user = null;

	        // if (result.next()) {
	        //    user = new User();
	        //    user.setFullname(result.getString("fullname"));
	        //    user.setEmail(email);
	        //}

	        connection.close();

        	return user;
    	}

	public static void checkDatabase() throws SQLException, ClassNotFoundException {
		String jdbcURL = "jdbc:mysql://localhost:3306/";
	        String dbUser = "root";
        	String dbPassword = "root";

	        Class.forName("com.mysql.jdbc.Driver");
	        Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
		System.out.println("Hello World");
		connection.close();

    	}

	public static void main (String[] args) throws SQLException, ClassNotFoundException {
		checkDatabase();
	}
}
