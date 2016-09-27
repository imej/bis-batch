package abmi.bis.batch.controller;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import abmi.bis.batch.CustomLogger;
import abmi.bis.batch.model.CSVRow;
import abmi.bis.batch.service.CSVService;
import abmi.bis.batch.service.DBService;

@Controller
@PropertySource(value = { "classpath:application.properties" })
public class AppController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private CustomLogger customLogger;
	
	@Autowired
	private DBService dBService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private CSVService cSVService;
	
	/**
	 * Make sure we can run:
	 * - CSV file exists
	 * - Temporary folder exists
	 * - WAC2WAV is installed - wac2wav.exe exists in the same folder
	 * - SOX is installed
	 * - Network folder is reachable
	 * - Database is accessible
	 *
	 * @return
	 */
	public Boolean isReady(String csv) {
		
        Logger logger = customLogger.getLogger();
		
        // check csv
		File file = new File(csv);
		if ( !file.exists() ) {
			logger.severe(messageSource.getMessage("csv.not.exist", new String[] {csv}, Locale.getDefault()));
			return false;
		}
		
		// check temporary folder
        String tempDir = env.getRequiredProperty("temp.folder");
        if (tempDir == null) {
        	logger.severe(messageSource.getMessage("tempdir.not.define", null, Locale.getDefault()));
			return false;
        }
        
        file = new File(tempDir);
        if ( !file.exists() ) {
        	logger.severe(messageSource.getMessage("tempdir.not.exist", new String[] {csv}, Locale.getDefault()));
			return false;
        }
        
        // check WAC2WAV - does the executable exist?
        String wac2wavExe = env.getRequiredProperty("wac2wav.exe");
        if (wac2wavExe == null) {
        	logger.severe(messageSource.getMessage("wac2wav.not.define", null, Locale.getDefault()));
			return false;
        }
        
        file = new File(wac2wavExe);
        if ( !file.exists() ) {
        	logger.severe(messageSource.getMessage("wac2wav.not.exist", new String[] {wac2wavExe}, Locale.getDefault()));
			return false;
        }
        
        // check SOX - does the installation folder exist?
        String soxFolder = env.getRequiredProperty("sox.path");
        if (soxFolder == null) {
        	logger.severe(messageSource.getMessage("sox.not.define", null, Locale.getDefault()));
			return false;
        }
        
        file = new File(soxFolder);
        if ( !file.exists() ) {
        	logger.severe(messageSource.getMessage("sox.not.exist", new String[] {soxFolder}, Locale.getDefault()));
			return false;
        }
        
        // TODO test network drive
        
        
        
        // check db connection
		if ( !dBService.isConnected() ) {
			logger.severe(messageSource.getMessage("db.not.connect", null, Locale.getDefault()));
			return false;
		}
		
		return true;
	}
	
	public void processCSV(String csv) {
		List<CSVRow> list = cSVService.parse(csv);
		
		for(CSVRow row : list) {
			System.out.println(row);
		}
	}
	
}
