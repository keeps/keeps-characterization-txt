package pt.keep.validator.txt;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import pt.keep.validator.txt.result.Result;
import pt.keep.validator.txt.result.ValidationInfo;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

public class TxtCharacterizationTool {
	private static String version = "1.0";

	public String getVersion(){
		return version;
	}
	public String run(File f, List<String> acceptedCharsets, int confidence) {
		try {
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Result res = process(f,acceptedCharsets,confidence);
			JAXBContext jaxbContext = JAXBContext.newInstance(Result.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(res, bos);
			return bos.toString("UTF-8");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}

	public Result process(File f, List<String> acceptedCharsets, int confidence) {
		Result res = new Result();
		Map<String,String> features = new HashMap<String,String>();
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			CharsetDetector cd = new CharsetDetector();
			cd.setText(bis);

			CharsetMatch cm = cd.detect();
			if(cm!=null){
				boolean found = false;
				for(String s : acceptedCharsets){
					if(s.equalsIgnoreCase(cm.getName())){
						if(cm.getConfidence()>=confidence){
							ValidationInfo vi = new ValidationInfo();
							vi.setValid(true);
							res.setValidationInfo(vi);
							Reader reader = cm.getReader();
							BufferedReader br = new BufferedReader(reader);
							int numberOfLines = 0;
							String line = br.readLine();
							while (line != null) {
								numberOfLines++;
								line = br.readLine();
							}
							
							features.put("numberOfLines", ""+numberOfLines);
							features.put("charset", cm.getName());
						}else{
							ValidationInfo vi = new ValidationInfo();
							vi.setValid(false);
							vi.setValidationError("The file have an acceptable encoding, but the confidence is too low ("+cm.getConfidence()+" < "+confidence+")");
							res.setValidationInfo(vi);
						}
						found=true;
						break;
					}
				}
				if(!found){
					ValidationInfo vi = new ValidationInfo();
					vi.setValid(false);
					vi.setValidationError("Encoding "+cm.getName()+" is not accepted");
					res.setValidationInfo(vi);
					
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			ValidationInfo validationInfo = new ValidationInfo();
			validationInfo.setValid(false);
			validationInfo.setValidationError(e.getMessage());
			res.setValidationInfo(validationInfo);
		}
		res.setFeatures(features);
		return res;
	}

	


	private void printHelp(Options opts) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar [jarFile]", opts);
	}
	
	private void printVersion() {
		System.out.println(version);
	}
	
	
	public static void main(String[] args) {
		try {
			TxtCharacterizationTool rct = new TxtCharacterizationTool();
			Options options = new Options();
			options.addOption("f", true, "file to analyze");
			options.addOption("a", true, "accepted charset(delimited with , )");
			options.addOption("c", true, "minimum confidence (optional)");
			options.addOption("v", false, "print this tool version");
			options.addOption("h", false, "print this message");

			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("h")) {
				rct.printHelp(options);
				System.exit(0);
			}
			
			if (cmd.hasOption("v")) {
				rct.printVersion();
				System.exit(0);
			}

			if (!cmd.hasOption("f")) {
				rct.printHelp(options);
				System.exit(0);
			}
			
			List<String> acceptedCharsets = new ArrayList<String>();
			if (!cmd.hasOption("a")) {
				rct.printHelp(options);
				System.exit(0);
			}else{
				acceptedCharsets = Arrays.asList(cmd.getOptionValue("a").split(","));
			}
			
			int confidence=0;
			if (cmd.hasOption("c")) {
				confidence = Integer.parseInt(cmd.getOptionValue("c"));
			}

			File f = new File(cmd.getOptionValue("f"));
			if (!f.exists()) {
				System.out.println("File doesn't exist");
				System.exit(0);
			}
			String toolOutput = rct.run(f,acceptedCharsets,confidence);
			if(toolOutput!=null){
				System.out.println(toolOutput);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
