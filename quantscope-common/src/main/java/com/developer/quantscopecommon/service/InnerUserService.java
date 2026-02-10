package com.developer.quantscopecommon.service;


import com.developer.quantscopecommon.entity.User;

/**
 * Inner User Service
 *
 * @author ziqi
 */
public interface InnerUserService {

    /**
     * Check if accessKey has been assigned to the user in the database
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
