package com.matrimony.photo.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "photo")
public class PhotoProperties {

    private int maxCount = 3;
    private Long maxFileSize = 1000000L; // in bytes

}
