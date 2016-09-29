package abmi.bis.batch.dao;

public class DBDaoImpl implements DBDao {

	@Override
	public Boolean isMetaDataReaday(String project, String site, String station, int year, int round) {
		Boolean rv = false;
		String qStr = "SELECT COUNT(*) FROM projects " +
                      "    INNER JOIN sites ON projects.proj_id = sites.proj_id " +
                      "    INNER JOIN stations ON sites.site_id = stations.site_id " +
                      "    INNER JOIN field_data ON stations.station_id = field_data.station_id " +
                      "WHERE projects.proj_code = 'CAO' " + 
                      "    AND sites.site_name = 'Exciting Site' " +
                      "    AND stations.station_name = 'S1' " +
                      "    AND field_data.year = 2008 " +
                      "    AND field_data.round = 10";
		
		return rv;
	}

}
