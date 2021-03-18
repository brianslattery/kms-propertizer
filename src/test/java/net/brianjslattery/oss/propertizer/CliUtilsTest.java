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

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;

import org.junit.Test;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class CliUtilsTest {
	
	private static final String IIQ_IN_PATH   = "iiq-in.properties";
	private static final String TARG_IN_PATH  = "targ-in.properties";
	private static final String IIQ_OUT_PATH  = "iiq-out.properties";
	private static final String TARG_OUT_PATH = "targ-out.properties";
	
	@Test
	public void testBasic() {
		String[] args = new String[] {};
 		CliUtils.handleArgs(args);
	}
	

	@Test
	public void testInputParamsOnly() {
		String[] args = new String[] { "-iiqInput",    IIQ_IN_PATH,
									   "-targetInput", TARG_IN_PATH };
 		PropertizerOptions opts = CliUtils.handleArgs(args);
		assertEquals(IIQ_IN_PATH,   opts.getInputPath().toString());
		assertEquals(TARG_IN_PATH,  opts.getTargetInputPath().toString());
		// Output should be same when not specified
		assertEquals(IIQ_IN_PATH,   opts.getOutputPath().toString());
		assertEquals(TARG_IN_PATH,  opts.getTargetOutputPath().toString());
	}
	
	@Test
	public void testInputAndOutputParams() {
		String[] args = new String[] { "-iiqInput",     IIQ_IN_PATH,
									   "-targetInput",  TARG_IN_PATH,
									   "-iiqOutput",    IIQ_OUT_PATH,
									   "-targetOutput", TARG_OUT_PATH};
		
 		PropertizerOptions opts = CliUtils.handleArgs(args);
		assertEquals(IIQ_IN_PATH,   opts.getInputPath().toString());
		assertEquals(TARG_IN_PATH,  opts.getTargetInputPath().toString());
		assertEquals(IIQ_OUT_PATH,  opts.getOutputPath().toString());
		assertEquals(TARG_OUT_PATH, opts.getTargetOutputPath().toString());
	}
	
}
