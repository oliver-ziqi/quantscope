package com.developer.quantscopecommen.service;


import com.developer.quantscopecommen.entity.InterfaceInfo;

/**
 * Inner Interface Info Service
 *
 * @author ziqi
 */
public interface InnerInterfaceInfoService {

    /**
     * Query whether the simulated interface exists in the database (request path, request method, request parameters)
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
