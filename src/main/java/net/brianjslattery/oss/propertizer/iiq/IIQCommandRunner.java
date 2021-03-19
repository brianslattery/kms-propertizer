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
package net.brianjslattery.oss.propertizer.iiq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import sailpoint.launch.Launcher;

/**
 * Method to execute IIQ commands.
 * 
 * ex: runCommand("encrypt","{plaintext}")
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class IIQCommandRunner {

	// https://stackoverflow.com/a/8708357
	public static String runCommand(String... args) {
		
		// Create a stream to hold the output
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			
			try(PrintStream ps = new PrintStream(baos)) {
				// IMPORTANT: Save the old System.out!
				PrintStream old = System.out;
				// Tell Java to use your special stream
				System.setOut(ps);
				// Print some output: goes to your special stream

				Launcher.main(args);
				
				// Put things back
				System.out.flush();
				System.setOut(old);

				return baos.toString().trim();	
			}

		} catch (IOException e) {
			throw new RuntimeException("Failed to run IIQ Command.", e);
		}
		
	}
	
	public static void run(String... args) {
		String cmd = args[0];
		System.out.println("IIQCommandRunner: begin command execution for " + cmd);
		long start = System.nanoTime();
		Launcher.main(args);
		long end = System.nanoTime();
		long durationMillis = (end - start) / 1000;
		System.out.println("IIQCommandRunner: end command execution for " + cmd + ". Time spent: " + durationMillis + " millis.");
	}
	
	private IIQCommandRunner() {
	}
}
