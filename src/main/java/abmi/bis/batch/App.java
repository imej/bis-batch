package abmi.bis.batch;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import abmi.bis.batch.model.CSVRow;
import abmi.bis.batch.service.CSVService;
import abmi.bis.batch.service.CSVServiceImpl;


/**
 * main
 *
 */
public class App {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) {
		
		try {
			CustomLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LOGGER.info("Batch started.");
		
		String csvFile = "/Users/richard/Downloads/csv-sample.csv";
		
		CSVService cs = new CSVServiceImpl();
		List<CSVRow> list = cs.parse(csvFile);
		
		for(CSVRow row : list) {
			System.out.println(row);
		}
		
		LOGGER.info("All done.");
	}
}
