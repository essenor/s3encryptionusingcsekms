package com.amazonaws.samples;

import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.s3.AmazonS3EncryptionClientV2Builder;
import com.amazonaws.services.s3.AmazonS3EncryptionV2;
import com.amazonaws.services.s3.model.CryptoConfigurationV2;
import com.amazonaws.services.s3.model.CryptoMode;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws IOException {
		String bucketName = "demo3105";
		String stringObjKeyName = "ff03fbc9-268d-4f48-bbb8-d51323cd7460";
		String fileName = "e:\\file.txt";

		//encryptObject(stringObjKeyName, bucketName, fileName);
		decryptObject(stringObjKeyName, bucketName);

	}

	public static void encryptObject(String stringObjKeyName, String bucketName, String fileName) {
		try {

			AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
					.withRegion(Regions.AP_SOUTH_1)
					.withCryptoConfiguration(
							new CryptoConfigurationV2().withCryptoMode(CryptoMode.StrictAuthenticatedEncryption))
					.withEncryptionMaterialsProvider(new KMSEncryptionMaterialsProvider(stringObjKeyName)).build();

			s3Encryption.putObject(bucketName, stringObjKeyName, new File(fileName));

			s3Encryption.shutdown();
		} catch (SdkClientException e) {
			e.printStackTrace();
		}
	}

	public static void decryptObject(String stringObjKeyName, String bucketName) throws IOException {
		try {

			AmazonS3EncryptionV2 s3Encryption = AmazonS3EncryptionClientV2Builder.standard()
					.withRegion(Regions.AP_SOUTH_1)
					.withCryptoConfiguration(
							new CryptoConfigurationV2().withCryptoMode(CryptoMode.StrictAuthenticatedEncryption))
					.withEncryptionMaterialsProvider(new KMSEncryptionMaterialsProvider(stringObjKeyName)).build();

			S3Object obj = s3Encryption.getObject(bucketName, stringObjKeyName);

			displayTextInputStream(obj.getObjectContent());

			s3Encryption.shutdown();
		} catch (SdkClientException e) {
			e.printStackTrace();
		}
	}

	private static void displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;

			System.out.println("    " + line);
		}
		System.out.println();
	}
}
