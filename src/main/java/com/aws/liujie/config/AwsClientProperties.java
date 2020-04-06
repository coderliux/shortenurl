package com.aws.liujie.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "aws.config")
public class AwsClientProperties {
    private String accessKeyId;
    private String secretKey;
    private String dynamoTableName;
    private String region;
}
