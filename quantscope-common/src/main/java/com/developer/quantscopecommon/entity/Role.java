package com.developer.quantscopecommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * Role
 */
@TableName(value = "role")
@Data
public class Role implements Serializable {
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
     * Role name
     */
    private String name;

    /**
     * Role code
     */
    private String code;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
