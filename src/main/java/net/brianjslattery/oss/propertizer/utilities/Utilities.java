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
package net.brianjslattery.oss.propertizer.utilities;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class Utilities {
	
	private static final String RETURN_EMPTY_PROPS = "Props file does not exist at '%s' return empty.";

	private static final String TARGET_PFX = "TRG_";
	private static final String IIQ_PFX    = "IIQ_";
	// keep both TRG_ and IIQ_ the same length or update code
	private static final int PFX_LEN = 4;
	
	private static final String KMS_SFX    = "_KMS";
	private static final int KMS_SFX_LEN   = KMS_SFX.length();
	
	/**
	 * 
	 * @param path a Path representing the absolute path to the file
	 * @param type the type of properties for logging (e.g. config, IIQ)
	 * @return a loaded Properties object
	 * @throws IOException
	 */
	public static Properties loadPropertiesFile(Path path, String type) throws IOException {
		Properties p = new Properties();
		
		if (Files.notExists(path)) {
			System.out.println(String.format(RETURN_EMPTY_PROPS, path.toAbsolutePath()));
			return p;
		}
		
		try(InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
			p.load(is);
			System.out.println("Loaded " + type + " successfully at: " + path);
		}
		
		return p;
	}
	
	public static void createBackupFile(Path path) throws IOException {
		if (Files.exists(path)) {
			Files.move(path, getBackupFilePath(path));
			System.out.println("Created a backup of (" + path + ")");
		} else {
			System.out.println("No backup created - no previous file. (" + path + ")");
		}
	}
	
	private static Path getBackupFilePath(Path path) {
		String dtString = LocalDateTime.now().format(ISO_LOCAL_DATE_TIME);
		dtString = dtString.replace(':', '-');
		return Paths.get(path.toString() + "-" + dtString + ".bak");
	}
	
	public static void saveProperties(Properties p, Path path) throws IOException {
		
		Utilities.createBackupFile(path);		

		Files.createFile(path);

		String comments = buildComments();
		
		try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.WRITE)) {
			p.store(os, comments);
			System.out.println("Stored output properties successfully at: " + path);
		}
		
	}
	
	public static String dropPrefixIfPrefixed(String k) {
		if (isIiq(k) || isTrg(k)) return dropPrefix(k);
		return k;
	}
	
	public static boolean isIiq(String k) {
		return k.startsWith(IIQ_PFX);
	}
	
	public static boolean isTrg(String k) {
		return k.startsWith(TARGET_PFX);
	}
	
	public static boolean isKms(String k) {
		return k.endsWith(KMS_SFX);
	}
	
	public static String dropPrefix(String k) {
		return k.substring(PFX_LEN);
	}
	
	public static String dropKmsSuffix(String k) {
		int end = k.length() - KMS_SFX_LEN;
		return k.substring(0, end);
	}
	
	private static String buildComments() {
		return new StringBuilder()
				.append("Built On: ")
				.append(System.getenv("HOSTNAME"))
				.toString();
	}
	
	
	private Utilities() {
	}
	
}
