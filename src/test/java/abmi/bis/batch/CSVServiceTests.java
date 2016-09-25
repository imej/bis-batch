package abmi.bis.batch;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import abmi.bis.batch.model.CSVRow;
import abmi.bis.batch.service.CSVService;
import abmi.bis.batch.service.CSVServiceImpl;

public class CSVServiceTests {

	CSVService cs = new CSVServiceImpl();
	
	@Test
	public void testParse() {
		String userDir = System.getProperty("user.dir");
		
		userDir += File.separator + "target" + File.separator + "test-classes" 
		        + File.separator + "sample.csv";
		
		System.out.println(userDir);
		
		List<CSVRow> list = cs.parse(userDir);
		
		assertNotNull("There should be entries in sample.csv", list);
		
	}
	
}
