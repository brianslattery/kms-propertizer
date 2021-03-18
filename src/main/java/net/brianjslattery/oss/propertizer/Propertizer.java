/**
 * Copyright © 2021 Brian J Slattery <oss@brnsl.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * 
 */
package net.brianjslattery.oss.propertizer;

import static java.util.Arrays.asList;
import static net.brianjslattery.oss.propertizer.Constants.DATASOURCE_URL_PROPNAME;
import static net.brianjslattery.oss.propertizer.Constants.DATASOURCE_USER_PROPNAME;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import net.brianjslattery.oss.propertizer.iiq.IIQEncryptor;
import net.brianjslattery.oss.propertizer.iiq.IiqImportHandler;
import net.brianjslattery.oss.propertizer.kms.DecryptionService;
import net.brianjslattery.oss.propertizer.kms.DecryptionServiceFactory;
import net.brianjslattery.oss.propertizer.utilities.Environment;
import net.brianjslattery.oss.propertizer.utilities.Utilities;

/**
 * Simple utility to handle configuring a SailPoint IIQ
 * properties files with environment sourced and KMS
 * encrypted properties.
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class Propertizer {
	
	private static final Set<String> NO_IIQ_ENC = new HashSet<>(asList(
			DATASOURCE_URL_PROPNAME,
			DATASOURCE_USER_PROPNAME));
	
	public static void main(String[] args) throws IOException {
		
		Environment env = Environment.getDefault();
		
		PropertizerOptions opts = CliUtils.handleArgs(args);
		System.out.println(opts);
		
		EnvironmentProperties eProps = EnvironmentProperties.create(env);
		
		System.out.println(eProps);
		
		if (opts.isIiqImport()) {
			IiqImportHandler.doHandle(opts);
			return;
		}
		
		handleProperties(opts, eProps);
		
		System.out.println("==========[ KMS Propertizer > Properties Complete ]==========");
		
	}
	
	private static void handleProperties(PropertizerOptions opts, EnvironmentProperties eProps) throws IOException {
		
		DecryptionService kms = DecryptionServiceFactory.getService();
		
		// Compile and store iiq.properties
		StringBuilder iiqRpt = new StringBuilder();
		Path iiqSrcPath  = eProps.getAbsoluteDirectory(opts.getInputPath());
		Properties completedIiqProps = Utilities.loadPropertiesFile(iiqSrcPath, "IIQ");
		populateFromKms(eProps.getKmsIiqProperties(),  completedIiqProps, kms, iiqRpt);
		populateRegular(eProps.getIiqProperties(),     completedIiqProps, iiqRpt);
		Path iiqDstPath = eProps.getAbsoluteDirectory(opts.getOutputPath());
		Utilities.saveProperties(completedIiqProps, iiqDstPath);
		System.out.println("==iiq.properties report==\n" + iiqRpt + "==end iiq.properties report==\n");
		
		// Compile and store target.properties
		StringBuilder targRpt = new StringBuilder();
		Path targSrcPath = eProps.getAbsoluteDirectory(opts.getTargetInputPath());
		Properties completedTrgProps = Utilities.loadPropertiesFile(targSrcPath, "Target");
		populateFromKms(eProps.getKmsTargProperties(), completedTrgProps, kms, targRpt);
		populateRegular(eProps.getTargProperties(),    completedTrgProps, targRpt);
		Path targDstPath = eProps.getAbsoluteDirectory(opts.getTargetOutputPath());
		Utilities.saveProperties(completedTrgProps, targDstPath);
		System.out.println("==target.properties report==\n" + targRpt + "==end target.properties report==\n");
		
	}
	
	private static void populateFromKms(Map<String, String> src, Properties target, DecryptionService kms, StringBuilder rpt) {
		for (Entry<String, String> e : src.entrySet()) {
			String k = e.getKey();
			String v = e.getValue();
			
			// Decrypt
			String decrypted = kms.decrypt(v);
			
			if (NO_IIQ_ENC.contains(k)) {
				target.put(k, decrypted);
				rpt.append("Added KMS encrypted key, skip IIQ encrypt for URL or User: key=[").append(k)
				   .append("], value=[").append(decrypted).append("] .\n");
			} else {
				target.put(k, IIQEncryptor.encrypt(decrypted));
				int decryptedLen = decrypted.length();
				rpt.append("Added KMS encrypted key via iiq encrypt key=[").append(k)
				   .append("], valueLength=[").append(decryptedLen).append("].\n");
			}

		}
	}
	
	private static void populateRegular(Map<String, String> src, Properties target, StringBuilder rpt) {
		for (Entry<String, String> e : src.entrySet()) {
			String k = e.getKey();
			String v = e.getValue();
			target.put(k, v);
			rpt.append("Added unencrypted key=[").append(k)
			   .append("], value=[").append(v).append("].\n");
		}
	}
	
	private Propertizer() {
	}
	
}
