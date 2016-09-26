package abmi.bis.batch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.springframework.stereotype.Component;

@Component("txtLogFormatter")
public class TxtLogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		
		StringBuffer buf = new StringBuffer(1000);
		
		buf.append(calcDate(record.getMillis()));
		buf.append(" [");
		buf.append(record.getLevel());
		buf.append("] ");
		buf.append(formatMessage(record));
		buf.append('\n');
		
		return buf.toString();
	}
	
	private String calcDate(long millisecs) {
		SimpleDateFormat dtFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
		Date resultDate = new Date(millisecs);
		return dtFormat.format(resultDate);
	}

}
