package abmi.bis.batch.service;

public interface RecordingService {

	Double getRecordingLength(String fpath);
	
	boolean convertToMPEG3(String fpath);
	
	boolean createSpectrograms(String fpath);
	
}
