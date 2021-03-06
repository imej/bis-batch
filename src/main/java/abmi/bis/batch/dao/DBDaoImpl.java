package abmi.bis.batch.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import abmi.bis.batch.model.CSVRow;
//import abmi.bis.batch.model.Settings;
import abmi.bis.batch.model.Spectrogram;

@Repository("dBDao")
public class DBDaoImpl implements DBDao {
	
	@Autowired
	private JdbcOperations jdbcOper;
	
	private static final String QUERY_FIELD_DATA_ID = 
			"SELECT field_data.fd_id FROM projects " +
            "    INNER JOIN sites ON projects.proj_id = sites.proj_id " +
            "    INNER JOIN stations ON sites.site_id = stations.site_id " +
            "    INNER JOIN field_data ON stations.station_id = field_data.station_id " +
            "WHERE projects.proj_code = ? " + 
            "    AND IFNULL(sites.site_group1, '') = ? " +
            "    AND sites.site_name = ? " +
            "    AND stations.station_name = ? " +
            "    AND field_data.year = ? " +
            "    AND field_data.round = ?";

	private static final String QUERY_RECORDING_ID_BY_FILE_NAME = 
			"SELECT record_id FROM recordings " +
            "WHERE file_name = ?";
	
	private static final String COUNT_REPLICATE_BY_FILE_NAME_AND_NUM = 
	        "SELECT COUNT(rep_id) FROM replicates rep " +
            "    INNER JOIN recordings rec on rep.record_id = rec.record_id " +
            "WHERE rec.file_name = ? AND rep.rep_num = ?";
    
	private static final String INSERT_RECORDING = 
	        "INSERT INTO recordings " + 
            "    (fd_id, record_date, record_time, file_name, file_type, file_length) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_REPLICATE = 
	        "INSERT INTO replicates " +
            "    (record_id, rep_num, observer, method, status, assign_num) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_SPECTROGRAM = 
	        "INSERT INTO spectrograms " +
            "    (record_id, file_name, width, height) " +
            "VALUES (?, ?, ?, ?)";
	
	@Override
	public boolean isMetaDataReady(String project, String siteGrp, String siteName, String station, int year, int round) {
		boolean rv = false;

		Long fdId = getFieldDataId(project, siteGrp, siteName, station, year, round);
		
		if (fdId != null && fdId > 0) {
			rv = true;
		}
		
		return rv;
	}

	@Override
	public boolean recordingExists(CSVRow row) {	
		return findRecordingId(row) > 0;
	}

	@Override
	public boolean replicateExists(CSVRow row) {
		Integer i = (Integer)jdbcOper.queryForObject(COUNT_REPLICATE_BY_FILE_NAME_AND_NUM, 
				new Object[] {row.getFileName().substring(0, row.getFileName().length()-4), row.getReplicateNumber()}, Integer.class);
	
	    return i > 0;
	}

	@Override
	public long addRecording(CSVRow row) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOper.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(INSERT_RECORDING, new String[]{"record_id"});
						
						int index = 1;
						ps.setLong(index++, getFieldDataId(row.getProject(), row.getSiteGrp(), row.getSiteName(), row.getStation(), row.getYear(), row.getRound()));
						ps.setDate(index++, new java.sql.Date(row.getCreated().getTime()));
						ps.setTime(index++, new java.sql.Time(row.getCreated().getTime()));
						ps.setString(index++, row.getFileName().substring(0, row.getFileName().length()-4));
						ps.setString(index++, "mp3");
						ps.setDouble(index++, row.getRecordingLength());
						return ps;
					}
				}, 
				keyHolder);
		
		row.setRecordId((Long)keyHolder.getKey());
		return row.getRecordId();
		
	}

	@Override
	public long addReplicate(CSVRow row) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOper.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
						PreparedStatement ps = conn.prepareStatement(INSERT_REPLICATE, new String[]{"rep_id"});
						
						int index = 1;
						ps.setLong(index++, row.getRecordId());
						ps.setInt(index++, row.getReplicateNumber());
						ps.setInt(index++, row.getObserver());
						ps.setLong(index++, row.getMethod());
						ps.setLong(index++, 1L);
						ps.setLong(index++, row.getLANumber());
						
                        return ps;
					}
				}, 
				keyHolder);
		
		row.setReplicateId((Long)keyHolder.getKey());
		return row.getReplicateId();
	}

	@Override
	public boolean addSpectrograms(CSVRow row) {
		
		jdbcOper.batchUpdate(INSERT_SPECTROGRAM, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Spectrogram sp = row.getSpectrograms().get(i);
				int index = 1;
				ps.setLong(index++, row.getRecordId());
				ps.setString(index++, sp.getFileName());
				ps.setInt(index++, sp.getWidth());
				ps.setLong(index++, sp.getHeight());
			}
			
			@Override
			public int getBatchSize() {
				return row.getSpectrograms().size();
			}
		});
		
		return true;
	}

	private Long getFieldDataId(String project, String siteGrp, String siteName, String station, int year, int round) {
		Long l;
		
		try {
			l = (Long)jdbcOper.queryForObject(QUERY_FIELD_DATA_ID, 
					new Object[] {project, siteGrp, siteName, station, year, round}, Long.class);
		} catch (EmptyResultDataAccessException e) {
			l = -1L;
		}
		return l;
	}
		
	@Override
	public long findRecordingId(CSVRow row) {
		Long l;
		
		try {
			l = (Long)jdbcOper.queryForObject(QUERY_RECORDING_ID_BY_FILE_NAME, 
				new Object[] {row.getFileName().substring(0, row.getFileName().length()-4)}, Long.class);
			return l;
		} catch (EmptyResultDataAccessException e) {
			return -1;
		}
	}
}
