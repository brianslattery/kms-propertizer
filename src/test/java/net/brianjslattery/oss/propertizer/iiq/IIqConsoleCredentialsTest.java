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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.brianjslattery.oss.propertizer.utilities.Environment;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class IIqConsoleCredentialsTest {

	private static final String USER = "the.userName";
	private static final String PASS = "th$e.passWord#";
	
	private static final String USERVAR = "USER_VAR";
	private static final String PASSVAR = "PASS_VAR_KMS";
	
	// Suffix to ensure KMS is called
	private static final String UNIT_TEST_KMS_SFX = " [decrypt-echo]";
	
	@Test
	public void simpleTest() {
		IIqConsoleCredentials icc = new IIqConsoleCredentials(USER, PASS);
		assertEquals(USER, icc.getUsername());
		assertEquals(PASS, icc.getPassword());
	}
	
	@Test
	public void testGetPairFromEnv() {
		Map<String, String> m = new HashMap<>();
		m.put(USERVAR, USER);
		m.put(PASSVAR, PASS);
		
		Environment e = environment(m);
		IIqConsoleCredentials icc = IIqConsoleCredentials.getPair(e, USERVAR, PASSVAR);
		assertEquals(USER, icc.getUsername());
		assertEquals(PASS + UNIT_TEST_KMS_SFX, icc.getPassword());
		
	}
	
	//@Test
	public void testGetPairFromEnvThrowsNoKmsPass() {
		Map<String, String> m = new HashMap<>();
		m.put(USERVAR, USER);
		m.put("NOT_KMS_ENCRYPTED", PASS);
		Environment e = environment(m);
		
		try {
			IIqConsoleCredentials icc = IIqConsoleCredentials.getPair(e, USERVAR, PASSVAR);
			assertNull("Should not get to this (should throw exception for unencrypted password.", icc);
		} catch (IllegalArgumentException iae) {
			boolean hasText = iae.getMessage().contains("passwords");
			assertTrue("Message should be thrown re: unencrypted password.", hasText);
		}
		
	}
	
	private Environment environment(Map<String, String> m) {
		return new Environment() {
			@Override
			public String get(String key) {
				return m.get(key);
			}
			@Override
			public Map<String, String> get() {
				return Collections.unmodifiableMap(m);
			}
		};
	}
	
}
