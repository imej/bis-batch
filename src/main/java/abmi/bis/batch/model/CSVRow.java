package abmi.bis.batch.model;

/**
 * Represents a row of the CSV file.
 * 
 * TODO discuss with Hedwig that column FileType and FileSize are not needed,
 *       Replicate Number is needed.
 *       
 * @author Richard Cao
 *
 */
public class CSVRow {

	private long id;
	private String folderPath;
	private String fileName;
	private long lANumber;
	private long method;
	private int observer;
	private int replicateNumber;
	
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
	
	@Override
	public String toString() {
		return "CSVRow [id=" + id + ", folderPath=" + folderPath + ", fileName=" + fileName + ", lANumber=" + lANumber
				+ ", method=" + method + ", observer=" + observer + ", replicateNumber=" + replicateNumber + "]";
	}
	
}
