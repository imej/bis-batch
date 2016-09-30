package abmi.bis.batch.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import abmi.bis.batch.service.RecordingService;

@ContextConfiguration(classes = abmi.bis.batch.config.AppConfig.class, loader = AnnotationConfigContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordingServiceTests {

	@Autowired
	private RecordingService rs;
	
	private String fpath1 = "C:\\temp\\bis-batch\\recordings\\open\\ABMI-0272-NE_20160621_053400.wav";

	private String mp3 = "C:\\temp\\bis-batch\\recordings\\temp\\ABMI-0272-NE_20160621_053400.mp3";
	
	@Test
	public void testGetRecordingLength() {
		Double dd = rs.getRecordingLength(fpath1);
		
		assertNotNull("The length of the recording should NOT be null", dd);
		assertEquals("The length should be 599.997823", (Double)599.997823, dd);
	}
	
	@Test
	public void testConvertToMPEG3() {
		boolean flag = rs.convertToMPEG3(fpath1);
		
		assertTrue("Converting failed", flag);
		
		File file = new File(mp3);
		
		assertTrue("mp3 was not generated", file.exists());
		
	}
}
