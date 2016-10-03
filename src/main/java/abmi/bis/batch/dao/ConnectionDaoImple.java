package abmi.bis.batch.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import abmi.bis.batch.CustomLogger;

@Repository("ConnectionDao")
public class ConnectionDaoImple implements ConnectionDao {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private CustomLogger customLogger;
	
	@Override
	public Boolean isConnected() {
		Boolean rv = false;
		
		try {	
			Connection con = dataSource.getConnection();
			if (con != null) {
			    rv = true;
			}
			
		} catch (SQLException e) {
			customLogger.log("ERROR: Connecting to database - " + e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
		
		return rv;
	}

}
