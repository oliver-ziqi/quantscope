package com.developer.quantscopecommen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * User
 *
 * @author ziqi
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * User nickname
     */
    private String userName;

    /**
     * Account
     */
    private String userAccount;

    /**
     * User avatar
     */
    private String userAvatar;

    /**
     * Gender
     */
    private Integer gender;

    /**
     * User role: user, admin
     */
    private String userRole;

    /**
     * Password
     */
    private String userPassword;

    /**
     * Signature accessKey
     */
    private String accessKey;

    /**
     * Signature secretKey
     */
    private String secretKey;

    /**
     * Creation time
     */
    private Date createTime;

    /**
     * Update time
     */
    private Date updateTime;

    /**
     * Is deleted
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}