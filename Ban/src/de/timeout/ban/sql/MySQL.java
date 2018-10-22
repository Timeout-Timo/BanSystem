package de.timeout.ban.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

import de.timeout.ban.config.Language;

/**
 * This class is a hook into a MySQL-Database.
 * 
 * 
 * @author Timeout
 */
public class MySQL {
	
	private static final String SQL_ERROR = "Could not create Statement";

	private String host, database, username, password;
	private int port;
	
	private Connection connection;
	
	public MySQL(String host, int port, String database, String username, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;

		connect();
	}
	
	/**
	 * Connect to MySQL-Database
	 */
	public void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
			Bukkit.getServer().getConsoleSender().sendMessage(Language.PREFIX.getMessage() + Language.MYSQL_CONNECTED.getMessage());
		} catch (SQLException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(Language.PREFIX.getMessage() + Language.MYSQL_FAILED.getMessage());
		}
	}
	
	/**
	 * Disconnect from MySQL-Database
	 */
	public void disconnect() {
		if(isConnected()) {
			try {
				connection.close();
				Bukkit.getServer().getConsoleSender().sendMessage(Language.PREFIX.getMessage() + Language.MYSQL_DISCONNECTED.getMessage());
			} catch (SQLException e) {
				Bukkit.getServer().getLogger().log(Level.INFO, "Could not close MySQL-Connection", e);
			}
		}
	}
	
	/**
	 * Checks if MySQL is connected
	 * @return the result
	 */
	public boolean isConnected() {
		try {
			connection.createStatement();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Get the MySQL-Connection
	 * @return the Connection
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * Returns an Object from Database
	 * return null if there is an Error.
	 * @param preparedStatement the Statement
	 * @param args the parameter
	 * @return the Result
	 */
	public Object getValue(String preparedStatement, String... args) {
		if(!isConnected()) connect();
		try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
			Object ans = null;
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next()) ans = rs.getObject(getDistinct(preparedStatement));
			}
			return ans;
		} catch(SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, SQL_ERROR, e);
		}
		return null;
	}
	
	/**
	 * Return the Distinct of the Statement
	 * @param preparedStatement the Statement
	 * @return the Distinct
	 */
	private String getDistinct(String preparedStatement) {
		return StringUtils.substringBetween(preparedStatement.toUpperCase(), "SELECT ", " FROM");
	}
	
	/**
	 * Converts the Statement with the parameters
	 * @param statement the Statement
	 * @param args the arguments
	 * @return the complete Statement
	 * @throws SQLException 
	 */
	private PreparedStatement convertStatement(String statement, String[] args) throws SQLException {
		//Do not close this Statement here!!
		PreparedStatement ps = connection.prepareStatement(statement);
		int index = 0;
		for(int i = 0; i < args.length; i++) {
			ps.setObject(index +1, args[index]);
			index++;
		}
		return ps;
	}
	
	/**
	 * Returns the TableName of the Statement
	 * @param preparedStatement the Statement
	 * @return the TableName
	 * @throws SQLException if a TableName doesn't exist.
	 */
	public String getTableName(String preparedStatement) throws SQLException {
		String[] split = preparedStatement.split(" ");
		for(int i = 0; i < split.length; i++) {
			if(split[i].equalsIgnoreCase("FROM"))return split[i +1];
		}
		throw new SQLException("Could not find tablename");
	}
	
	/**
	 * Update a Value in the Database
	 * @param preparedStatement the Statement
	 * @param args the arguments
	 */
	public void update(String preparedStatement, String... args) {
		if(!isConnected())connect();
		try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
			ps.executeUpdate();
		} catch(SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, SQL_ERROR, e);			
		}
	}
	
	/**
	 * Delete a Value from the Database
	 * @param preparedStatement the Statement
	 * @param args the arguments
	 */
	public void delete(String preparedStatement, String... args) {
		if(!isConnected()) connect();
		try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
			ps.execute();
		} catch(SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, SQL_ERROR, e);
		}
	}
	
	/**
	 * Insert a Value in the Database
	 * @param preparedStatement the Statement
	 * @param args the arguments
	 */
	public void insert(String preparedStatement, String... args) {
		if(!isConnected())connect();
		try(PreparedStatement ps = convertStatement(preparedStatement, args)) {
			ps.execute();
		} catch (SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, SQL_ERROR, e);
		}
	}
	
	/**
	 * Checks if a Statement has Results
	 * @param preparedStatement the Statement
	 * @param args the Arguments of the Statement
	 * @return the result
	 */
	public boolean hasResult(String preparedStatement, String... args) {
		if(!isConnected())connect();
		try(ResultSet rs = convertStatement(preparedStatement, args).executeQuery()) {
			return rs.next();
		} catch(SQLException e) {
			Bukkit.getLogger().log(Level.SEVERE, SQL_ERROR, e);
		}
		return false;
	}
}
