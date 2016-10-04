package abmi.bis.batch.model;

public class Settings {

	private String tempDir;
	private String urlPrefix;
	private String wac2wavExe;
	private String soxDir;
	private String soxCmd;
	private String soxiCmd;
	private int spLength;
	private int spFreq;
	private int spHeight;
	private int spRange;
	private int spRes;
	private boolean hasHeader;
	private boolean debug;
	private String serverFolder;

	public String getSoxCmd() {
		return soxCmd;
	}

	public void setSoxCmd(String soxCmd) {
		this.soxCmd = soxCmd;
	}

	public String getSoxiCmd() {
		return soxiCmd;
	}

	public void setSoxiCmd(String soxiCmd) {
		this.soxiCmd = soxiCmd;
	}

	public int getSpLength() {
		return spLength;
	}

	public void setSpLength(int spLength) {
		this.spLength = spLength;
	}

	public int getSpFreq() {
		return spFreq;
	}

	public void setSpFreq(int spFreq) {
		this.spFreq = spFreq;
	}

	public int getSpHeight() {
		return spHeight;
	}

	public void setSpHeight(int spHeight) {
		this.spHeight = spHeight;
	}

	public int getSpRange() {
		return spRange;
	}

	public void setSpRange(int spRange) {
		this.spRange = spRange;
	}

	public int getSpRes() {
		return spRes;
	}

	public void setSpRes(int spRes) {
		this.spRes = spRes;
	}

	public String getTempDir() {
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public String getWac2wavExe() {
		return wac2wavExe;
	}

	public void setWac2wavExe(String wac2wavExe) {
		this.wac2wavExe = wac2wavExe;
	}

	public String getSoxDir() {
		return soxDir;
	}

	public void setSoxDir(String soxDir) {
		this.soxDir = soxDir;
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getServerFolder() {
		return serverFolder;
	}

	public void setServerFolder(String serverFolder) {
		this.serverFolder = serverFolder;
	}

}
