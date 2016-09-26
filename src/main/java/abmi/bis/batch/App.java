package abmi.bis.batch;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import abmi.bis.batch.model.CSVRow;
import abmi.bis.batch.service.CSVService;
import abmi.bis.batch.service.CSVServiceImpl;


/**
 * main
 *
 */
public class App {

	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("abmi.bis.batch");
		CustomLogger customLogger = ctx.getBean(CustomLogger.class);
		Logger logger = customLogger.getLogger();
		
		
		logger.info("Batch started.");
		
		/*
		 * Make sure we can run:
		 * - CSV file exists
		 * - Temporary folder exists
		 * - WAC2WAV is installed - wac2wav.exe exists in the same folder
		 * - SOX is installed
		 * - Network folder is reachable
		 * - Database is accessible
		 */
		if (args.length == 0) {
			logger.severe("CSV file is not provided.");
			return;
		}
		
		File file = new File(args[0]);
		if ( !file.exists() ) {
			logger.severe("'" + args[0] + "' does not exist.");
			return;
		}
		
		
		
		
		CSVService cs = new CSVServiceImpl();
		List<CSVRow> list = cs.parse(args[0]);
		
		for(CSVRow row : list) {
			System.out.println(row);
		}
		
		logger.info("All done.");
	}
}
