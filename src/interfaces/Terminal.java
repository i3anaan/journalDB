package interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import util.Tags;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.exceptions.jdbc4.*;

import connection.Connector;
import connection.SQLCommander;

public class Terminal {
	/**
	 * De input die gelezen wordt.
	 */
	private static BufferedReader in = new BufferedReader(
			new InputStreamReader(System.in));

	private static Connector connector;

	/**
	 * Starts up our program, 0 arguments will decrypt, 1 argument will encrypt,
	 * more arguments does nothing.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		connector = Connector.getConnector();
		Tags tags = new Tags();
		tags.add("creativity");
		tags.add("book");
		try {
			SQLCommander.insertRecord(tags, "Dummy description","test enviroment");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		acceptInput();
	}

	/**
	 * This will start accepting input, till "Stahp!" is received. It will
	 * decrypt (currently)
	 */
	public static void acceptInput() {
		String input = "";
		try {
			while (input != null) {
				try {
					if (input.equals("Stahp!")) {
						System.out.println("NOOoooooooo......");
						System.exit(0);
					}
					Statement stmt = Connector.getConnector().getStatement();

					if (input.toLowerCase().startsWith("select") || input.toLowerCase().startsWith("show")) {
						System.out.println(input);
						ResultSet result = stmt.executeQuery(input);
						printResult(result);
					} else if (input.equals("RESET")) {
						System.out.println("RESETTING DATABASE");
						SQLCommander.reset();
					}else if (!input.equals("")) {
						stmt.execute(input);
						System.out.println("Executed SQL command: " + input);
					}

				} catch (MySQLSyntaxErrorException e) {
					System.err.println("SQL Syntax error!");
				} catch (SQLException e) {
					System.out.println("Error getting SQL statemnt");
					e.printStackTrace();
				}
				input = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printResult(ResultSet set) {
		try {
			if (set.first()) {
				java.sql.ResultSetMetaData meta = set.getMetaData();
				int columns = meta.getColumnCount();
				for(int i=1;i<=columns;i++){
					System.out.print(String.format("%-22s ",meta.getColumnName(i)));
				}
				System.out.print("\n");
				for(int i=1;i<=columns;i++){
					String type = meta.getColumnTypeName(i);
					if(type.equals("DATETIME")){
						System.out.print(String.format("%-22s ",set.getTimestamp(i)));
					}else if(type.equals("INT")){
						System.out.print(String.format("%-22s ",set.getDouble(i)));
					}else if(type.equals("VARCHAR")){
						System.out.print(String.format("%-22s ",set.getNString(i)));
					}
				}
				System.out.print("\n");
				while (!set.isLast()) {
					set.next();
					
					for(int i=1;i<=columns;i++){
						String type = meta.getColumnTypeName(i);
						if(type.equals("DATETIME")){
							System.out.print(String.format("%-22s ",set.getTimestamp(i)));
						}else if(type.equals("INT")){
							System.out.print(String.format("%-22s ",set.getDouble(i)));
						}else if(type.equals("VARCHAR")){
							System.out.print(String.format("%-22s ",set.getNString(i)));
						}
					}
					System.out.print("\n");
				}
			} else {
				System.out.println("Empty ResultSet");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
