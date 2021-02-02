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
	
	private static final String INPUT       = "input";
	private static final String OUTPUT      = "output";
	private static final String DB_USERNAME = "dbuser";
	private static final String DB_PASSWORD = "dbpass";
	private static final String DB_URL      = "dburl";
	
	public static PropertizerOptions handleArgs(String[] args) {
		
        Options options = new Options();

        addOption(INPUT,       options);
        addOption(OUTPUT,      options);
        addOption(DB_USERNAME, options);
        addOption(DB_PASSWORD, options);
        addOption(DB_URL,      options);
        
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
        
        System.out.println("The Input Path: " + cmd.getOptionValue(INPUT));
        
        Path inputPath = null;
        if (cmd.hasOption(INPUT)) {
        	inputPath = Paths.get(cmd.getOptionValue(INPUT));
        }
        
        Path outputPath = null;
        if(cmd.hasOption(OUTPUT)) {
        	outputPath = Paths.get(cmd.getOptionValue(OUTPUT));
        }

        String dbUser = cmd.getOptionValue(DB_USERNAME);
        String dbPass = cmd.getOptionValue(DB_PASSWORD);
        String dbUrl  = cmd.getOptionValue(DB_URL);
        
        return new PropertizerOptions(inputPath, outputPath, dbUser, dbPass, dbUrl);        
		
	}
		
	private static void addOption(String targetOption, Options options) {
        Option opt = new Option(targetOption, true, targetOption + " file");
        options.addOption(opt);
	}
	
	private CliUtils() {
	}
	
}
