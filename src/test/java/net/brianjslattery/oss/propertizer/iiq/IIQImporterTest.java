package net.brianjslattery.oss.propertizer.iiq;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class IIQImporterTest {
	
	@Test
	public void testPrivateConstructor() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	    Constructor<IIQImporter> c = IIQImporter.class.getDeclaredConstructor();
	    assertTrue(Modifier.isPrivate(c.getModifiers()));
	    c.setAccessible(true);
	    c.newInstance();
	}
	
}
