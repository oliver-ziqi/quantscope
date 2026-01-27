package com.developer.quantscope.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ziqi
 */

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String userPassword;
}
