package abmi.bis.batch.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import abmi.bis.batch.model.CSVRow;
import abmi.bis.batch.service.CSVService;

@ContextConfiguration(classes = abmi.bis.batch.config.AppConfig.class, loader = AnnotationConfigContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CSVServiceTests {

	@Autowired
	private CSVService cSVService;
	
	@Test
	public void testParse() {
		String userDir = System.getProperty("user.dir");
		
		userDir += File.separator + "target" + File.separator + "test-classes" 
		        + File.separator + "sample.csv";
		
		System.out.println(userDir);
		
		List<CSVRow> list = cSVService.parse(userDir);
		
		assertNotNull("There should be entries in sample.csv", list);
		
		assertTrue("File path of first row", 
				list.get(0).getFolderPath().equals("C:\\temp\\bis-batch\\csv\\recordings\\open"));;
		
	}
	
}
