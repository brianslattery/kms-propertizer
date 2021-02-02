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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Test;

/**
 * Note: the PROP_* vars at the top must match the values
 * in `propertizer.properties` for these tests to pass.
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class OptionsUtilitiesTest {
	
	public static final String PROP_FILE_USER = "test-username";
	public static final String PROP_FILE_PASS = "test-password";
	public static final String PROP_FILE_URL  = "test-url";
	
	@Test
	public void testWithoutCli() throws IOException {
		
		PropertizerOptions opts = OptionsUtilities.mergeAndProcess(null);

		assertEquals(PROP_FILE_USER, opts.getDbUser());
		assertEquals(PROP_FILE_PASS, opts.getDbPass());
		assertEquals(PROP_FILE_URL,  opts.getDbUrl());
	}
	
	@Test
	public void testCliOverridesFile() throws IOException {
		
		String dbUser = "theDbUser!";
		String dbPass = "theDbP@$$!";
		String dbUrl  = "mysql://is-your-sql";
		
		PropertizerOptions cliOpts = new PropertizerOptions(null, null, dbUser, dbPass, dbUrl);
		PropertizerOptions opts = OptionsUtilities.mergeAndProcess(cliOpts);

		assertEquals(dbUser, opts.getDbUser());
		assertEquals(dbPass, opts.getDbPass());
		assertEquals(dbUrl,  opts.getDbUrl());
		
	}
	
	@Test
	public void testCliAndFileHybrid() throws IOException {
		
		String dbUser = "theDbUser!";
		String dbPass = "theDbP@$$!";
		
		PropertizerOptions cliOpts = new PropertizerOptions(null, null, dbUser, dbPass, null);
		PropertizerOptions opts = OptionsUtilities.mergeAndProcess(cliOpts);

		assertEquals(dbUser,        opts.getDbUser());
		assertEquals(dbPass,        opts.getDbPass());
		assertEquals(PROP_FILE_URL, opts.getDbUrl());
		
	}
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	    Constructor<OptionsUtilities> c = OptionsUtilities.class.getDeclaredConstructor();
	    assertTrue(Modifier.isPrivate(c.getModifiers()));
	    c.setAccessible(true);
	    c.newInstance();
	}

}
