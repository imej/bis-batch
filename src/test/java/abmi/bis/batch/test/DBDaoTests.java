package abmi.bis.batch.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import abmi.bis.batch.dao.DBDao;
import abmi.bis.batch.model.CSVRow;

@ContextConfiguration(classes = abmi.bis.batch.config.AppConfig.class, loader = AnnotationConfigContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DBDaoTests {

	@Autowired
	private DBDao dbDao;
	
	@Test
	public void testGetFieldDataId() {
		String project = "";
		String siteGrp = "";
		String siteName = "";
		String station = "";
		int year = 2008;
		int round = 10;
		
		assertTrue("Empty project does not exist", !dbDao.isMetaDataReaday(project, siteGrp, siteName, station, year, round));
		
		project = "ABMI";
		siteGrp = "";
		siteName = "0272";
		station = "NE";
		year = 2016;
		round = 1;
		
		assertTrue("Empty project does not exist", dbDao.isMetaDataReaday(project, siteGrp, siteName, station, year, round));
		
	}
	
	@Test
	public void testRecordingExists() {
		String fileName1 = "not_exist_file";
		
		//assertTrue(fileName1 + " exists", !dbDao.recordingExists(fileName1));
		
		String fileName2 = "FMX-01-01-E_20140603_050000";
		
		//assertTrue(fileName2 + " not exists", dbDao.recordingExists(fileName2));
		
	}
	
	@Test
	public void testReplicateExists() {
		String fileName1 = "FMX-01-01-E_20140603_050000";
		int repNum1 = 5;
		
		//assertTrue(fileName1 + " - " + repNum1 + " exists", !dbDao.replicateExists(fileName1, repNum1));
		
		int repNum2 = 1;
		
		//assertTrue(fileName1 + " - " + repNum2 + " not exists", dbDao.replicateExists(fileName1, repNum2));
		
	}
	
	@Test
	public void testAddRecording() {
		CSVRow row = new CSVRow();
		
		row.setId(1);
		row.setFolderPath("C:\\temp\\bis-batch\\recordings\\open");
		row.setFileName("ABMI-0272-NE_20160621_053400.wav");
		row.setReplicateNumber(1);
        row.setLANumber(100);
        row.setMethod(11);
        row.setObserver(5);
		row.setYear(2016);
		row.setRound(1);
		
		long l = dbDao.addRecording(row);
		
		assertTrue("New recording ID must > 0", l > 0);
	}
	
}
