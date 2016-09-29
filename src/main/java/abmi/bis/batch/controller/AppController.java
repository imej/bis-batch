package abmi.bis.batch.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import abmi.bis.batch.CustomLogger;
import abmi.bis.batch.model.CSVRow;
import abmi.bis.batch.model.Settings;
import abmi.bis.batch.service.CSVService;
import abmi.bis.batch.service.DBService;

@Controller
public class AppController {

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private CustomLogger customLogger;
	
	@Autowired
	private DBService dBService;
	
	@Autowired
	private CSVService cSVService;
	
	@Autowired
	private Settings settings;
	
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
        String tempDir = settings.getTempDir();
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
        String wac2wavExe = settings.getWac2wavExe();
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
        String soxFolder = settings.getSoxDir();
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
			
			List<String> lst = new ArrayList<String>();
			lst.add(settings.getWac2wavExe());
			lst.add(row.getFolderPath() + File.separator + row.getFileName());
			lst.add(settings.getTempDir() + File.separator + row.getFileName());
			
			ProcessBuilder pb = new ProcessBuilder(lst);
			
			Process process;
			try {
				process = pb.start();
				int errCode = process.waitFor();
				if (errCode == 0) {
					System.out.println("success!");
				} else {
					System.out.println("error!");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
			
		}
	}
	
}
