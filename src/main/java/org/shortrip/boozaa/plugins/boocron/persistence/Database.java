package org.shortrip.boozaa.plugins.boocron.persistence;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


/*
* Copyright (C) 2012  boozaa
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
public class Database extends SQLOperations {

	private String host, database, username, password;
	private SQLTYPE schema;
	private Connection connection;
	private File databaseFile;

	public Database(String host, String database, String username, String password) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
		this.schema = SQLTYPE.MySQL;
	}

	public Database(File databaseFile) {
		this.databaseFile = databaseFile;
		this.schema = SQLTYPE.SQLite;
	}

	/**
	 * Reopens the SQL connection if it is closed. This is invoked upon every
	 * query.
	 */
	public void refreshConnection() {
		if (connection == null) {
			initialise();
		}
	}

	/**
	 * Manually close the connection.
	 */
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialise a new connection. This will automatically create the database
	 * file if you are using SQLite and it doesn't already exist.
	 * 
	 * @return
	 */
	public boolean initialise() {
		if (schema == SQLTYPE.MySQL) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, username, password);
				return true;
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} else {
			if (!databaseFile.exists()) {
				try {
					databaseFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());
				return true;
			} catch (SQLException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Any query which does not return a ResultSet object. Such as : INSERT,
	 * UPDATE, CREATE TABLE...
	 * 
	 * @param query
	 */
	public void standardQuery(String query) throws SQLException {
		this.refreshConnection();
		super.standardQuery(query, this.connection);
	}

	/**
	 * Check whether a field/entry exists in a database.
	 * @param query
	 * @return Whether or not a result has been found in the query.
	 * @throws SQLException
	 */
	public boolean existanceQuery(String query) throws SQLException {
		this.refreshConnection();
		return super.sqlQuery(query, this.connection).next();
	}

	/**
	 * Any query which returns a ResultSet object. Such as : SELECT Remember to
	 * close the ResultSet object after you are done with it to free up
	 * resources immediately. ----- ResultSet set =
	 * sqlQuery("SELECT * FROM sometable;"); set.doSomething(); set.close();
	 * -----
	 * 
	 * @param query
	 * @return ResultSet
	 */
	public ResultSet sqlQuery(String query) throws SQLException {
		this.refreshConnection();
		return super.sqlQuery(query, this.connection);
	}

	/**
	 * Check whether the table name exists.
	 * 
	 * @param table
	 * @return
	 */
	public boolean doesTableExist(String table) throws SQLException {
		this.refreshConnection();
		return super.checkTable(table, this.connection);
	}
	
	
}
