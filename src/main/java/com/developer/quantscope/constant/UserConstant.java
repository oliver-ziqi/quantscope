package com.developer.quantscope.constant;

/**
 * User Constants
 *
 * @author ziqi
 */
public interface UserConstant {

    /**
     * User login status key
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * System user ID (virtual user)
     */
    long SYSTEM_USER_ID = 0;

    //  region Permissions

    /**
     * Default permission
     */
    String DEFAULT_ROLE = "user";

    /**
     * Administrator permission
     */
    String ADMIN_ROLE = "admin";

    // endregion
}
