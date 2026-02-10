package com.developer.quantscoperagservice.model.dto;

import java.util.Map;
import lombok.Data;

/**
 * RAG report request.
 */
@Data
public class RagReportRequest {
    /**
     * Query or prompt.
     */
    private String prompt;

    /**
     * Report date, e.g. 2026-02-10.
     */
    private String date;

    /**
     * Output format: JSON/PDF.
     */
    private String format;

    /**
     * Extra params.
     */
    private Map<String, Object> params;
}
