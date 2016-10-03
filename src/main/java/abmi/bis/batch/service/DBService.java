package abmi.bis.batch.service;

import abmi.bis.batch.model.CSVRow;

public interface DBService {

	/**
	 * Test if we can connect to the database
	 * @return
	 */
	Boolean isConnected();
	
	/**
	 * Update database tables for a row of the CSV file
	 * @param row
	 * @return
	 */
	Boolean updateDB(CSVRow row);
	
}
