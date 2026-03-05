package com.matrimony.common.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    private S3 s3;
    private Dynamodb dynamodb;

    @Getter
    @Setter
    public static class S3 {
        private String region;
        private String bucketName;
        private String prefix;
    }

    @Getter
    @Setter
    public static class Dynamodb {
        private String region;
        private String tableName;
        private String endpoint;
    }
}