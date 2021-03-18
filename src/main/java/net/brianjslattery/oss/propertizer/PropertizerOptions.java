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
	private final Path targetInputPath;
	private final Path outputPath;
	private final Path targetOutputPath;
	private final String importCommand;
	private final String iiqUserVar;
	private final String iiqPassVar;

	public PropertizerOptions(Path inputPath,  Path targetInputPath,
					          Path outputPath, Path targetOutputPath,
					          String importCommand, String iiqUserVar, String iiqPassVar) {
		super();
		this.inputPath        = inputPath;
		this.targetInputPath  = targetInputPath;
		this.outputPath       = outputPath;
		this.targetOutputPath = targetOutputPath;
		this.importCommand    = importCommand;
		this.iiqUserVar       = iiqUserVar;
		this.iiqPassVar       = iiqPassVar;
	}

	public Path getInputPath() {
		return inputPath;
	}
	
	public Path getTargetInputPath() {
		return targetInputPath;
	}

	/**
	 * 
	 * @return the configured iiq output path; or iiq input path if none
	 */
	public Path getOutputPath() {
		return outputPath != null ? outputPath : inputPath;
	}
	
	/**
	 * 
	 * @return the configured target output path; or target input path if none
	 */
	public Path getTargetOutputPath() {
		return targetOutputPath != null ? targetOutputPath : targetInputPath;
	}
	
	public boolean isIiqImport() {
		return getImportCommand() != null;
	}
	
	public String getIiqUserVar() {
		return iiqUserVar;
	}

	public String getIiqPassVar() {
		return iiqPassVar;
	}

	public String getImportCommand() {
		return importCommand;
	}
	
	public String toString() {
		return new StringBuilder("PropertizerOptions: ")
				.append("inputPath=").append(inputPath)
				.append(", targetInputPath=").append(targetInputPath)
				.append(", outputPath=").append(outputPath)
				.append(", targetOutputPath=").append(targetOutputPath)
				.toString();
	}

}
