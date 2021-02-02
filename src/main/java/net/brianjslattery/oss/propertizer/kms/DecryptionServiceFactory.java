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
package net.brianjslattery.oss.propertizer.kms;

import software.amazon.awssdk.services.kms.KmsClient;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
public class DecryptionServiceFactory {
	
	public static DecryptionService getService() {
		
		String keyId = getKey();
		
		if (keyId != null) {
			KmsClient k = KmsClient
					.builder()
					.build();
			
			return new AwsKmsService(k, keyId);
		} 
		
		return createStub();
	}
	
	private static String getKey() {
		return System.getenv("KMS_KEY_ID");
	}
	
	private static DecryptionService createStub() {
		return c -> {
			System.out.println("KMS ID not present - echoing cypher");
			return c + " [decrypt-echo]";
		};
	}
	
	private DecryptionServiceFactory() {
	}
	
}
