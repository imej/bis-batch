package abmi.bis.batch.config;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import abmi.bis.batch.model.Settings;

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
	
	/**
     * Configure MessageSource to lookup any validation/error message in internationalized property files
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
    
    /**
     * DataSource bean
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
        return dataSource;
    }
    
    /**
     * JdbcTemplate bean
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    	return new JdbcTemplate(dataSource);
    }
    
    /**
     * Properties
     * @return
     */
    @Bean
    public Settings settings() {
    	Settings s = new Settings();
    	
    	s.setSoxDir(env.getRequiredProperty("sox.path"));
    	s.setTempDir(env.getRequiredProperty("temp.folder"));
    	s.setUrlPrefix(env.getRequiredProperty("url.prefix"));
    	s.setWac2wavExe(env.getRequiredProperty("wac2wav.exe"));
    	s.setSoxCmd(env.getRequiredProperty("sox.sox.exe"));
    	s.setSoxiCmd(env.getRequiredProperty("sox.soxi.exe"));
    	s.setSpFreq(Integer.parseInt(env.getRequiredProperty("sp.default.freq")));
    	s.setSpHeight(Integer.parseInt(env.getRequiredProperty("sp.default.height")));
    	s.setSpLength(Integer.parseInt(env.getRequiredProperty("sp.default.length")));
    	s.setSpRange(Integer.parseInt(env.getRequiredProperty("sp.default.range")));
    	s.setSpRes(Integer.parseInt(env.getRequiredProperty("sp.default.res")));
    	
    	return s;
    }
}
