package com.developer.quantscopecommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * Strategy
 */
@TableName(value = "strategy")
@Data
public class Strategy implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Tenant ID
     */
    private Long tenantId;

    /**
     * Strategy name
     */
    private String name;

    /**
     * 1-online, 0-offline
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
