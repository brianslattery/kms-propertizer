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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Base64;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;

/**
 * 
 * @author Brian J Slattery <oss@brnsl.com>
 * 
 */
class AwsKmsService implements DecryptionService {
	
	private final KmsClient kms;
	private final String keyId;
	
	AwsKmsService(KmsClient kms, String keyId) {
		this.kms   = kms;
		this.keyId = keyId;
		System.out.println("Loaded AwsKmsService with Key " + keyId);
	}

	public String decrypt(String ciphertext) {
		
		try {
			return doDecrypt(ciphertext);
		} catch (Exception e) {
			
			int len = ciphertext.length();
			
			String start = null;
			String end   = null;
			
			if (len > 15) {
				start = ciphertext.substring(0, 8);
				end   = ciphertext.substring(ciphertext.length()-8);
			} else if (len <=15 && len > 2) {
				int sub = len / 3;
				start = ciphertext.substring(0, sub);
				end   = ciphertext.substring(ciphertext.length() - sub);
			}
			
			String errMsg = new StringBuilder("Encountered exception ")
					.append(e.getClass())
					.append(" while handling ciphertext of length ").append(len)
					.append(". start=[").append(start)
					.append("] end=[").append(end)
					.append("]. Message: ").append(e.getMessage())
					.toString();
			
			System.out.println(errMsg);
			
			throw e;
		}
		
	}
	
	private String doDecrypt(String ciphertext) {
		
		byte[] bytes = doDecode(ciphertext);
		SdkBytes ciphertextBlob = SdkBytes.fromByteArray(bytes);
		
		DecryptRequest decryptRequest = DecryptRequest
				.builder()
				.ciphertextBlob(ciphertextBlob)
				.keyId(keyId)
				.build();
		
		DecryptResponse res = kms.decrypt(decryptRequest);
		return res.plaintext().asString(UTF_8);
		
	}
	
	private byte[] doDecode(String ciphertext) {
		return Base64.getDecoder().decode(ciphertext);
	}
	
}
