package abmi.bis.batch.dao;

import abmi.bis.batch.model.CSVRow;

public interface DBDao {

	/**
	 * Find out if the meta data has been entered into the database.
	 * 
	 * @param project
	 * @param site
	 * @param station
	 * @param year
	 * @param round
	 * @return
	 */
	boolean isMetaDataReaday(String project, String site, String station, int year, int round);
	
	/**
	 * Check if the recording record exists in database.
	 * 
	 * @param fileName
	 * @return
	 */
	boolean recordingExists(CSVRow row);
	
	/**
	 * Check if the replicate record exists
	 * @param fileName
	 * @param repNum
	 * @return
	 */
	boolean replicateExists(CSVRow row);
	
	/**
	 * Insert a record into the recordings table.
	 * 
	 * @param row
	 * @return long - > 0 - the id of the newly inserted record
	 *                < 0 - insertion failed
	 */ 
	long addRecording(CSVRow row);
	
	/**
	 * Insert a record into the replicates table.
	 * @param row
	 * @return long - > 0 - the id of the newly inserted record
	 *                < 0 - insertion failed
	 */
	long addReplicate(CSVRow row);
	
	/**
	 * Insert records into the spectrograms table.
	 * 
	 * @param row
	 * @return boolean - true - succeeded
	 *                   false - failed
	 */
	boolean addSpectrograms(CSVRow row);
	
}
