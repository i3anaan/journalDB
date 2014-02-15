package connection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import util.Tags;

public class SQLCommander {

	private static final String tableName = "Notes";

	public static String makeInsert(String[] tags, String description){
		//return "INSERT INTO "+tableName+" VALUES("
		return null;
	}
	
	/**
	 * Gives a ArrayList<String> which contains all the possible tags (extracted from the tag table)
	 * @param stat 	a statement to use
	 * @return ArrayList<String> which contains all the possible tags, empty ArrayList if there are no tags.
	 * @throws SQLException if something goes wrong
	 */
	public static Tags getTags(int inuse) throws SQLException{
		Statement stat = Connector.getConnector().getStatement();
		Tags arr = new Tags();
		ResultSet set = stat.executeQuery("SELECT * FROM taglist");// WHERE inuse =="+inuse+";");
		if (set.first()) {
			arr.add(set.getNString("tag"));
			while (!set.isLast()) {
				set.next();
				arr.add(set.getNString("tag"));
			}
		}
		return arr;
	}
	
	/**
	 * Inserts a record into the database
	 * @param tags
	 * @param description
	 * @param location
	 * @throws SQLException
	 * @require description.length()<=500, location.length()<=50
	 */
	public static void insertRecord(Tags tags,String description,String location) throws SQLException{
		Statement stat = Connector.getConnector().getStatement();
		ResultSet set = stat.executeQuery("SELECT tag FROM records;");
		int tagIndex;
		if(set.last()){
			tagIndex = set.getInt("tag")+1;
		}else{
			tagIndex = 0;
		}
		
		stat = Connector.getConnector().getStatement();
		System.out.println("INSERT INTO records (tag,location,description) VALUES("+tagIndex+",'"+location+"','"+description+"');");
		stat.execute("INSERT INTO records (tag,location,description) VALUES("+tagIndex+",'"+location+"','"+description+"');");
		
		if(tags!=null){
			stat = Connector.getConnector().getStatement();
			for(int i=0;i<tags.size();i++){
				stat.addBatch("INSERT INTO tags (id,tag) VALUES("+tagIndex+",'"+tags.get(i)+"');");
			}
			stat.executeBatch();
		}
	}
	
	public static void reset() throws SQLException{
		Statement stat = Connector.getConnector().getStatement();
		
		try {
			makeBatch(stat,"src/DatabaseSchema");
			stat.executeBatch();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String makeBatch(Statement stat, String fileName) throws IOException, SQLException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            stat.addBatch(line);
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
}
