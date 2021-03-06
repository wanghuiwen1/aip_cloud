package com.wanghuiwen.core.conifg.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;

public class AuthMetadataSource implements FilterInvocationSecurityMetadataSource {
    private WhiteList whiteList ;

    public AuthMetadataSource(WhiteList whiteList) {
        this.whiteList = whiteList;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法。
     * object-->FilterInvocation
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;

        if (whiteList!=null && isMatcherAllowedRequest(filterInvocation)) return null; //return null 表示允许访问，不做拦截

        return org.springframework.security.access.SecurityConfig.createList(filterInvocation.getRequestUrl());
    }


    /**
     * 判断当前请求是否在允许请求的范围内
     *
     * @param fi 当前请求
     * @return 是否在范围中
     */
    private boolean isMatcherAllowedRequest(FilterInvocation fi) {
        return whiteList.getWhiteList().stream().map(AntPathRequestMatcher::new)
                .filter(requestMatcher -> requestMatcher.matches(fi.getHttpRequest()))
                .toArray().length > 0;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
