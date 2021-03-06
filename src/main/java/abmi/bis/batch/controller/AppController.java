package abmi.bis.batch.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import abmi.bis.batch.CustomLogger;
import abmi.bis.batch.model.CSVRow;
import abmi.bis.batch.model.Settings;
import abmi.bis.batch.service.CSVService;
import abmi.bis.batch.service.DBService;
import abmi.bis.batch.service.RecordingService;

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
	
	@Autowired
	private RecordingService recordingService;
	
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
		
        // check csv
		File file = new File(csv);
		if ( !file.exists() ) {
			customLogger.log(messageSource.getMessage("csv.not.exist", new String[] {csv}, Locale.getDefault()), Level.SEVERE);
			return false;
		}
		
		// check temporary folder
        String tempDir = settings.getTempDir();
        if (tempDir == null) {
        	customLogger.log(messageSource.getMessage("tempdir.not.define", null, Locale.getDefault()), Level.SEVERE);
			return false;
        }
        
        file = new File(tempDir);
        if ( !file.exists() ) {
        	customLogger.log(messageSource.getMessage("tempdir.not.exist", new String[] {csv}, Locale.getDefault()), Level.SEVERE);
			return false;
        }
        
        // check WAC2WAV - does the executable exist?
        String wac2wavExe = settings.getWac2wavExe();
        if (wac2wavExe == null) {
        	customLogger.log(messageSource.getMessage("wac2wav.not.define", null, Locale.getDefault()), Level.SEVERE);
			return false;
        }
        
        file = new File(wac2wavExe);
        if ( !file.exists() ) {
        	customLogger.log(messageSource.getMessage("wac2wav.not.exist", new String[] {wac2wavExe}, Locale.getDefault()), Level.SEVERE);
			return false;
        }
        
        // check SOX - does the installation folder exist?
        String soxFolder = settings.getSoxDir();
        if (soxFolder == null) {
        	customLogger.log(messageSource.getMessage("sox.not.define", null, Locale.getDefault()), Level.SEVERE);
			return false;
        }
        
        file = new File(soxFolder);
        if ( !file.exists() ) {
        	customLogger.log(messageSource.getMessage("sox.not.exist", new String[] {soxFolder}, Locale.getDefault()), Level.SEVERE);
			return false;
        }
        
        // check network drive
        String serverFolder = settings.getServerFolder();
        if (serverFolder == null) {
        	customLogger.log(messageSource.getMessage("svrdir.not.define", null, Locale.getDefault()), Level.SEVERE);
        	return false;
        }
        
        file = new File(serverFolder);
        if ( !file.exists() ) {
        	customLogger.log(messageSource.getMessage("svrdir.not.exist", new String[] {serverFolder}, Locale.getDefault()), Level.SEVERE);
			return false;
        }
        
        
        // check db connection
		if ( !dBService.isConnected() ) {
			customLogger.log(messageSource.getMessage("db.not.connect", null, Locale.getDefault()), Level.SEVERE);
			return false;
		}
		
		return true;
	}
	
	public void processCSV(String csv) {
		List<CSVRow> list = cSVService.parse(csv);
		
		String msg;
		
		for(CSVRow row : list) {
			if (settings.isDebug()) {
				System.out.println(row);
			}
				
			// Do nothing if the recording file does not exist.
			File file = null;
			try {
				file = new File(row.getFolderPath() + File.separator + row.getFileName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (file == null || !file.exists()) {
				msg = "Error: File not exist. Skip row #" + row.getId();
				customLogger.log(msg, Level.SEVERE);;
				continue;
			}
			
			// Do nothing if the meta data is not ready.
			if ( !dBService.isMetaDataReady(row) ) {
				msg = "Error: Meta data is not ready. Skip row #" + row.getId();
				customLogger.log(msg, Level.SEVERE);;
				continue;
			}
			
			// Everything looks good, lets go
			/*
			 * STEP 1: convert WAC to WAV when necessary
			 */
			if (row.getFileType().equals("WAC")) {
				row.setWacPath(row.getFolderPath() + File.separator + row.getFileName());
				row.setWavPath(settings.getTempDir() + File.separator + row.getFileName().replace(".wac", ".wav"));
				
				List<String> lst = new ArrayList<String>();
				lst.add(settings.getWac2wavExe());
				lst.add(row.getWacPath());
				lst.add(row.getWavPath());
				
				ProcessBuilder pb = new ProcessBuilder(lst);
				
				if (settings.isDebug()) {
					System.out.println(lst);
				}
								
				Process process;
				try {
					process = pb.start();
					if (process.waitFor() != 0) {
						msg = "Error: failed to convert " + row.getWacPath() + " to " + row.getWavPath() + ". Skip row#" + row.getId();
						customLogger.log(msg, Level.SEVERE);
						continue;
					}	
				} catch (Exception e) {
					e.printStackTrace();
					msg = "Error: failed to convert " + row.getWacPath() + " to " + row.getWavPath() + ". Skip row#" + row.getId();
					customLogger.log(msg, Level.SEVERE);
					continue;
				}
			} else {
				row.setWacPath(row.getFolderPath() + File.separator + row.getFileName());
				row.setWavPath(row.getWacPath());
			}
			
			/*
			 * STEP 2: process WAV at local: 
			 *                     - convert to mp3
			 *                     - create spectrograms 
			 */
			row.setRecordingLength(recordingService.getRecordingLength(row.getWavPath()));
			
			if (row.getRecordingLength() == null || !(row.getRecordingLength() > 0)) {
				msg = "Error: zero length recording. Skip row #" + row.getId();
				customLogger.log(msg, Level.SEVERE);
				continue;
			}
			
			if ( !recordingService.convertToMPEG3(row.getWavPath()) ) {
				msg = "Error: failed to convert to mp3. Skip row #" + row.getId();
				customLogger.log(msg, Level.SEVERE);
				continue;
			}
			
			if ( !recordingService.createSpectrograms(row) ) {
				msg = "Error: failed to create spectrograms. Skip row #" + row.getId();
				customLogger.log(msg, Level.SEVERE);
				continue;
			}
			
			/*
			 * STEP 3: upload mp3 and spectrograms to server
			 *         The structure of the folders is:
			 *         - context/static/recordings/project/site/station/year/round/.mp3
			 *         - context/static/recordings/project/site/station/year/round/mp3_file_name/.png
			 */
			if ( !recordingService.copyFiles(row) ) {
				msg = "Error: failed to copy files to server. Skip row #" + row.getId();
				customLogger.log(msg, Level.SEVERE);
				continue;
			}
			
			/*
			 * STEP 4: update database
			 */
			if ( !dBService.updateDB(row) ) {
				msg = "Error: failed to update database. Skip row #" + row.getId();
				customLogger.log(msg, Level.SEVERE);
				continue;
			}
		}
		
		// Delete temporary contents.
		try {
			FileUtils.cleanDirectory(new File(settings.getTempDir()));
		} catch (IOException e) {
			customLogger.log("Error: failed to clean the contents of folder " + settings.getTempDir(), Level.SEVERE);
			e.printStackTrace();
		}
	}
}