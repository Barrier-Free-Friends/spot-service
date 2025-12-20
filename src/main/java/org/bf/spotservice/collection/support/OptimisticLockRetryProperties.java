package org.bf.spotservice.collection.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spotservice.optimistic-lock.retry")
@Getter
@Setter
public class OptimisticLockRetryProperties {

    private int maxAttempts = 3;
    private long initialDelayMs = 100;
    private double multiplier = 2.0;
    private long maxDelayMs = 1000;
}

