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

import net.brianjslattery.oss.propertizer.kms.DecryptionService;
import net.brianjslattery.oss.propertizer.kms.DecryptionServiceFactory;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 * 
 */
class OptionParser {
	
	private final DecryptionService decryptionService;

	public OptionParser(DecryptionService decryptionService) {
		this.decryptionService = decryptionService;
	}
	
	public OptionParser() {
		this.decryptionService = DecryptionServiceFactory.getService();
	}

	SplitOption parse(String opt) {

		if (opt == null) {
			return new SplitOption(null, null, false, false);
		}
		
		String[] split = opt.split("::", 2);
		boolean hasMeta = split.length > 1;
		
		if (!hasMeta) {
			return new SplitOption(opt, opt, false, false);
		}
		
		// Split out the metadata, e.g. ENV:KMS
		String[] meta = split[0].toUpperCase().split(":");
		
		
		String val  = split[1];
		boolean kms = false;
		boolean env = false;
	
		for (String m : meta) {
			if ("KMS".equals(m)) {
				kms = true;
			} else if ("ENV".equals(m)) {
				env = true;
			}
		}
		
		if (env) val = envVal(val);
		
		if (kms) val = kmsVal(val);
		
		return new SplitOption(opt, val, kms, env);
		
	}
	
	private static String envVal(String k) {
		String v = System.getenv(k);
		return v != null && !v.isEmpty() ? v : null;
	}
	
	private String kmsVal(String ciphertext) {
		return decryptionService.decrypt(ciphertext);
	}

}
