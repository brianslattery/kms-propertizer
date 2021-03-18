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

import static net.brianjslattery.oss.propertizer.utilities.Utilities.*;
import net.brianjslattery.oss.propertizer.CliUtils;
import net.brianjslattery.oss.propertizer.kms.DecryptionService;
import net.brianjslattery.oss.propertizer.kms.DecryptionServiceFactory;
import net.brianjslattery.oss.propertizer.utilities.Environment;
import net.brianjslattery.oss.propertizer.utilities.Utilities;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class IIqConsoleCredentials {

	private final String username;
	private final String password;

	public static IIqConsoleCredentials getPair(Environment env, String userVar, String passVar) {
		
		throwIfNullOrEmpty(userVar, CliUtils.IIQ_USER_VAR);
		throwIfNullOrEmpty(passVar, CliUtils.IIQ_PASS_VAR);
		
		String userVarName = userVar;
		String passVarName = passVar;
		
		boolean userIsKms = isKms(userVarName);
		boolean passIsKms = isKms(passVarName);
	
		// Enforce proper security
		if (!passIsKms) {
			throw new IllegalArgumentException("Do not pass passwords without KMS. Variable should end with _KMS.");
		}
		
		String username = env.get(userVarName);
		String password = env.get(passVarName);

		throwIfNullOrEmpty(username, userVarName);
		throwIfNullOrEmpty(password, passVarName);
		
		DecryptionService kms = DecryptionServiceFactory.getService();
		
		if (userIsKms) username = kms.decrypt(username);
		password = kms.decrypt(password);
		
		return new IIqConsoleCredentials(username, password);
		
	}
	
	public IIqConsoleCredentials(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	private static void throwIfNullOrEmpty(String prop, String propName) {
		if (prop == null || prop.isEmpty()) {
			throw new IllegalArgumentException("Property [" + propName + "] cannot be null or empty.");
		}
	}

}
