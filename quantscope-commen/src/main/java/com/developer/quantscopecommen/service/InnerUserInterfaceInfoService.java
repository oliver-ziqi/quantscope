package com.developer.quantscopecommen.service;

/**
 * Inner User Interface Info Service
 *
 * @author ziqi
 */
public interface InnerUserInterfaceInfoService {

    /**
     * Interface invocation statistics
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
