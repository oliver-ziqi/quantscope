package com.developer.quantscope.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.developer.quantscope.exception.BusinessException;
import com.developer.quantscope.mapper.InterfaceInfoMapper;
import com.developer.quantscope.model.network.ErrorCode;
import com.developer.quantscope.service.UserInterfaceInfoService;
import com.developer.quantscope.mapper.UserInterfaceInfoMapper;
import com.developer.quantscopecommon.entity.InterfaceInfo;
import com.developer.quantscopecommon.entity.UserInterfaceInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author ziqi
* @description Database operation Service implementation for table 【user_interface_info(User Interface Invocation Relationship)】
* @createDate 2026-01-23 16:45:56
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {

        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);

        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return this.update(updateWrapper);
    }

    @Override
    public List<InterfaceInfo> listUserInterfaceInfo(long userId) {
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<UserInterfaceInfo> userInterfaceInfoList = this.list(queryWrapper);

        List<Long> interfaceInfoIds = userInterfaceInfoList.stream()
                .map(UserInterfaceInfo::getInterfaceInfoId)
                .collect(Collectors.toList());

        if (interfaceInfoIds.isEmpty()) {
            return List.of();
        }

        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        interfaceInfoQueryWrapper.in("id", interfaceInfoIds);
        return interfaceInfoMapper.selectList(interfaceInfoQueryWrapper);
    }
}
