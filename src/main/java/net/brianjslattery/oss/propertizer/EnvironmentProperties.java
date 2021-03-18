package net.brianjslattery.oss.propertizer;

import static net.brianjslattery.oss.propertizer.utilities.Utilities.dropKmsSuffix;
import static net.brianjslattery.oss.propertizer.utilities.Utilities.dropPrefix;
import static net.brianjslattery.oss.propertizer.utilities.Utilities.isIiq;
import static net.brianjslattery.oss.propertizer.utilities.Utilities.isKms;
import static net.brianjslattery.oss.propertizer.utilities.Utilities.isTrg;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.brianjslattery.oss.propertizer.utilities.Environment;

/**
 * 
 * Collects the environment properties based on prefix/suffix.
 * Subsort them by whether or not they are KMS encrypted for
 * further processing.
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 *
 */
public class EnvironmentProperties {

	private static final String DOUBLE_PCT = "%%";
	
	private static final String USER_DIRECTORY = "user.dir";
	
	private Path currentDirectory;
	private String outputPath;
	
	private Map<String, String> targProperties;
	private Map<String, String> iiqProperties;
	private Map<String, String> kmsTargProperties;
	private Map<String, String> kmsIiqProperties;
	
	private EnvironmentProperties() {
	}
	
	public static EnvironmentProperties create(Environment env) {
		
		String userDir = env.get(USER_DIRECTORY);
		
		EnvironmentProperties ep = new EnvironmentProperties();
		ep.currentDirectory = Paths.get(userDir);
		
		ep.targProperties    = new HashMap<>();
		ep.iiqProperties     = new HashMap<>();
		ep.kmsTargProperties = new HashMap<>();
		ep.kmsIiqProperties  = new HashMap<>();
		
		Map<String, String> envVars = env.get();
		
		List<String> discards = new ArrayList<>();
		
		envVars.forEach((k,v) -> {
			
			String key = k;
			
			boolean isIiq = isIiq(key);
			boolean isTrg = isTrg(key);
			
			if (isIiq || isTrg) key = dropPrefix(k);
			
			boolean isKms = isKms(key);
			
			if (isKms) key = dropKmsSuffix(key);
			
			key = doubleUnderscoreToPeriod(key);
			
			if(isKms) { // drop suffix, add to KMS Props
				if (isIiq) {
					ep.kmsIiqProperties.put(key, v);
				} else if (isTrg) {
					ep.kmsTargProperties.put(key, buildTargetParam(v));
				}
			} else if(isIiq) {
				ep.iiqProperties.put(key, v);
			} else if (isTrg) {
				ep.targProperties.put(key, buildTargetParam(v));
			} else {
				discards.add(k);
			}
			
		});
		
		discards.remove(USER_DIRECTORY);
		if (!discards.isEmpty()) {
			System.out.println("---> Discarded unknown ENV vars: " + discards);
		}
		
		return ep;
		
	}
	
	private static String buildTargetParam(String v) {
		return DOUBLE_PCT + v + DOUBLE_PCT;
	}

	private static String doubleUnderscoreToPeriod(String k) {
		return k.replace("__", ".");
	}
	
	public Path getAbsoluteDirectory(Path p) {
		if (p.isAbsolute()) {
			return p;
		} else {
			return currentDirectory.resolve(p);
		}
	}

	public String getOutputPath() {
		return outputPath;
	}

	public Map<String, String> getTargProperties() {
		return targProperties;
	}

	public Map<String, String> getIiqProperties() {
		return iiqProperties;
	}
	
	public Map<String, String> getKmsTargProperties() {
		return kmsTargProperties;
	}

	public Map<String, String> getKmsIiqProperties() {
		return kmsIiqProperties;
	}

	@Override
	public String toString() {
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append("==========[KMS Propertizer Environment]==========")
		  .append(USER_DIRECTORY).append(currentDirectory)
		  .append("\n> Target Properties <\n");
		
		if (targProperties.isEmpty()) {
			sb.append("  !! Target Properties is EMPTY !!\n");
		} else {
			targProperties.forEach((k,v) -> sb.append("    * ").append(k));
		}
		
		sb.append("\n> Target KMS Properties <\n");
		if (kmsTargProperties.isEmpty()) {
			sb.append("  !! KMS Target Properties is EMPTY !!\n");
		} else {
			kmsTargProperties.forEach((k,v) -> sb.append("    * ").append(k));
		}
		
		sb.append("\n> IIQ Properties <\n");
		if (iiqProperties.isEmpty()) {
			sb.append("  !! IIQ Properties is EMPTY !!\n");
		} else {
			sb.append("\n> IIQ Properties <\n");
			iiqProperties.forEach((k,v) -> sb.append("\n* ").append(k));
		}
		
		sb.append("\n> IIQ KMS Properties <\n");
		if (kmsIiqProperties.isEmpty()) {
			sb.append("  !! IIQ KMS Properties is EMPTY !!\n");
		} else {
			sb.append("\n> IIQ KMS Properties <\n");
			kmsIiqProperties.forEach((k,v) -> sb.append("\n* ").append(k));
		}
		
		return sb.toString();
		
	}
	
}
