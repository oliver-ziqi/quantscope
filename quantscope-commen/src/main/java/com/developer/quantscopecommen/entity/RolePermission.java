package com.developer.quantscopecommen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * Role-Permission relationship
 */
@TableName(value = "role_permission")
@Data
public class RolePermission implements Serializable {
    /**
     * Role ID
     */
    private Long roleId;

    /**
     * Permission ID
     */
    private Long permissionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
