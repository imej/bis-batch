package abmi.bis.batch.service;

import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import abmi.bis.batch.CustomLogger;
import abmi.bis.batch.model.CSVRow;

@Service("cSVService")
public class CSVServiceImpl implements CSVService {

	@Autowired
	private CustomLogger customLogger;
	
	public List<CSVRow> parse(String filePath) {
		List<CSVRow> list = null;
		
		customLogger.log("Start parsing file: " + filePath, Level.INFO);
		
		CSVReader reader = null;
		int i = 1;
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = getClass().getClassLoader().getResourceAsStream("application.properties");
			prop.load(input);
			Boolean hasHeader = prop.getProperty("csv.hasHeader").equals("true");
			if ( hasHeader ) {
				customLogger.log("First row includes header columns.", Level.INFO);
			} else {
				customLogger.log("First row does NOT include header columns.", Level.INFO);
			}
			
			CSVParser parser = new CSVParser(CSVParser.DEFAULT_SEPARATOR, 
					                         CSVParser.DEFAULT_QUOTE_CHARACTER, 
					                         '\0', 
					                         CSVParser.DEFAULT_STRICT_QUOTES);
			reader = new CSVReader(new FileReader(filePath), 0, parser);
			String[] line;
			list = new ArrayList<CSVRow>();
			
			while ((line = reader.readNext()) != null) {
				if (i == 1 && hasHeader) {
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
				 * 6 - year
				 * 7 - round
				 */
				CSVRow row = new CSVRow();
				row.setId(i);
				row.setFolderPath(line[0]);
				row.setFileName(line[1]);
				row.setReplicateNumber(Integer.parseInt(line[2]));
		        row.setLANumber(Long.parseLong(line[3]));
		        row.setMethod(Long.parseLong(line[4]));
		        row.setObserver(Integer.parseInt(line[5]));
				row.setYear(Integer.parseInt(line[6]));
				row.setRound(Integer.parseInt(line[7]));
				
		        list.add(row);
		        
				i++;
				
			}
			
			customLogger.log("Complete parsing file: " + filePath + ". " + list.size() + " rows are received.", Level.INFO);
			
		} catch (Exception e) {
			customLogger.log("ERROR: " + e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
				
		return list;
	}

}
