package com.icbc.valuation.configuration.interceptor;

import com.icbc.valuation.model.Authority;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.icbc.valuation.model.Constants.AUTHORITY;

@Component
public class RequestHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authority authority;

        if (StringUtils.isNotBlank(request.getHeader("x-Username"))) {
            String userName = request.getHeader("x-Username");
            authority = new Authority(userName);
        } else {
            String userName = request.getHeader("x-username");
            authority = new Authority(userName);
        }

        request.setAttribute(AUTHORITY, authority);
        return true;
    }
}
