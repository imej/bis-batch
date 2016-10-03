package abmi.bis.batch;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Customize the output of Logger.
 *  
 * @author Richard Cao
 *
 */
@Component("customLogger")
public class CustomLogger {
    
	private Logger logger;
	
	@Autowired
	public CustomLogger(FileHandler logFile) {
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.INFO);
		logger.addHandler(logFile);
	}
	
	public Logger getLogger() {
		return this.logger;
	}
	
	public void log(String msg, Level level) {
		if (msg != null) {
			System.out.println(msg);

			this.logger.log(level, msg);
		}
		
	}
		
}
