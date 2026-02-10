package com.developer.quantscoperagservice.model.mq;

import java.io.Serializable;
import lombok.Data;

/**
 * MQ message for RAG job requested.
 */
@Data
public class RagJobRequestedMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jobId;
    private Long tenantId;
    private Long userId;
    private String payload;
}
