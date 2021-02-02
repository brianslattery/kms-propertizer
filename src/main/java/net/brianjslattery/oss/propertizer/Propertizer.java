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

import static net.brianjslattery.oss.propertizer.Constants.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Simple utility to handle configuring a SailPoint IIQ
 * properties files with environment sourced and KMS
 * encrypted properties.
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class Propertizer {
	
	public static void main(String[] args) throws IOException {

		PropertizerOptions cliOpts = CliUtils.handleArgs(args);
		
		PropertizerOptions opts = OptionsUtilities.mergeAndProcess(cliOpts);
		
		Path base = Paths.get(System.getProperty("user.dir"));
		
		Path srcPath = opts.getInputPath();
		Path dstPath = opts.getOutputPath();
		
		String iiqEncDbPass = IIQEncryptor.encrypt(opts.getDbPass());
		
		if (!srcPath.isAbsolute()) {
			srcPath = base.resolve(srcPath);
		}
		
		if (!dstPath.isAbsolute()) {
			dstPath = base.resolve(dstPath);
		}
		
		if (!Files.isRegularFile(srcPath)) {
			throw new IllegalArgumentException("File not valid: " + srcPath);
		}

		Properties p = Utilities.loadPropertiesFile(srcPath, "IIQ");
		p.put(DATASOURCE_USER_PROPNAME, opts.getDbUser());
		p.put(DATASOURCE_PASS_PROPNAME, iiqEncDbPass);
		p.put(DATASOURCE_URL_PROPNAME,  opts.getDbUrl());

		Utilities.saveProperties(p, dstPath);
		
	}

	private Propertizer() {
	}
}
