package com.developer.quantscope.aop;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Resolve tenant from request headers.
 */
@Component
public class TenantResolver {

    public static final String HEADER_TENANT_ID = "X-Tenant-Id";
    public static final String HEADER_TENANT = "X-Tenant";

    public Long resolveTenantId(HttpHeaders headers) {
        String tenantId = headers.getFirst(HEADER_TENANT_ID);
        if (!StringUtils.hasText(tenantId)) {
            tenantId = headers.getFirst(HEADER_TENANT);
        }
        if (!StringUtils.hasText(tenantId)) {
            return null;
        }
        try {
            return Long.parseLong(tenantId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
