package com.developer.quantscopecommen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * Permission
 */
@TableName(value = "permission")
@Data
public class Permission implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Permission code
     */
    private String code;

    /**
     * Description
     */
    private String desc;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
