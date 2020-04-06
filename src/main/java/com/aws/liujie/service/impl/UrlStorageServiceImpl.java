package com.aws.liujie.service.impl;

import com.aws.liujie.config.AwsClientProperties;
import com.aws.liujie.service.UrlStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

import static com.aws.liujie.config.DynamodbTableConst.KEY_RESOURCE_ID;
import static com.aws.liujie.config.DynamodbTableConst.KEY_URL;

@Service
public class UrlStorageServiceImpl implements UrlStorageService {

    @Autowired
    DynamoDbClient client;
    @Autowired
    AwsClientProperties clientProperties;

    @Override
    public boolean storeUrl(String resourceId, String url) {
        Map<String, AttributeValue> data = new HashMap<>();
        data.put(KEY_RESOURCE_ID, AttributeValue.builder().s(resourceId).build());
        data.put(KEY_URL, AttributeValue.builder().s(url).build());
        PutItemRequest itemRequest = PutItemRequest.builder()
                .tableName(clientProperties.getDynamoTableName())
                .item(data)
                .build();
        client.putItem(itemRequest);
        return true;
    }

    @Override
    public String getUrl(String resourceId) {

        Map<String, AttributeValue> key = new HashMap<>();
        key.put(KEY_RESOURCE_ID, AttributeValue.builder().s(resourceId).build());
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(clientProperties.getDynamoTableName())
                .attributesToGet(KEY_URL, KEY_RESOURCE_ID)
                .key(key)
                .build();
        GetItemResponse getItemResponse = client.getItem(getItemRequest);
        Map<String, AttributeValue> item = getItemResponse.item();
        if (item == null || item.isEmpty()) {
            return null;
        }
        AttributeValue value = item.get(KEY_URL);
        return value.s();
    }
}
