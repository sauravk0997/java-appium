package com.disney.heath_check.clients;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;

import java.util.List;
import java.util.Map;

public class DynamoDBClient {

    private static final String DYNAMO_USER = "V+xJT2AbpaB9gAOaeIqvGtHc6iEy0/xG5mrO01ddlQ0=";
    private static final String DYNAMO_USER_KEY = "1gz4+NaWZKVKxX7W8rcaRLk3r/1WkBQR/5FvGWkqK9HgT3Wm/2RS8oUx/EbfEY8N";
    private static final CryptoTool CRYPTO_TOOL = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));

    private static DynamoDBClient dynamoDBClient = null;
    private final AmazonDynamoDB ddb;

    private DynamoDBClient() {
        BasicAWSCredentials basicAWSCredential = new BasicAWSCredentials(
                CRYPTO_TOOL.decrypt(DYNAMO_USER),
                CRYPTO_TOOL.decrypt(DYNAMO_USER_KEY));
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredential);
        ddb = AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(awsStaticCredentialsProvider)
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public static DynamoDBClient getInstance() {
        if (dynamoDBClient == null)
            dynamoDBClient = new DynamoDBClient();
        return dynamoDBClient;
    }

    public List<Map<String, AttributeValue>> getAvailableDevicesForPlatform(String platform) {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName("devices")
                .withFilterExpression("platform = :p and available = :a")
                .withExpressionAttributeValues(Map.of(":p", new AttributeValue(platform), ":a", new AttributeValue().withBOOL(true)));
        ScanResult result = ddb.scan(scanRequest);
        return result.getItems();
    }
}
