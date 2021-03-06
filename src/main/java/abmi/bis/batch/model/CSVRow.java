package abmi.bis.batch.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Represents a row of the CSV file.
 * 
 * TODO discuss with Hedwig that column FileType and FileSize are not needed,
 *       Replicate Number, year and round are needed.
 *       
 * @author Richard Cao
 *
 */
public class CSVRow {

	// values from the CSV file
	private String folderPath;
	private String fileName;
	private long lANumber;
	private long method;
	private int observer;
	private int replicateNumber;
	private int year;
	private int round;
	private long recordId;
	private long replicateId;
	
	// values calculated based on the information
	// of the CSV file
	private long id;            // row# starts from 1 (it is probably the header row)
	
	private String project;     // -------------------- 
	private String siteGrp;        // These fields are from fileName.
	private String siteName;
	private String station;     // The format of the fileName is project-site-station_date_time.
	private Date created;       //     site can include '-' and "_0+1_" can appear between station and date
	                            // ----------------------
	
	private String fileType;    // WAC, WAV or UNKNOWN
	
	private String wacPath;     // absolute path to the WAC file 
	private String wavPath;     // absolute path to the WAV file
	private List<Spectrogram> spectrograms;
	private Double recordingLength;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
		setFileType();
		parseFileName();
	}
	public long getLANumber() {
		return lANumber;
	}
	public void setLANumber(long lANumber) {
		this.lANumber = lANumber;
	}
	public long getMethod() {
		return method;
	}
	public void setMethod(long method) {
		this.method = method;
	}
	public int getObserver() {
		return observer;
	}
	public void setObserver(int observer) {
		this.observer = observer;
	}
	public int getReplicateNumber() {
		return replicateNumber;
	}
	public void setReplicateNumber(int replicateNumber) {
		this.replicateNumber = replicateNumber;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public long getRecordId() {
		return recordId;
	}
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	public long getReplicateId() {
		return replicateId;
	}
	public void setReplicateId(long replicateId) {
		this.replicateId = replicateId;
	}
	public String getProject() {
		return project;
	}
	public String getSite() {
		String rv = "";
		if (siteGrp != null && !siteGrp.equals("")) {
			rv += siteGrp + "-";
		}
		return rv + siteName;
	}
	public String getSiteGrp() {
		return siteGrp;
	}
	public String getSiteName() {
		return siteName;
	}
	public String getStation() {
		return station;
	}
	public Date getCreated() {
		return created;
	}
	public String getFileType() {
		return fileType;
	}
	public List<Spectrogram> getSpectrograms() {
		return spectrograms;
	}
	public void setSpectrograms(List<Spectrogram> spectrograms) {
		this.spectrograms = spectrograms;
	}
	public Double getRecordingLength() {
		return recordingLength;
	}
	public void setRecordingLength(Double recordingLength) {
		this.recordingLength = recordingLength;
	}
	public String getWacPath() {
		return wacPath;
	}
	public void setWacPath(String wacPath) {
		this.wacPath = wacPath;
	}
	public String getWavPath() {
		return wavPath;
	}
	public void setWavPath(String wavPath) {
		this.wavPath = wavPath;
	}
	private void setFileType() {
		if (fileName.endsWith(".wac")) {
			fileType = "WAC";
		} else if (fileName.endsWith(".wav")) {
			fileType = "WAV";
		} else {
		    fileType = "UNKNOWN";
		}
	}
	
	private void parseFileName() {
		if (fileName.contains("_")) {
			String[] parts = fileName.split("_");
			if (parts.length == 4) {
				// format: project-site-station_0+1_date_time.wa*
				created = getDate(parts[2], parts[3].substring(0, 6));
			} else if (parts.length == 3) {
				// format: project-site-station_date_time.wa*
				created = getDate(parts[1], parts[2].substring(0, 6));
			}
			
			if (parts[0].contains("-")) {
				String[] parts2 = parts[0].split("-");
				if (parts2.length == 3) {
					// site does not include '-'
					project = parts2[0];
					siteGrp = "";
					siteName = parts2[1];
					station = parts2[2];
				} else if (parts2.length == 4) {
					// site includes '-'
					project = parts2[0];
					siteGrp = parts2[1];
					siteName = parts2[2];
					station = parts2[3];
				}
			}
		}
	}
	
	/**
	 * Return java.util.Date from a date string (yyyyMMdd) and a time string (HHmmss)
	 * @param dateStr
	 * @param timeStr
	 * @return
	 */
	private Date getDate(String dateStr, String timeStr) {
		if (dateStr == null || timeStr == null || dateStr.length() != 8 || timeStr.length() != 6) {
			return null;
		}
		
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		
		try {
			date = (Date)format.parse(dateStr + timeStr);
		} catch (Exception e) {
			
		}
		
		return date;
	}
	
	@Override
	public String toString() {
		return "CSVRow [id=" + id + ", folderPath=" + folderPath + ", fileName=" + fileName + ", lANumber=" + lANumber
				+ ", method=" + method + ", observer=" + observer + ", replicateNumber=" + replicateNumber + "]";
	}
	
}
