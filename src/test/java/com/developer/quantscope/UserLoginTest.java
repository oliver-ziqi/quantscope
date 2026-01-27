package com.developer.quantscope;

import com.developer.quantscope.model.network.BaseResponse;
import com.developer.quantscope.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ziqi
 */

@SpringBootTest
public class UserLoginTest {

    @Resource
    UserService userService;

    String userName = "test";
    String userPassword = "123456";

    @Test
    void userRegister() {
        BaseResponse<String> register = userService.register(userName, userPassword);
        System.out.println(register);
    }

    @Test
    void userLogin() {
        BaseResponse<String> login = userService.login(userName, userPassword);
        System.out.println(login);
    }
}
