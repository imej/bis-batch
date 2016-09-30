package abmi.bis.batch.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import abmi.bis.batch.dao.DBDao;

@ContextConfiguration(classes = abmi.bis.batch.config.AppConfig.class, loader = AnnotationConfigContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DBDaoTests {

	@Autowired
	private DBDao dbDao;
	
	@Test
	public void testGetFieldDataId() {
		String project = "";
		String site = "";
		String station = "";
		int year = 2008;
		int round = 10;
		
		assertTrue("Empty project does not exist", !dbDao.isMetaDataReaday(project, site, station, year, round));
		
		project = "CAO";
		site = "Exciting Site";
		station = "S1";
		year = 2008;
		round = 10;
		
		assertTrue("Empty project does not exist", dbDao.isMetaDataReaday(project, site, station, year, round));
		
	}
}
