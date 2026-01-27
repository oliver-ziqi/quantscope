package com.developer.quantscope.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.developer.quantscope.exception.BusinessException;
import com.developer.quantscope.mapper.InterfaceInfoMapper;
import com.developer.quantscope.model.network.ErrorCode;
import com.developer.quantscopecommen.entity.InterfaceInfo;
import com.developer.quantscopecommen.service.InnerInterfaceInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * Inner Interface Service Implementation Class
 *
 * @author ziqi
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }

}
