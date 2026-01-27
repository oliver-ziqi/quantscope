package com.developer.quantscope.controller;

import com.developer.quantscope.model.network.BaseResponse;
import com.developer.quantscope.model.network.ResultUtils;
import com.developer.quantscope.service.UserInterfaceInfoService;
import com.developer.quantscope.service.UserService;
import com.developer.quantscopecommen.entity.InterfaceInfo;
import com.developer.quantscopecommen.entity.User;
import com.developer.quantscopecommen.service.InnerUserService;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author ziqi
 */

@RestController
@RequestMapping("/api/interface")
public class InterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private InnerUserService innerUserService;

    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(ServerWebExchange exchange) {
        User invokeUser = innerUserService.getInvokeUser(exchange.getRequest().getHeaders().getFirst("accessKey"));
        List<InterfaceInfo> interfaceInfoList = userInterfaceInfoService.listUserInterfaceInfo(invokeUser.getId());
        return ResultUtils.success(interfaceInfoList);
    }
}
