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

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Test;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class UtilitiesTest {
	
	@Test
	public void testLoad() throws IOException {
		String fileName = "propertizer.properties";
		Path cwd        = Paths.get(System.getProperty("user.dir"));
		Path propsFile  = cwd.resolve(fileName);
		Utilities.loadPropertiesFile(propsFile, "testing");
	}
	
	@Test
	public void testBackupFile() throws IOException {
		
		String fileName = "propertizer.properties";
		Path cwd        = Paths.get(System.getProperty("user.dir"));
		Path propsFile  = cwd.resolve(fileName);

		String testFileName = "test.properties";
		Path testFile  = cwd.resolve(testFileName);
		
		Files.copy(propsFile, testFile);
		
		Set<String> before = listBakFiles();
		Utilities.createBackupFile(testFile);
		Set<String> after = listBakFiles();
		assertEquals(before.size() + 1, after.size());
		
	}
	
	@Test
	public void testDropPrefixIfPrefixedWhenIs() {
		assertEquals("theRest", Utilities.dropPrefixIfPrefixed("TRG_theRest"));
	}
	
	@Test
	public void testDropPrefixIfPrefixedWhenIsNot() {
		assertEquals("IM_NOT_PREFIXED", Utilities.dropPrefixIfPrefixed("IM_NOT_PREFIXED"));
	}
	
	@Test
	public void testIsIiqWhenTrue() {
		assertTrue(Utilities.isIiq("IIQ_BLAH"));
	}
	
	@Test
	public void testIsIiqWhenFalse() {
		assertFalse(Utilities.isIiq("TRG_BLAH"));
	}
	
	@Test
	public void testIsTrgWhenTrue() {
		assertTrue(Utilities.isTrg("TRG_ASDF"));
	}
	
	@Test
	public void testIsTrgWhenFalse() {
		assertFalse(Utilities.isTrg("IIQ_isNotTrg"));
	}
	
	@Test
	public void testIsKmsWhenTrue() {
		assertTrue(Utilities.isKms("IIQ_DSF_KMS"));
	}
	
	@Test
	public void testIsKmsWhenFalse() {
		assertFalse(Utilities.isKms("TRG_asdfasdf_NOTKMS"));
	}
	
	@Test
	public void testDropPrefix() {
		assertEquals("SHOULD_BE_LEFT", Utilities.dropPrefix("IIQ_SHOULD_BE_LEFT"));
	}
	
	@Test
	public void testDropKmsSuffix() {
		assertEquals("KEEP__ME", Utilities.dropKmsSuffix("KEEP__ME_KMS"));
	}
	
	private static final Set<String> listBakFiles() throws IOException {
		Path cwd = Paths.get(System.getProperty("user.dir"));
		return Files.list(cwd)
					.map(p -> p.getFileName().toString())
					.filter(x -> x.endsWith(".bak"))
					.collect(toSet());
	}
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	    Constructor<Utilities> c = Utilities.class.getDeclaredConstructor();
	    assertTrue(Modifier.isPrivate(c.getModifiers()));
	    c.setAccessible(true);
	    c.newInstance();
	}

}
