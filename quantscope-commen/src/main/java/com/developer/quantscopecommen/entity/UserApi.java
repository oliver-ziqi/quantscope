package com.developer.quantscopecommen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * User API
 */
@TableName(value = "user_api")
@Data
public class UserApi implements Serializable {
    /**
     * User ID
     */
    private Long userId;

    /**
     * API ID
     */
    private Long apiId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
