package abmi.bis.batch;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Application configuration file.
 * 
 * @author richard
 *
 */
@Configuration
@ComponentScan(basePackages={"abmi.bis.batch"})
@PropertySource(value = { "classpath:application.properties" })
public class AppConfig {

	@Autowired
	private Environment env;
	
	/**
	 * FileHandler bean for logger
	 * - the log files are saved in a folder indicated in the properties file
	 * - the name of the log files is the current time .log
	 * 
	 * @param formatter
	 * @return
	 */
	@Bean
	public FileHandler fileHander(Formatter formatter) {
		FileHandler fh = null;
		
		// create directory if not exist
		File file = new File(env.getRequiredProperty("log.root_folder") 
				+ File.separator 
				+ env.getRequiredProperty("log.folder_name"));
		
		if (!file.exists()) {
			file.mkdir();
		}
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date currentTime = new Date();
		String logName = dtFormat.format(currentTime) + ".log";
		
		try {
			fh = new FileHandler(file.getPath() + File.separator + logName);
			fh.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fh;
	}
}
