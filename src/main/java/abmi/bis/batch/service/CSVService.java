package abmi.bis.batch.service;

import java.util.List;

import abmi.bis.batch.model.CSVRow;

/**
 * interface CSVService
 * 
 * @author Richard Cao
 *
 */
public interface CSVService {

	/**
	 * 
	 * @param filePath - absolute path of the CSV file.
	 * @return
	 */
	List<CSVRow> parse(String filePath);
}
