package com.developer.quantscopecommen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * Tenant Strategy
 */
@TableName(value = "tenant_strategy")
@Data
public class TenantStrategy implements Serializable {
    /**
     * Tenant ID
     */
    private Long tenantId;

    /**
     * Strategy ID
     */
    private Long strategyId;

    /**
     * 1-enabled, 0-disabled
     */
    private Integer enabled;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
