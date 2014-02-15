package connection;


import java.sql.*;
import java.util.Properties;
import com.mysql.jdbc.Driver;

public class Connector {

	private static Connection con;
	
	private static Connector c;
	private static final String dbms = "mysql";
	private static final String databaseName = "myjournal";
	
	private static final String DRIVER_NAME="com.mysql.jdbc.Driver";
	
	/**
	 * @return c if made (using makeConnector()), otherwise null.
	 */
	public static Connector getConnector(){
		return c;
	}
	
	public static void makeConnector(String ip, int port,String username, String password){
		c = new Connector(ip,port, username, password);
	}
	
	public Connector(String ip, int port, String username, String password){
		Properties connectionProps = new Properties();
		connectionProps.put("user", username);
		connectionProps.put("password", password);
		try {
			con = getConnection(connectionProps,ip,port);
		} catch (SQLException e) {
			if(e.getErrorCode()==1045){	//Wrong account info
				System.out.println("Wrong account info, Connection failed.");
			}else{
				System.out.println("Connection failed");
			}
			System.exit(0);
		}
	}
	
	/**
	 * Returns an empty statement object to be used to execute MySQL commands.
	 * @return empty statement object.
	 * @throws SQLException if creating a statement failed
	 */
	public Statement getStatement() throws SQLException{
			return con.createStatement();
	}
	
	private Connection getConnection(Properties connectionProps, String ip, int port) throws SQLException {
	    if(con==null){
		    try {
		    	Class.forName("com.mysql.jdbc.Driver").newInstance();
		    	System.out.println("Loaded driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Could not load driver");
				e.printStackTrace();
				System.exit(0);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        con = DriverManager.getConnection(
	                   "jdbc:" + dbms + "://" +
	                   ip +
	                   ":" + port + "/"+databaseName,
	                   connectionProps);
		    System.out.println("Connected to database");
	    }
	    return con;
	}
	
}
