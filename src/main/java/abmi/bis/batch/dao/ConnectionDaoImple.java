package abmi.bis.batch.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("ConnectionDao")
public class ConnectionDaoImple implements ConnectionDao {

	@Autowired
	private DataSource dataSource;
	
	@Override
	public Boolean isConnected() {
		Boolean rv = false;
		
		try {	
			Connection con = dataSource.getConnection();
			if (con != null) {
			    rv = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rv;
	}

}
