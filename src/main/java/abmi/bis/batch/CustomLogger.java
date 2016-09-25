package abmi.bis.batch;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Customize the output of Logger.
 * - Logs are written to text files named bis_batch#.log.
 * - The log files are saved into a directory named 
 *   bis_batch_logs of the home directory of the current user.
 *  
 * @author Richard Cao
 *
 */
public class CustomLogger {

	private static FileHandler logFile;
	private static Formatter formatter;
	
	public static void setup() throws IOException {
		
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		
		logger.setLevel(Level.INFO);
		
		String homeDir = System.getProperty("user.home");
		
		// create directory if not exist
		File file = new File(homeDir + File.separator + "bis_batch_logs");
		if (!file.exists()) {
			file.mkdir();
		}
		
		logFile = new FileHandler(file.getPath() + "/bis_batch_%u.log");
		formatter = new TxtLogFormatter();
		logFile.setFormatter(formatter);
		logger.addHandler(logFile);
		
	}
	
}
