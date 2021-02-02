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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class OptionsUtilities {

	/**
	 * Merges the CLI options with those loaded from a config file.
	 * Favors CLI provided options over the config file.
	 * 
	 * @param cli the options loaded from the CLI (nullable)
	 * @return a PropertizerOptions favoring the CLI options
	 * @throws IOException if file opening fails
	 */
	public static PropertizerOptions mergeAndProcess(PropertizerOptions cli) throws IOException {
		
		PropertizerOptions opts = mergeWithConfig(cli);
		
		OptionParser op = new OptionParser();
		
		Path inputPath  = opts.getInputPath();
		Path outputPath = opts.getOutputPath();
		
		SplitOption oDbUser = op.parse(opts.getDbUser());
		SplitOption oDbPass = op.parse(opts.getDbPass());
		SplitOption oDbUrl  = op.parse(opts.getDbUrl());
		
		String dbUser = oDbUser.getVal();
		String dbPass = oDbPass.getVal();
		String dbUrl  = oDbUrl.getVal();

		return new PropertizerOptions(inputPath, outputPath, dbUser, dbPass, dbUrl);
		
	}
	
	/**
	 * Merges the CLI options with those loaded from a config file.
	 * Favors CLI provided options over the config file.
	 * 
	 * @param cli the options loaded from the CLI (nullable)
	 * @return a PropertizerOptions favoring the CLI options
	 * @throws IOException if file opening fails
	 */
	private static PropertizerOptions mergeWithConfig(PropertizerOptions cli) throws IOException {
		
		PropertizerOptions p = loadFromConfigFile();
		
		if (cli != null) {
			return mergeWithConfig(cli, p);
		} else {
			return p;
		}
		
	}
	
	/**
	 * 
	 * @param cli the options loaded from the CLI
	 * @param file the options loaded from the Config file
	 * @return a PropertizerOptions favoring the CLI options
	 */
	private static PropertizerOptions mergeWithConfig(PropertizerOptions cli,
												      PropertizerOptions file) {
		
		Path inputPath  = firstNotNull(cli.getInputPath(),  file.getInputPath());
		Path outputPath = firstNotNull(cli.getOutputPath(), file.getOutputPath());
		String dbUser   = firstNotNull(cli.getDbUser(),     file.getDbUser());
		String dbPass   = firstNotNull(cli.getDbPass(),     file.getDbPass());
		String dbUrl    = firstNotNull(cli.getDbUrl(),      file.getDbUrl());
		
		return new PropertizerOptions(inputPath, outputPath, dbUser, dbPass, dbUrl);
		
	}
	
	private static PropertizerOptions loadFromConfigFile() throws IOException {
		Path cwd  = Paths.get(System.getProperty("user.dir"));
		Path conf = cwd.resolve("propertizer.properties");
		Properties p = Utilities.loadPropertiesFile(conf, "propertizer config");
		return createOptionsFromProps(p);
	}
	
	private static PropertizerOptions createOptionsFromProps(Properties p) {
		Path inputPath  = null;
		Path outputPath = null;
		
		String dbUser   = getProp(DATASOURCE_USER_PROPNAME, p);
		String dbPass   = getProp(DATASOURCE_PASS_PROPNAME, p);
		String dbUrl    = getProp(DATASOURCE_URL_PROPNAME,  p);
		
		return new PropertizerOptions(inputPath, outputPath, dbUser, dbPass, dbUrl);
	}
	
	private static String getProp(String k, Properties p) {
		return (String) p.get(k);
	}
	
	private static Path firstNotNull(Path cli, Path file) {
		System.out.println("");
		return cli != null ? cli : file;
	}
	
	private static String firstNotNull(String cli, String file) {
		return cli != null && !cli.isEmpty() ? cli : file;
	}
	
	private OptionsUtilities() {
	}
}
