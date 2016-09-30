package abmi.bis.batch;

import java.io.File;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import abmi.bis.batch.controller.AppController;

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
		
        appCon.processCSV(args[0]);
		
		logger.info("All done.");
	}
}
