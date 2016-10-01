package abmi.bis.batch.service;

import abmi.bis.batch.model.CSVRow;

public interface RecordingService {

	Double getRecordingLength(String fpath);
	
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
