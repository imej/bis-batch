package abmi.bis.batch;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import abmi.bis.batch.controller.AppController;
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
		AppController appCon = ctx.getBean(AppController.class);
		CustomLogger customLogger = ctx.getBean(CustomLogger.class);
		
		Logger logger = customLogger.getLogger();
		logger.info("Batch started.");
		
		if (args.length == 0) {
			logger.severe("CSV file is not provided.");
			return;
		}
		
		File file = new File(args[0]);
		if ( !file.exists() ) {
			logger.severe("'" + args[0] + "' does not exist.");
			return;
		}
		
		if (!appCon.isReady(args[0])) {
			logger.severe("Cannot continue.");
			return;
		}
		
//		CSVService cs = new CSVServiceImpl();
//		List<CSVRow> list = cs.parse(args[0]);
//		
//		for(CSVRow row : list) {
//			System.out.println(row);
//		}
		
		logger.info("All done.");
	}
}
