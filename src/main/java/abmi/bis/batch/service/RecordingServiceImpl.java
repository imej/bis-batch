package abmi.bis.batch.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import abmi.bis.batch.model.Settings;

/**
 * Implement RecordingService with SoX. Mainly from Paul Morrill's code.
 * 
 * Refer to Sox man page for full details of parameters we use:
 * http://sox.sourceforge.net/sox.html
 * 
 * To get the length of the recording: soxi.exe -D wav_file_path
 * To convert a wac to mp3: sox.exe wav_file_path mp3_file_path
 * 
 * @author Richard.Cao
 *
 */
@Service("recordingService")
public class RecordingServiceImpl implements RecordingService {

	@Autowired
	private Settings settings;
	
	@Override
	public Double getRecordingLength(String fpath) {
		ArrayList<String> output = new ArrayList<String>();
        if ( 0 == soxi(fpath,"-D",output) ) {
                return Double.parseDouble(output.get(0));
        }
        return null;
	}

	@Override
	public boolean convertToMPEG3(String fpath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createSpectrograms(String fpath) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * soxi.exe is a copy of the sox.exe
	 * 
	 * @param filep
	 * @param param
	 * @param output
	 * @return
	 */
	private int soxi(String filep, String param, ArrayList<String> output) {
		String cmd = settings.getSoxiCmd() + " " + param + " " + filep;
        int r = runCmd(cmd,output);
        if ( r == 1 ) System.out.println("Return 1 - Error in commandline parameters...");
        if ( r == 2 ) System.out.println("Return 2 - Error in processing command...");
        if ( r == 3 ) System.out.println("Return 3 - Exception in command processing code...");
        return r;
	}
	
	/**
	 * http://stackoverflow.com/questions/14542448/capture-the-output-of-an-external-program-in-java
     * SoX return values are 0 - success; 1 - cmdline parameter problem; 2 - error in processing
     * 
	 * @param cmd
	 * @param output
	 * @return
	 */
	private int runCmd(String cmd, ArrayList<String> output) {
		Runtime r = Runtime.getRuntime();
		Process p;
		BufferedReader is;
		String line;
		
		try {
			p = r.exec(cmd);
			is = new BufferedReader(new InputStreamReader(p.getInputStream()));
			p.waitFor();
			while ((line = is.readLine()) != null) {
				output.add(line);
			}
			
			return p.exitValue();
		} catch (IOException | InterruptedException e) {
			System.out.println(e.getMessage());
		}
		
		return 3;
	}
}
