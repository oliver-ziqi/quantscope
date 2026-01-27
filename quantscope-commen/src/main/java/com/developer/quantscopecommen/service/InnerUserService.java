package com.developer.quantscopecommen.service;


import com.developer.quantscopecommen.entity.User;

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
