package com.aws.liujie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShortUrlApplicationIntgTest {

    @Autowired
    DynamoDbClient dynamoDbClient;

    @Test
    public void getDynamoDbClient() {
        String tableName = "SHORT_URL";
        DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                .tableName(tableName).build();
        DescribeTableResponse tableResponse = dynamoDbClient.describeTable(tableRequest);
        assertEquals(tableName, tableResponse.table().tableName());
    }
}