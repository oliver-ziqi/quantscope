package com.developer.quantscope.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.developer.quantscope.model.network.BaseResponse;
import com.developer.quantscope.model.vo.ApiKeyResponse;
import com.developer.quantscopecommon.entity.User;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author ziqi
 * @description Database operation Service for table 【user(User)】
 * @createDate 2026-01-23 16:45:56
 */
public interface UserService extends IService<User> {

    BaseResponse<String> register(String userAccount, String userPassword);

    BaseResponse<String> login(String userAccount, String userPassword);

    BaseResponse<ApiKeyResponse> getApiKey(String userAccount);

    User getLoginUser(ServerWebExchange exchange);
}
