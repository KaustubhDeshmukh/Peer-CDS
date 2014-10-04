package com.turn.ttorrent.client.peer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.turn.ttorrent.common.Torrent;

public class CloudPeer {

	private static final Logger logger =
			LoggerFactory.getLogger(CloudPeer.class);
	
	private AWSCredentials credentials = null;
	private AmazonS3 s3 = null;

	public CloudPeer() {

		super();
		credentials = new ProfileCredentialsProvider().getCredentials();
		s3 = new AmazonS3Client(credentials);
		s3.setRegion(Region.getRegion(Regions.US_WEST_2));
		
	}

	public static void main(String args[]) throws Exception{

		System.out.println("===========================================");
		System.out.println("Getting Started with Amazon S3");
		System.out.println("===========================================\n");

	}

	private static void displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null) break;

			System.out.println("    " + line);
		}
		System.out.println();
	}

	public void uploadTorrent(String bucketName, String key, String filePath, String fileName){

		if(credentials == null){
			
		}
		
		System.out.println("Creating bucket " + bucketName + "\n");

		s3.createBucket(bucketName);
		System.out.println("bucket created");

		System.out.println("Uploading a new object to S3 from a file\n");
		PutObjectResult result = s3.putObject(new PutObjectRequest(bucketName, key, new File(filePath, fileName)));
		System.out.println(fileName+" uploaded");
	}

	public void downloadPiece(String bucketName, String key, int startByteIndex, int endByteIndex) throws IOException{

		GetObjectRequest req = new GetObjectRequest(bucketName, key);
		req.setRange(startByteIndex, endByteIndex);
		S3Object object = s3.getObject(req);
		System.out.println("Downloading an object");
		System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
		System.out.println("Size: "+object.getObjectMetadata().getContentLength());

		FileOutputStream opfile = new FileOutputStream(key);
		while(true){

			int nxt = object.getObjectContent().read();
			if(nxt == -1) break;
			opfile.write(nxt);

		}
		opfile.close();
	}
}
