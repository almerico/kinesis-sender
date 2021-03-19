package com.sender;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import com.amazonaws.services.kinesisfirehose.model.PutRecordBatchRequest;
import com.amazonaws.services.kinesisfirehose.model.Record;

public class SenderToKinesis {

	static {
		System.out.println("IMPORTANT !!!!! \n"
								   + "Please check that all environment are set \n"
								   + "export AWS_ACCESS_KEY_ID=xxx \n"
								   + "export AWS_SECRET_ACCESS_KEY=xxx\n"
								   + "export AWS_DEFAULT_REGION=us-east-1\n"
								   + "export NUMBER_SENDER_THREADS=50\n"
								   + "export NUMBER_SENDER_RECORDS=500    HERE LIMITATION IS 500\n"
								   + "export DELIVERY_STREAM_NAME=loadtest\n"
								   + "export SENDER_DEBUG_IS_ON=false\n");
	}

	protected static AmazonKinesisFirehose firehoseClient;
	public static int numberOfThreads = Integer.parseInt(System.getenv("NUMBER_SENDER_THREADS"));
	public static int numberOfRecords = Integer.parseInt(System.getenv("NUMBER_SENDER_RECORDS"));
	public static String deliveryStreamName = System.getenv("DELIVERY_STREAM_NAME");
	public static boolean debugIsOn = Boolean.parseBoolean(System.getenv("SENDER_DEBUG_IS_ON"));
	public static String region = System.getenv("AWS_DEFAULT_REGION");

	public static void main(String[] args) {
		AgentAWSCredentialsProvider agentAWSCredentialsProvider = new AgentAWSCredentialsProvider();
		System.out.println("SenderToKinesis");
		System.out.println("numberOfThreads=" + numberOfThreads);
		System.out.println("numberOfRecords=" + numberOfRecords);
		System.out.println("deliveryStreamName=" + deliveryStreamName);
		System.out.println("debugIsOn=" + debugIsOn);
		System.out.println("region=" + region);

		for (int i = 0; i < numberOfThreads; i++) {
			new Thread(new ParallelTask(),
					   "Thread - " + i).start();
		}
		if (debugIsOn) {
			System.out.println(numberOfThreads + " Sent");
		}
	}

	public static int lastDigit(int n) {
		return (n % 10);
	}

	private static List<Record> prepareData(int numberOfRecoeds) {
		List<Record> recordList = new ArrayList<>();
		for (int i = 0; i < numberOfRecoeds; i++) {

			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			format.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date d = new Date();
			String date = format.format(d);

			String template = "{\"wearable_timestamp_millis\":16363457,\"wearable_timestamp\":\"date\",\"value\":\"118.lastdigit,-33.lastdigit,17.2lastdigit,103.2lastdigit,0.0,0.0\",\"timestamp\":\"date\",\"received_timestamp\":\"date\",\"firmware_version\":1,\"device\":\"99:99:99:99:99:99\"}";
			String data = template.replace("date",
										   date).replace("lastdigit",
														 Integer.toString(lastDigit(i)));
			if (debugIsOn) {
				System.out.println(data);
			}
			Record record = new Record().withData(ByteBuffer.wrap(data.getBytes()));
			recordList.add(record);
		}
		return recordList;
	}

	private static class ParallelTask implements Runnable {

		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName()
									   + " is executing this code " + Thread.currentThread());
			sendBatch(numberOfRecords);
		}
	}

	public static void sendBatch(int numOfRecord) {
		AgentAWSCredentialsProvider agentAWSCredentialsProvider = new AgentAWSCredentialsProvider();
		firehoseClient = AmazonKinesisFirehoseClientBuilder.standard()
				.withCredentials(agentAWSCredentialsProvider)
				.withRegion(region)
				.build();
		PutRecordBatchRequest putRecordBatchRequest = new PutRecordBatchRequest();
		putRecordBatchRequest.setDeliveryStreamName(deliveryStreamName);
		List<Record> recordList = prepareData(500);
		putRecordBatchRequest.setRecords(recordList);
		firehoseClient.putRecordBatch(putRecordBatchRequest);
		recordList.clear();
	}
}
