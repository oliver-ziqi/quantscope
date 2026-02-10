package com.developer.quantscoperagservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RAG MQ properties.
 */
@ConfigurationProperties(prefix = "rag.mq")
public class RagMqProperties {
    /**
     * Exchange name.
     */
    private String exchange = "rag.job.exchange";

    /**
     * Routing key.
     */
    private String routingKey = "rag.job.requested";

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
