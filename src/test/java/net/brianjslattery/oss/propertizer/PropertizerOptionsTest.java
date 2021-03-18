package net.brianjslattery.oss.propertizer;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

public class PropertizerOptionsTest {
	
	private static final Path IIQ_IN_PATH    = Path.of("iiq-in.properties");
	private static final Path IIQ_OUT_PATH   = Path.of("iiq-out.properties");
	private static final Path TARG_IN_PATH   = Path.of("targ-in.properties");
	private static final Path TARG_OUT_PATH  = Path.of("targ-out.properties");
	private static final String IMPORT_CMD   = "file.xml";
	private static final String IIQ_USER_VAR = "iiqUser";
	private static final String IIQ_PASS_VAR = "targ-out.properties";
	
	private PropertizerOptions opts;
	
	@Before
	public void init() {
		opts = new PropertizerOptions(IIQ_IN_PATH, TARG_IN_PATH, IIQ_OUT_PATH, TARG_OUT_PATH,
										   IMPORT_CMD, IIQ_USER_VAR, IIQ_PASS_VAR);
	}
	
	@Test
	public void test() {
		assertEquals(IIQ_IN_PATH,   opts.getInputPath());
		assertEquals(TARG_IN_PATH,  opts.getTargetInputPath());
		assertEquals(IIQ_OUT_PATH,  opts.getOutputPath());
		assertEquals(TARG_OUT_PATH, opts.getTargetOutputPath());
		assertEquals(IMPORT_CMD,    opts.getImportCommand());
		assertEquals(IIQ_USER_VAR,  opts.getIiqUserVar());
		assertEquals(IIQ_PASS_VAR,  opts.getIiqPassVar());
	}

}
