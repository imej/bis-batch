package abmi.bis.batch.service;

import abmi.bis.batch.model.CSVRow;

public interface RecordingService {

	Double getRecordingLength(String fpath);
	
	/**
	 * Convert MAV to MP3. The mp3 file has the same name as the mav file,
	 * and is saved in the temporary folder indicated in the properties file.
	 *  
	 * @param fpath
	 * @return
	 */
	boolean convertToMPEG3(String fpath);
	
	/**
	 * 1. Create spectrograms for a recording to the temporary folder.
	 *    A sub directory is created in the temporary folder.
	 *    The name of the sub directory is the name of the recording file.
	 * 2. The list of the spectrograms is added to Object CSVRow 
	 * @param row
	 * @return
	 */
	boolean createSpectrograms(CSVRow row);
	
}
