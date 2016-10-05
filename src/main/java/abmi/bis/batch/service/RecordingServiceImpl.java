package abmi.bis.batch.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import abmi.bis.batch.CustomLogger;
import abmi.bis.batch.model.CSVRow;
import abmi.bis.batch.model.Settings;
import abmi.bis.batch.model.Spectrogram;

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
        	customLogger.log(msg, Level.SEVERE);
            return false;
        }
        
        return true;
	}

	@Override
	public boolean createSpectrograms(CSVRow row) {
		ArrayList<String> output = new ArrayList<String>();
		
		if ( row.getRecordingLength() == null ) {
			row.setRecordingLength(getRecordingLength(row.getFolderPath() + File.separator + row.getFileName()));
		}
		
		if (row.getRecordingLength() != null) {
			// spectrograms will be saved in the same folder as the mp3
			// and within a subfolder named as the file name of the recording
			String spTempPath = settings.getTempDir() + File.separator 
					+ row.getFileName().substring(0, row.getFileName().length()-4);
			File dir = new File(spTempPath);
			if (!dir.mkdir()) {
				String msg = "Error creating spectrograms: canont create directory " + spTempPath;
				customLogger.log(msg, Level.SEVERE);
				return false;
			}
			
			String outPath, fileName;
			Double lenTotal = row.getRecordingLength();
			Spectrogram tempSpec;
			BufferedImage readImage = null;
			List<Spectrogram> spectrograms = new ArrayList<Spectrogram>();
			
			try {
				if (lenTotal == 0) return false;
				int segs = 1;
				double start = 0, process = settings.getSpLength();
				
				/* start with a mono version of the png files */
				String ch = "M", remix = "remix -";
				do {
					/* output file segment name */
					fileName = ch + (segs>9 ? segs : "0" + segs) + ".png";
					outPath = spTempPath + File.separator + fileName;
					
					if ( process + start > lenTotal ) process = lenTotal - start;
					
					String cmd = settings.getSoxCmd() + " " + 
					        row.getWavPath() + " -n -V" +
                            " rate " + settings.getSpFreq() + "k " + remix +
                            " trim " + start + " " + process +
                            " spectrogram -l -m -X " + settings.getSpRes() +
                            " -z " + settings.getSpRange() +
                            " -r -Y " + settings.getSpHeight() + " -o " + outPath;// + " 2>&1";
					
					output.clear();
					if (runCmd(cmd, output) > 0) {
						String msg = "Error creating spectrograms: " + output;
						customLogger.log(msg, Level.SEVERE);
						return false;
					}
					
					// add spectrogram to row
					tempSpec = new Spectrogram();
					tempSpec.setFileName(fileName);
					
					readImage = ImageIO.read(new File(outPath));
					tempSpec.setWidth(readImage.getWidth());
					tempSpec.setHeight(readImage.getHeight());
					
					spectrograms.add(tempSpec);
					segs++;
					start += process;
					if (start < lenTotal - 1) continue;
					switch (ch) {
					    case "M":
					    	/* now loop and do a left channel version */
					    	ch = "L"; 
					    	remix = "remix 1";
                            start = 0; 
                            process = settings.getSpLength();
                            segs = 1;
                            break;
                    case "L":
                            /* now loop and do a right channel version */
                            ch = "R"; 
                            remix = "remix 2";
                            start = 0; 
                            process = settings.getSpLength();
                            segs = 1;
                            break;
                    case "R":
                            /* done - break out */
                            segs = -1;
					}
				} while (segs >= 0);
			} catch (Exception e) {
				e.printStackTrace();
				String msg = "Error creating spectrograms: " + e.getMessage();
				customLogger.log(msg, Level.SEVERE);
				return false;
			}
			
			row.setSpectrograms(spectrograms);
			
			return true;
		} 
		
		// Cannot get the length of the recording.
		String msg = "Error creating spectrograms: cannot figure out recording length.";
		customLogger.log(msg, Level.SEVERE);
		
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
        
        String msg = null;
        switch (r) {
            case 1: msg = "Error running soxi: return 1 - Error in commandline parameters... command: " + cmd;
                    break;
            case 2: msg = "Error running soxi: Return 2 - Error in processing command... command: " + cmd;
                    break;
            case 3: msg = "Error running soxi: Return 3 - Exception in command processing code... command: " + cmd;
                    break;
        }
        
        if (msg != null) {
        	customLogger.log(msg, Level.SEVERE);
        }

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
			String msg = "Error running command: " + cmd + " - " + e.getLocalizedMessage(); 
			customLogger.log(msg, Level.SEVERE);
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
			customLogger.log(msg, Level.SEVERE);
			return false;
		}
		
		File file = new File(fpath);
		if (!file.exists()) {
			String msg = funcName + ": " + fpath + " not exist.";
			customLogger.log(msg, Level.SEVERE);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean copyFiles(CSVRow row) {
		String tofld = settings.getServerFolder();
		if (!tofld.endsWith(File.separator)) tofld += File.separator; 
		tofld += row.getProject() + 
				File.separator + row.getSite() + File.separator + row.getStation() + 
				File.separator + row.getYear() + File.separator + row.getRound() + File.separator;
		
		String fromfld = settings.getTempDir();
		if (!fromfld.endsWith(File.separator)) fromfld += File.separator;
		
		String fn = row.getFileName().substring(0, row.getFileName().length()-4);
		String mp3 = fn + ".mp3";
		
		try {
			FileUtils.copyFileToDirectory(new File(fromfld + mp3), new File(tofld));
		} catch (IOException e) {
			customLogger.log("Error copying file: from " + fromfld + mp3 + " to " + tofld, Level.SEVERE);
			customLogger.log("Error copying file: " + e.getMessage(), Level.SEVERE);
			e.printStackTrace();
			return false;
		}
		
		try {
			FileUtils.copyDirectory(new File(fromfld + fn), new File(tofld + fn));
		} catch (IOException e) {
			customLogger.log("Error copying folder: from " + fromfld + fn + " to " + tofld + fn, Level.SEVERE);
			customLogger.log("Error copying folder: " + e.getMessage(), Level.SEVERE);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
