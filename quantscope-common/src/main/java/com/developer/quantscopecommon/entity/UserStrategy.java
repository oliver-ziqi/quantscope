package com.developer.quantscopecommon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * User Strategy
 */
@TableName(value = "user_strategy")
@Data
public class UserStrategy implements Serializable {
    /**
     * User ID
     */
    private Long userId;

    /**
     * Strategy ID
     */
    private Long strategyId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
