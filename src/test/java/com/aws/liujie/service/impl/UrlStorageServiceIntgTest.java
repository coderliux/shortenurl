package com.aws.liujie.service.impl;

import com.aws.liujie.config.AwsClientProperties;
import com.aws.liujie.config.DynamodbTableConst;
import com.aws.liujie.service.UrlStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlStorageServiceIntgTest {
    @Autowired
    UrlStorageService urlStorageService;
    @Autowired
    DynamoDbClient client;

    @Autowired
    AwsClientProperties clientProperties;

    @Test
    public void shouldStoreUrlAndQuerySuccess() {
        dropAndCreateTable();
        String id = "intg_test_resourceId";
        String url = "http://www.test.com/test";
        boolean result = urlStorageService.storeUrl(id, url);
        assertTrue(result);
        String storedUrl = urlStorageService.getUrl(id);
        assertEquals(url, storedUrl);
    }


    public void dropAndCreateTable() {
        String tableName = clientProperties.getDynamoTableName();
        CreateTableRequest tableRequest = CreateTableRequest.builder()
                .tableName(tableName)
                .keySchema(KeySchemaElement.builder()
                        .attributeName(DynamodbTableConst.KEY_RESOURCE_ID)
                        .keyType(KeyType.HASH).build())
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName(DynamodbTableConst.KEY_URL)
                        .attributeType(ScalarAttributeType.S).build())
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName(DynamodbTableConst.KEY_RESOURCE_ID)
                        .attributeType(ScalarAttributeType.S).build()
                )
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build())
                .build();
        DescribeTableRequest describeTableRequest = DescribeTableRequest.builder()
                .tableName(tableName).build();
        try {
            DescribeTableResponse describeTable = client.describeTable(describeTableRequest);
        } catch (ResourceNotFoundException e) {
            createTable(tableRequest);
        }
    }

    private void createTable(CreateTableRequest tableRequest) {
        CreateTableResponse table = client.createTable(tableRequest);
        assertTrue(true);
    }
}