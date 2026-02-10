package com.developer.quantscopecommon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * Tenant API
 */
@TableName(value = "tenant_api")
@Data
public class TenantApi implements Serializable {
    /**
     * Tenant ID
     */
    private Long tenantId;

    /**
     * API ID
     */
    private Long apiId;

    /**
     * 1-enabled, 0-disabled
     */
    private Integer enabled;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
