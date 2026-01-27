package com.developer.quantscope.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.developer.quantscope.mapper.UserMapper;
import com.developer.quantscope.model.network.BaseResponse;
import com.developer.quantscope.model.network.ResultUtils;
import com.developer.quantscope.model.vo.ApiKeyResponse;
import com.developer.quantscope.service.UserService;
import com.developer.quantscope.util.JwtService;
import com.developer.quantscopecommen.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;

/**
 * @author ziqi
 * @description Database operation Service implementation for table 【user(User)】
 * @createDate 2026-01-23 16:45:56
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtService jwtService;

    @Override
    @Transactional
    public BaseResponse<String> register(String userAccount, String userPassword) {
        //1.check whether the user account has been existed
        boolean exists = userMapper.exists(
            new LambdaQueryWrapper<User>()
                .eq(User::getUserAccount, userAccount)
        );

        if (exists) {
            throw new IllegalArgumentException("Username already exists");
        }

        //2.encrypt the password
        String encryptPassword = passwordEncoder.encode(userPassword);

        //3.save the user info
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        userMapper.insert(user);

        //4.generate jwt and return
        String token = jwtService.generateToken(user.getId(), user.getUserName());
        return ResultUtils.success(token);
    }

    @Override
    public BaseResponse<String> login(String userAccount, String userPassword) {
        boolean exists = userMapper.exists(
            new LambdaQueryWrapper<User>()
                .eq(User::getUserAccount, userAccount)
        );

        if (!exists) {
            throw new IllegalArgumentException("user not found");
        }

        //1.get user info
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount));

        //2. match the password
        boolean ok = passwordEncoder.matches(userPassword, user.getUserPassword());
        if (!ok) {
            throw new IllegalArgumentException("bad credentials");
        }

        //3. generate jwt and return
        String token = jwtService.generateToken(user.getId(), user.getUserName());
        return ResultUtils.success(token);
    }

    @Override
    public BaseResponse<ApiKeyResponse> getApiKey(String userAccount) {
        //1. find the user info
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserAccount, userAccount));

        //2. generate ak and sk
        String accessKey = passwordEncoder.encode(userAccount + RandomUtil.randomNumbers(5));
        String secretKey = passwordEncoder.encode(userAccount + RandomUtil.randomNumbers(8));

        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);

        //3.save the user
        userMapper.updateById(user);

        //4. return secret
        ApiKeyResponse apiKeyResponse = new ApiKeyResponse(accessKey, secretKey, "the secret key will be seen only once");
        log.info("apiKeyResponse has been returned{}", apiKeyResponse);
        return ResultUtils.success(apiKeyResponse);
    }

    @Override
    public User getLoginUser(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        String raw = token.substring(7);
        Optional<Long> userIdOptional = jwtService.getUserIdFromToken(raw);

        if (userIdOptional.isEmpty()) {
            return null;
        }

        Long userId = userIdOptional.get();

        return userMapper.selectById(userId);
    }
}
