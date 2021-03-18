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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class CliUtils {
	
	public static final String NAME = "KmsPropertizer";
	
	private static final String INPUT         = "input";
	private static final String IIQ_INPUT     = "iiqInput";
	private static final String TARGET_INPUT  = "targetInput";
	private static final String OUTPUT        = "output";
	private static final String IIQ_OUTPUT    = "iiqOutput";
	private static final String TARGET_OUTPUT = "targetOutput";
	private static final String IIQ_IMPORT    = "import";
	public static final String IIQ_USER_VAR   = "iiqUserVar";
	public static final String IIQ_PASS_VAR   = "iiqPassVar";
	
	public static PropertizerOptions handleArgs(String[] args) {
		
        Options options = new Options();

        options.addOption(new Option(INPUT,         true, "Input iiq.properties path"));
        options.addOption(new Option(IIQ_INPUT,     true, "Input iiq.properties path"));
        options.addOption(new Option(TARGET_INPUT,  true, "Input target.properties path"));
        options.addOption(new Option(OUTPUT,        true, "Output iiq.properties path"));
        options.addOption(new Option(IIQ_OUTPUT,    true, "Output iiq.properties path"));
        options.addOption(new Option(TARGET_OUTPUT, true, "Ouput target.properties path"));
        options.addOption(new Option(IIQ_IMPORT,    true, "Run IIQ import. Value is the XML file."));
        options.addOption(new Option(IIQ_USER_VAR,  true, "Name of env var to get user from."));
        options.addOption(new Option(IIQ_PASS_VAR,  true, "Name of env var to get pass from."));
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            HelpFormatter formatter  = new HelpFormatter();
            formatter.printHelp(NAME, options);
            System.exit(1);
        }
        
        Path iiqInputPath   = getPath(cmd, IIQ_INPUT,     INPUT);
        Path iiqOutputPath  = getPath(cmd, IIQ_OUTPUT,    OUTPUT);
        Path targInputPath  = getPath(cmd, TARGET_INPUT,  null);
        Path targOutputPath = getPath(cmd, TARGET_OUTPUT, null);
        
        String importCmd    = getString(cmd, IIQ_IMPORT);
        String iiqUserVar   = getString(cmd, IIQ_USER_VAR);
        String iiqPassVar   = getString(cmd, IIQ_PASS_VAR);

        return new PropertizerOptions(iiqInputPath, targInputPath, iiqOutputPath, targOutputPath,
        								importCmd, iiqUserVar, iiqPassVar);
		
	}

	private static Path getPath(CommandLine cmd, String newParam, String oldParam) {
        if(cmd.hasOption(newParam)) {
        	return Paths.get(cmd.getOptionValue(newParam));
        } else if(oldParam != null && cmd.hasOption(oldParam)) {
        	System.out.println("CLI param " + oldParam + " is deprecated. Changed to " + newParam);
        	return Paths.get(cmd.getOptionValue(oldParam));
        }
        return null;
	}
	
	private static String getString(CommandLine cmd, String param) {
        if (cmd.hasOption(param)) {
        	return cmd.getOptionValue(param);
        }
        return null;
	}
	
	private CliUtils() {
	}
	
}
