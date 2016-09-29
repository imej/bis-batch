package abmi.bis.batch.dao;

public interface DBDao {

	Boolean isMetaDataReaday(String project, String site, String station, int year, int round);
	
}
