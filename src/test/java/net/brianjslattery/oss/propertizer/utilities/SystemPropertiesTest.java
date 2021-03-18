package net.brianjslattery.oss.propertizer.utilities;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SystemPropertiesTest {

	@Test
	public void test() {
		Environment env = SystemProperties.getInstance();
		String home = env.get("HOME");
		assertNotNull("home should be present", home);
	}
	
	@Test
	public void testUserDir() {
		Environment env = SystemProperties.getInstance();
		String userDir = env.get("user.dir");
		assertNotNull("user dir should be present", userDir);
	}
	
}
