package com.developer.quantscoperagservice.constant;

/**
 * RAG job status constants.
 */
public final class RagJobStatus {

    private RagJobStatus() {
    }

    public static final String CREATED = "CREATED";
    public static final String QUEUED = "QUEUED";
    public static final String RUNNING = "RUNNING";
    public static final String READY = "READY";
    public static final String FAILED = "FAILED";
}
