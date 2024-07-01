package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class Database {

	public static final String DB_DRIVER_KEY = "db.driver";
	public static final String DB_URL_KEY = "db.url";
	public static final String DB_USER_KEY = "db.user";
	public static final String DB_PASSWORD_KEY = "db.password";

	private static Connection connection;
	private final Properties properties;

	public Database(Properties properties) throws FileNotFoundException, IOException {
		this.properties = properties;
	}

	public Connection getConnection() throws SQLException {
		if (connection != null) {
			return connection;
		}

		try {
			connect();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e + "could not connect");
		}

		return connection;
	}

	private void connect() throws ClassNotFoundException, SQLException {
		Class.forName(properties.getProperty(DB_DRIVER_KEY));
		System.out.println("Driver loaded");
		connection = DriverManager.getConnection(properties.getProperty(DB_URL_KEY),
				properties.getProperty(DB_USER_KEY), properties.getProperty(DB_PASSWORD_KEY));
		System.out.println("Database connected");
	}

	public void shutdown() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
			}
		}
	}

	public static Properties loadPropertiesFromFile(String propertiesFilePath) throws IOException {
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(propertiesFilePath)) {
			properties.load(input);
		}
		return properties;
	}

	public boolean tableExists(String tableName) throws SQLException {
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet resultSet = null;
		String rsTableName = null;

		try {
			resultSet = databaseMetaData.getTables(connection.getCatalog(), "%", "%", null);
			while (resultSet.next()) {
				rsTableName = resultSet.getString("TABLE_NAME");
				if (rsTableName.equalsIgnoreCase(tableName)) {
					return true;
				}
			}
		} finally {
			resultSet.close();
		}

		return false;
	}

	public List<Employee> getAllEmployees() throws SQLException {
		List<Employee> employees = new ArrayList<>();
		try (Connection connection = getConnection()) {
			String query = "SELECT ID, firstName, lastName, dob FROM dbo.A01285019_Employees";
			try (PreparedStatement statement = connection.prepareStatement(query);
					ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Employee employee = new Employee();
					employee.setId(resultSet.getString("ID"));
					employee.setFirstName(resultSet.getString("firstName"));
					employee.setLastName(resultSet.getString("lastName"));
					employee.setDob(resultSet.getDate("dob"));
					employees.add(employee);
				}
			}
		}
		shutdown();
		return employees;
	}

	public Employee findEmployeeById(String employeeId) throws SQLException {
		Employee employee = null;
		String query = "SELECT ID, firstName, lastName, dob FROM dbo.A01285019_Employees WHERE ID = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, employeeId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					employee = new Employee();
					employee.setId(resultSet.getString("ID"));
					employee.setFirstName(resultSet.getString("firstName"));
					employee.setLastName(resultSet.getString("lastName"));
					employee.setDob(resultSet.getDate("dob"));
				}
			}
		}
		shutdown();
		return employee;
	}

	public boolean createEmployee(Employee employee) throws SQLException {
		String query = "INSERT INTO dbo.A01285019_Employees (ID, firstName, lastName, dob) VALUES (?, ?, ?, ?)";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, employee.getId());
			statement.setString(2, employee.getFirstName());
			statement.setString(3, employee.getLastName());
			statement.setDate(4, new java.sql.Date(employee.getDob().getTime()));
			int rowsInserted = statement.executeUpdate();
			shutdown();
			return rowsInserted > 0;
		}
	}

	public boolean deleteEmployee(String employeeId) throws SQLException {
		String query = "DELETE FROM dbo.A01285019_Employees WHERE ID = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, employeeId);
			int rowsDeleted = statement.executeUpdate();
			shutdown();
			return rowsDeleted > 0;
		}

	}

}
