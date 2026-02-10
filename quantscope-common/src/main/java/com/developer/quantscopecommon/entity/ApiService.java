package com.developer.quantscopecommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * API service
 */
@TableName(value = "api_service")
@Data
public class ApiService implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * API name
     */
    private String name;

    /**
     * API code
     */
    private String code;

    /**
     * 1-open, 0-closed
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
