package com.developer.quantscopegateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Rate limit settings.
 */
@ConfigurationProperties(prefix = "quantscope.gateway.rate-limit")
public class RateLimitProperties {

    /**
     * Enable rate limit.
     */
    private boolean enabled = true;

    /**
     * Permits per interval.
     */
    private long permits = 10;

    /**
     * Interval in seconds.
     */
    private long intervalSeconds = 1;

    /**
     * Rate limiter key prefix.
     */
    private String keyPrefix = "rl:ak:api";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getPermits() {
        return permits;
    }

    public void setPermits(long permits) {
        this.permits = permits;
    }

    public long getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(long intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
