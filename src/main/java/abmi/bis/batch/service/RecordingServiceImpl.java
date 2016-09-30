package abmi.bis.batch.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import abmi.bis.batch.CustomLogger;
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
	
	@Autowired
	private CustomLogger customLogger;
	
	@Override
	public Double getRecordingLength(String fpath) {
		if (!checkFile(fpath, "Error getting recording length")) {
			return null;
		}
		
		ArrayList<String> output = new ArrayList<String>();
        if ( 0 == soxi(fpath,"-D",output) ) {
            return Double.parseDouble(output.get(0));
        }
        return null;
	}

	@Override
	public boolean convertToMPEG3(String fpath) {
		if (!checkFile(fpath, "Error converting file to mp3")) {
			return false;
		}
		
		String sep = File.separator.equals("\\") ? "\\\\" : File.separator;
		String[] dirs = fpath.split(sep);
		String newPath = settings.getTempDir() + File.separator + dirs[dirs.length-1].replace(".wav", ".mp3");
		
        String cmd = settings.getSoxCmd() + " " + fpath + " " + newPath;
        ArrayList<String> output = new ArrayList<String>();
        int res = runCmd(cmd,output);
        if ( res > 0 ) {
        	String msg = "Error converting file to mp3: "+Arrays.toString(output.toArray());
            System.out.println(msg);
            customLogger.getLogger().severe(msg);
            return false;
        }
        
        return true;
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
	
	/**
	 * Make sure a .wav file path is provide and it exists.
	 * 
	 * @param fpath
	 * @param funcName
	 * @return
	 */
	private boolean checkFile(String fpath, String funcName) {
		if (fpath == null || fpath.length() == 0 || !fpath.endsWith(".wav")) {
			String msg = funcName + ": WAV file was not provided.";
			System.out.println(msg);
			customLogger.getLogger().severe(msg);
			return false;
		}
		
		File file = new File(fpath);
		if (!file.exists()) {
			String msg = funcName + ": " + fpath + " not exist.";
			System.out.println(msg);
			customLogger.getLogger().severe(msg);
			return false;
		}
		
		return true;
	}
}
