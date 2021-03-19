
package com.sender;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

public class AgentAWSCredentialsProvider implements AWSCredentialsProvider {

	public AWSCredentials getCredentials() {
		String accessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
		String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
		try {
			return new BasicAWSCredentials(accessKeyId,
										   secretKey);
		} catch (Exception exception) {
			throw new AmazonClientException("Unable to load credentials from agent config. Missing entries: ");
		}
	}

	@Override
	public void refresh() {

	}
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
