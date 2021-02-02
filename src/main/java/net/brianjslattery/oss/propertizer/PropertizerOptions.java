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

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class PropertizerOptions {

	private final Path inputPath;
	private final Path outputPath;
	private final String dbUser;
	private final String dbPass;
	private final String dbUrl;

	public PropertizerOptions(Path inputPath, Path outputPath,
							  String dbUser, String dbPass, String dbUrl) {
		super();
		this.inputPath  = inputPath;
		this.outputPath = outputPath;
		this.dbUser     = dbUser;
		this.dbPass     = dbPass;
		this.dbUrl      = dbUrl;
	}

	public Path getInputPath() {
		return inputPath;
	}

	/**
	 * 
	 * @return the configured output path; or input path if none
	 */
	public Path getOutputPath() {
		return outputPath != null ? outputPath : inputPath;
	}
	
	public String getDbUser() {
		return dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}
	
	public boolean dbPassPresent() {
		return dbPass != null && dbPass.length() > 0;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public String toString() {
		return new StringBuilder("PropertizerOptions: inputPath=")
				.append(inputPath)
				.append(", outputPath=").append(outputPath)
				.append(", dbUser=").append(dbUser)
				.append(", dbPassPresent=").append(dbPassPresent())
				.toString();
	}

}
