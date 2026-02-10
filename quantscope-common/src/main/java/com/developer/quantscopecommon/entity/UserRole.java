package com.developer.quantscopecommon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * User-Role relationship
 */
@TableName(value = "user_role")
@Data
public class UserRole implements Serializable {
    /**
     * User ID
     */
    private Long userId;

    /**
     * Role ID
     */
    private Long roleId;

    /**
     * Tenant ID
     */
    private Long tenantId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
