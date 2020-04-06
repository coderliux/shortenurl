package com.aws.liujie;

import com.aws.liujie.config.AwsClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"com.aws.liujie"})
@EnableConfigurationProperties({AwsClientProperties.class})
public class ShortUrlApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ShortUrlApplication.class);
    }

    @Bean
    public DynamoDbClient getDynamoDbClient(AwsClientProperties properties) {
        AwsCredentialsProvider credentialProvider = new AwsCredentialsProvider() {
            @Override
            public AwsCredentials resolveCredentials() {
                return AwsBasicCredentials.create(properties.getAccessKeyId(), properties.getSecretKey());
            }
        };

        return DynamoDbClient.builder()
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(credentialProvider)
                .build();
    }

}