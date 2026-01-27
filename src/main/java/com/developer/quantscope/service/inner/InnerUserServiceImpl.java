package com.developer.quantscope.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.developer.quantscope.exception.BusinessException;
import com.developer.quantscope.mapper.UserMapper;
import com.developer.quantscope.model.network.ErrorCode;
import com.developer.quantscopecommen.entity.User;
import com.developer.quantscopecommen.service.InnerUserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * Inner User Service Implementation Class
 *
 * @author ziqi
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
