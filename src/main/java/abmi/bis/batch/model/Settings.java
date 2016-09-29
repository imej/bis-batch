package abmi.bis.batch.model;

public class Settings {

	private String tempDir;
	private String urlPrefix;
	private String wac2wavExe;
	private String soxDir;

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

}
