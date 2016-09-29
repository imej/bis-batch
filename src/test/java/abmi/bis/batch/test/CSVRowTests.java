package abmi.bis.batch.test;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import abmi.bis.batch.model.CSVRow;

@ContextConfiguration(classes = abmi.bis.batch.config.AppConfig.class, loader = AnnotationConfigContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CSVRowTests {

	private CSVRow row;
	
	@Test
	public void testSetFileName() {
		row = new CSVRow();
		
		row.setFileName("ABMI-0272-NE_20160621_053400.wav");
		assertTrue("FileType is WAV", row.getFileType().equals("WAV"));
		assertTrue("Project is ABMI", row.getProject().equals("ABMI"));
		assertTrue("Site is 0272", row.getSite().equals("0272"));
		assertTrue("Station is NE", row.getStation().equals("NE"));
		
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		assertTrue("datetime is wrong", df.format(row.getCreated()).equals("20160621053400"));
		
		row.setFileName("OW-12-37-CT_20140406_000000.wac");
		assertTrue("OW-12-37-CT_20140406_000000.wac: FileType is WAC", row.getFileType().equals("WAC"));
		assertTrue("OW-12-37-CT_20140406_000000.wac: Project is OW", row.getProject().equals("OW"));
		assertTrue("OW-12-37-CT_20140406_000000.wac: Site is 12-37", row.getSite().equals("12-37"));
		assertTrue("OW-12-37-CT_20140406_000000.wac: Station is CT", row.getStation().equals("CT"));
		
		assertTrue("OW-12-37-CT_20140406_000000.wac: datetime is wrong", df.format(row.getCreated()).equals("20140406000000"));
		
		row.setFileName("ABMI-0001-NE_0+1_20150531_072300.wac");
		assertTrue("ABMI-0001-NE_0+1_20150531_072300.wac: FileType is WAC", row.getFileType().equals("WAC"));
		assertTrue("ABMI-0001-NE_0+1_20150531_072300.wac: Project is ABMI", row.getProject().equals("ABMI"));
		assertTrue("ABMI-0001-NE_0+1_20150531_072300.wac: Site is 0001", row.getSite().equals("0001"));
		assertTrue("ABMI-0001-NE_0+1_20150531_072300.wac: Station is NE", row.getStation().equals("NE"));
		
		assertTrue("ABMI-0001-NE_0+1_20150531_072300.wac: datetime is wrong", df.format(row.getCreated()).equals("20150531072300"));
		
	}
}
