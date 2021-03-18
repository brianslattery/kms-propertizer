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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import net.brianjslattery.oss.propertizer.utilities.Environment;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 *
 */
public class EnvironmentPropertiesTest {
	
	private static final String IIQ_PROP_A        = "IIQ_PROP_A";
	private static final String IIQ_PROP_A_EXPECT = "PROP_A";
	private static final String IIQ_PROP_B        = "IIQ_PROP_B_KMS";
	private static final String IIQ_PROP_B_EXPECT = "PROP_B";
	private static final String TRG_PROP_A        = "TRG_PROP_A_KMS";
	private static final String TRG_PROP_A_EXPECT = "PROP_A";
	private static final String TRG_PROP_B        = "TRG_PROP_B";
	private static final String TRG_PROP_B_EXPECT = "PROP_B";
	
	private static final String IIQ_PROP_A_VAL = "IIQ_4iiqitself";
	private static final String IIQ_PROP_B_VAL = "IIQ_other.for-iiq!$12";
	private static final String TRG_PROP_A_VAL = "TRG_PROP-A.something-targ";
	private static final String TRG_PROP_B_VAL = "TRG_the-second-targ-prp";
	
	private static final String DOUBLE_PCT = "%%";
	
	@Test
	public void testWithAll() {
		
		Map<String, String> m = new HashMap<>();
		m.put(TRG_PROP_A, TRG_PROP_A_VAL);
		m.put(TRG_PROP_B, TRG_PROP_B_VAL);
		m.put(IIQ_PROP_A, IIQ_PROP_A_VAL);
		m.put(IIQ_PROP_B, IIQ_PROP_B_VAL);
		
		EnvironmentProperties ep = getProps(m);
		
		// Count Tests
		assertOne("IIQ",          ep.getIiqProperties());
		assertOne("IIQ (KMS)",    ep.getKmsIiqProperties());
		assertOne("Target",       ep.getTargProperties());
		assertOne("Target (KMS)", ep.getKmsTargProperties());

		// Assert Proper IIQ Values
		String iiqA = ep.getIiqProperties().get(IIQ_PROP_A_EXPECT);
		assertEquals(IIQ_PROP_A_VAL, iiqA);
		String iiqB = ep.getKmsIiqProperties().get(IIQ_PROP_B_EXPECT);
		assertEquals(IIQ_PROP_B_VAL, iiqB);

		// Assert Proper Target Values 
		String targA = ep.getKmsTargProperties().get(TRG_PROP_A_EXPECT);
		assertEquals(DOUBLE_PCT + TRG_PROP_A_VAL + DOUBLE_PCT, targA);
		String targB = ep.getTargProperties().get(TRG_PROP_B_EXPECT);
		assertEquals(DOUBLE_PCT + TRG_PROP_B_VAL + DOUBLE_PCT, targB);
		
	}
	
	@Test
	public void testWithJustIiq() {
		
		Map<String, String> m = new HashMap<>();
		m.put(IIQ_PROP_A, IIQ_PROP_A_VAL);
		m.put(IIQ_PROP_B, IIQ_PROP_B_VAL);
		
		EnvironmentProperties ep = getProps(m);
		
		// Count Tests
		assertOne("IIQ",           ep.getIiqProperties());
		assertOne("IIQ (KMS)",     ep.getKmsIiqProperties());
		assertNone("Target",       ep.getTargProperties());
		assertNone("Target (KMS)", ep.getKmsTargProperties());
		
		// Assert proper IIQ Values
		String iiqA = ep.getIiqProperties().get(IIQ_PROP_A_EXPECT);
		assertEquals(IIQ_PROP_A_VAL, iiqA);
		String iiqB = ep.getKmsIiqProperties().get(IIQ_PROP_B_EXPECT);
		assertEquals(IIQ_PROP_B_VAL, iiqB);
		
	}
	
	@Test
	public void testWithJustTarget() {
		
		Map<String, String> m = new HashMap<>();
		m.put(TRG_PROP_A, TRG_PROP_A_VAL);
		m.put(TRG_PROP_B, TRG_PROP_B_VAL);
		
		EnvironmentProperties ep = getProps(m);
		
		// Count Tests
		assertOne("Target",       ep.getTargProperties());
		assertOne("Target (KMS)", ep.getKmsTargProperties());
		assertNone("IIQ",         ep.getIiqProperties());
		assertNone("IIQ (KMS)",   ep.getKmsIiqProperties());
		
		// Assert Proper Target Values 
		String targA = ep.getKmsTargProperties().get(TRG_PROP_A_EXPECT);
		assertEquals(DOUBLE_PCT + TRG_PROP_A_VAL + DOUBLE_PCT, targA);
		String targB = ep.getTargProperties().get(TRG_PROP_B_EXPECT);
		assertEquals(DOUBLE_PCT + TRG_PROP_B_VAL + DOUBLE_PCT, targB);

	}
	
	@Test
	public void testDoubleUnderscoreChangesToPeriod_dbUser() {
		String expect = "dataSource.username";
		String envVar = "IIQ_dataSource__username_KMS";
		baseTestIiqDoubleUnderscoreChangesToPeriod(expect, envVar);
	}
	
	@Test
	public void testDoubleUnderscoreChangesToPeriod_dbPass() {
		String expect = "dataSource.password";
		String envVar = "IIQ_dataSource__password_KMS";
		baseTestIiqDoubleUnderscoreChangesToPeriod(expect, envVar);
	}
	
	@Test
	public void testDoubleUnderscoreChangesToPeriod_dbUrl() {
		String expect = "dataSource.url";
		String envVar = "IIQ_dataSource__url_KMS";
		baseTestIiqDoubleUnderscoreChangesToPeriod(expect, envVar);
	}
	
	public void baseTestIiqDoubleUnderscoreChangesToPeriod(String expectKey, String envVar) {
		
		String v = "the-value!";
				
		Map<String, String> m = new HashMap<>();
		m.put(envVar, v);
		
		EnvironmentProperties ep = getProps(m);
		
		// Count Tests
		assertNone("Target",       ep.getTargProperties());
		assertNone("Target (KMS)", ep.getKmsTargProperties());
		assertNone("IIQ",          ep.getIiqProperties());
		assertOne("IIQ (KMS)",     ep.getKmsIiqProperties());
		
		// Assert Proper Target Values 
		String out = ep.getKmsIiqProperties().get(expectKey);
		assertEquals(v, out);

	}
	
	private void assertOne(String type, Map<String, String> p) {
		assertEquals("Should have one " + type + " properties.", 1, p.size());
	}

	private void assertNone(String type, Map<String, String> p) {
		assertTrue("Should have NO " + type + " properties.", p.isEmpty());
	}
	
	private EnvironmentProperties getProps(Map<String, String> m) {
		m.put("user.dir", System.getProperty("user.dir"));
		Environment e = environment(m);
		return EnvironmentProperties.create(e);	
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
