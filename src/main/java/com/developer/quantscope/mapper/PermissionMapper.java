package com.developer.quantscope.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.developer.quantscopecommen.entity.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * Database operation Mapper for table permission
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> selectPermissionsByUserId(@Param("userId") Long userId);

    List<Permission> selectPermissionsByUserIdAndTenantId(@Param("userId") Long userId,
                                                          @Param("tenantId") Long tenantId);
}
