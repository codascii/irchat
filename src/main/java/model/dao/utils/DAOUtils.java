package model.dao.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DAOUtils {
	private static final Logger LOG = Logger.getLogger(DAOUtils.class.getName()); 
	
	private DAOUtils() {
		// Useless
	}
	
	public static void close(Connection c){
		try {
			if (c != null){
				c.close();
			}
		} catch (SQLException e) {
			LOG.info("Error during closing mysql connection stream.", e);
		}
	}
	
	public static void close(Statement st){
		try {
			if (st != null){
				st.close();
			}
		} catch (SQLException e) {
			LOG.info("Error during closing mysql connection stream.", e);
		}
	}
	
	public static void close(ResultSet r){
		try {
			if (r != null){
				r.close();
			}
		} catch (SQLException e) {
			LOG.info("Error during closing mysql connection stream.", e);
		}
	}

}
