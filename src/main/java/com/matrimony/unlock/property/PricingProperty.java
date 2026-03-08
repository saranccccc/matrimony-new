package com.matrimony.unlock.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Data
@Configuration
@ConfigurationProperties(prefix = "pricing")
public class PricingProperty {
   private BigDecimal subscriberContactPrice;
   private BigDecimal  nonSubscriberContactPrice;
}
