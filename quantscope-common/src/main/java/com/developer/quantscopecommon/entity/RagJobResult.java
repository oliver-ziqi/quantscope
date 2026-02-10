package com.developer.quantscopecommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * RAG job result
 */
@TableName(value = "rag_job_result")
@Data
public class RagJobResult implements Serializable {
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
     * Result type: JSON/PDF
     */
    private String resultType;

    /**
     * Result reference (json or url)
     */
    private String resultRef;

    /**
     * Creation time
     */
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
