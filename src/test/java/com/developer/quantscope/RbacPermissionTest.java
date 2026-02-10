package com.developer.quantscope;

import com.developer.quantscope.mapper.PermissionMapper;
import com.developer.quantscopecommon.entity.Permission;
import jakarta.annotation.Resource;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * RBAC permission lookup test.
 */
@SpringBootTest
public class RbacPermissionTest {

    @Resource
    private PermissionMapper permissionMapper;

    @Test
    void getPermissionsByUserId() {
        Long userId = 1L;
        List<Permission> permissions = permissionMapper.selectPermissionsByUserId(userId);
        System.out.println("permissions for userId=" + userId + ": " + permissions);
    }
}
