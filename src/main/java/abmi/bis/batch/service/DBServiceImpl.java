package abmi.bis.batch.service;

import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import abmi.bis.batch.CustomLogger;
import abmi.bis.batch.dao.ConnectionDao;
import abmi.bis.batch.dao.DBDao;
import abmi.bis.batch.model.CSVRow;

@Service("dBService")
public class DBServiceImpl implements DBService {

	@Autowired
	private ConnectionDao conDao;
	
	@Autowired
	private DBDao dBDao;
	
	@Autowired
	private CustomLogger customLogger;
	
	@Override
	public Boolean isConnected() {
		return conDao.isConnected();
	}

	/**
	 * Tables need to be updated:
	 * - recordings
	 * - replicates
	 * - spectrograms
	 * 
	 * We only want to insert table recordings and spectrograms if 
	 * the file name does not exist in the recordings table.
	 * 
	 * We only want to insert table replicates if replicate does not
	 * exist (recording id + replicate number).
	 */
	@Override
	public Boolean updateDB(CSVRow row) {
		if (dBDao.recordingExists(row)) {
			customLogger.log("Existing file, will not update recordings and spectrograms table. Row#" + 
		                      row.getId(), Level.INFO);
			if (dBDao.replicateExists(row)) {
				customLogger.log("Existing replicate, will not update replicate table. Row#" + 
			                      row.getId(), Level.INFO);
				return true;
			} else {
				
				
			}
		} 
		
        return false;
	}

}
