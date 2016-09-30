package abmi.bis.batch.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

import abmi.bis.batch.model.CSVRow;

public class DBDaoImpl implements DBDao {
	
	@Autowired
	private JdbcOperations jdbcOper;

	private static final String QUERY_FIELD_DATA_ID = 
			"SELECT field_data.fd_id FROM projects " +
            "    INNER JOIN sites ON projects.proj_id = sites.proj_id " +
            "    INNER JOIN stations ON sites.site_id = stations.site_id " +
            "    INNER JOIN field_data ON stations.station_id = field_data.station_id " +
            "WHERE projects.proj_code = ? " + 
            "    AND sites.site_name = ? " +
            "    AND stations.station_name = ? " +
            "    AND field_data.year = ? " +
            "    AND field_data.round = ?";

	private static final String QUERY_RECORDING_BY_FILE_NAME = 
			"SELECT record_id FROM recordings " +
            "WHERE file_name = ?";
	
	
	@Override
	public boolean isMetaDataReaday(String project, String site, String station, int year, int round) {
		boolean rv = false;

		Long fdId = getFieldDataId(project, site, station, year, round);
		
		if (fdId != null && fdId > 0) {
			rv = true;
		}
		
		return rv;
	}

	@Override
	public boolean recordingExists(String fileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean replicateExists(String fileName, int repNum) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long addRecording(CSVRow row) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long addReplicate(CSVRow row) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean addSpectrograms(CSVRow row) {
		// TODO Auto-generated method stub
		return false;
	}

	private Long getFieldDataId(String project, String site, String station, int year, int round) {
		return (Long)jdbcOper.queryForObject(QUERY_FIELD_DATA_ID, 
				new Object[] {project, site, station, year, round}, Long.class);
	}
}
