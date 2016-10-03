package abmi.bis.batch;

import java.io.File;
import java.util.logging.Level;

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
		
		customLogger.log("Batch started.", Level.INFO);
		
		if (args.length == 0) {
			customLogger.log("CSV file is not provided.", Level.SEVERE);
			return;
		}
		
		File file = new File(args[0]);
		if ( !file.exists() ) {
			customLogger.log("'" + args[0] + "' does not exist.", Level.SEVERE);
			return;
		}
		
		if (!appCon.isReady(args[0])) {
			customLogger.log("Cannot continue.", Level.SEVERE);
			return;
		}
		
        appCon.processCSV(args[0]);
		
		customLogger.log("All done.", Level.INFO);
		
	}
}
