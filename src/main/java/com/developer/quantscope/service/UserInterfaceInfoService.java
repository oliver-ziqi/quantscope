package com.developer.quantscope.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.developer.quantscopecommen.entity.InterfaceInfo;
import com.developer.quantscopecommen.entity.UserInterfaceInfo;
import java.util.List;

/**
* @author ziqi
* @description Database operation Service for table 【user_interface_info(User Interface Invocation Relationship)】
* @createDate 2026-01-23 16:45:56
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    boolean invokeCount(long interfaceInfoId, long userId);

    List<InterfaceInfo> listUserInterfaceInfo(long userId);
}
