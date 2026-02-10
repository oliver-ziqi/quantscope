package com.developer.quantscopecommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * RAG job task
 */
@TableName(value = "rag_job_task")
@Data
public class RagJobTask implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Job ID
     */
    private String jobId;

    /**
     * Tenant ID
     */
    private Long tenantId;

    /**
     * User ID
     */
    private Long userId;

    /**
     * Status: CREATED/QUEUED/RUNNING/READY/FAILED
     */
    private String status;

    /**
     * Request payload (json)
     */
    private String requestPayload;

    /**
     * Result json
     */
    private String resultJson;

    /**
     * Result pdf url
     */
    private String resultPdfUrl;

    /**
     * Error message
     */
    private String errorMsg;

    /**
     * Creation time
     */
    private Date createdAt;

    /**
     * Update time
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
