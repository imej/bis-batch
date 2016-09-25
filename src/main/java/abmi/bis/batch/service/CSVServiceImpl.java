package abmi.bis.batch.service;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.opencsv.CSVReader;

import abmi.bis.batch.model.CSVRow;

public class CSVServiceImpl implements CSVService {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public List<CSVRow> parse(String filePath) {
		List<CSVRow> list = null;
		
		LOGGER.info("Start parsing file: " + filePath);
		
		File file = new File(filePath);
		if ( !file.exists() ) {
			LOGGER.severe("'" + filePath + "' does not exist.");
			return list;
		}
		
		CSVReader reader = null;
		int i = 0;
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = getClass().getClassLoader().getResourceAsStream("application.properties");
			prop.load(input);
			Boolean hasHeader = prop.getProperty("csv.hasHeader").equals("true");
			if ( hasHeader ) {
				LOGGER.info("First row includes header columns.");
			} else {
				LOGGER.info("First row does NOT include header columns.");
			}
			
			reader = new CSVReader(new FileReader(filePath));
			String[] line;
			list = new ArrayList<CSVRow>();
			
			while ((line = reader.readNext()) != null) {
				if (i == 0 && hasHeader) {
					i++;
					continue;
				}
				
				/*
				 * Columns:
				 * 0 - folderPath
				 * 1 - fileName
				 * 2 - replicateNumber
				 * 3 - lANumber
				 * 4 - method
				 * 5 - observer
				 */
				CSVRow row = new CSVRow();
				row.setId(i);
				row.setFolderPath(line[0]);
				row.setFileName(line[1]);
				row.setReplicateNumber(Integer.parseInt(line[2]));
		        row.setLANumber(Long.parseLong(line[3]));
		        row.setMethod(Long.parseLong(line[4]));
		        row.setObserver(Integer.parseInt(line[5]));
				
		        list.add(row);
		        
				i++;
				
			}
			
			LOGGER.info("Complete parsing file: " + filePath + ". " + list.size() + " rows are received.");
			
		} catch (Exception e) {
			LOGGER.severe("ERROR: " + e.getMessage());
			e.printStackTrace();
		}
				
		return list;
	}

}
