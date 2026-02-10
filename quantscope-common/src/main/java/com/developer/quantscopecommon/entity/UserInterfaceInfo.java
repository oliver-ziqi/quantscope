package com.developer.quantscopecommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * User Interface Invocation Relationship
 *
 * @author ziqi
 */
@TableName(value ="user_interface_info")
@Data
public class UserInterfaceInfo implements Serializable {
    /**
     * Primary key
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Invoking user ID
     */
    private Long userId;

    /**
     * Interface ID
     */
    private Long interfaceInfoId;

    /**
     * Total invocation count
     */
    private Integer totalNum;

    /**
     * Remaining invocation count
     */
    private Integer leftNum;

    /**
     * 0-normal, 1-disabled
     */
    private Integer status;

    /**
     * Creation time
     */
    private Date createTime;

    /**
     * Update time
     */
    private Date updateTime;

    /**
     * Is deleted (0-not deleted, 1-deleted)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}