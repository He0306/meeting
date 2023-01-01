package com.rookie.rookiemeeting.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rookie.rookiemeeting.common.execption.ServiceException;
import com.rookie.rookiemeeting.common.lang.Constants;
import com.rookie.rookiemeeting.config.AuthAccess;
import com.rookie.rookiemeeting.entity.Employee;
import com.rookie.rookiemeeting.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    IEmployeeService employeeService;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求头里的token
        String token = request.getHeader("token");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        } else {
            HandlerMethod h = (HandlerMethod) handler;
            AuthAccess authAccess = h.getMethodAnnotation(AuthAccess.class);
            if (authAccess != null) {
                return true;
            }
        }
        //如果不是映射方法自己通过
        /*if (!(handler instanceof HandlerMethod)){
            return true;
        }*/
        if (redisTemplate.opsForValue().get("JWT_" + token) == null) {
            throw new ServiceException(Constants.CODE_401, "无TOKEN，请重新登录");
        }
        //执行认证
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(Constants.CODE_401, "无TOKEN，请重新登录");
        }
        //获取token中的userId
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401, "TOKEN验证失败,请重新登录");
        }
        //根据token中的userId查询数据库
        Employee employee = employeeService.getById(userId);
        if (employee == null) {
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
        }
        //验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(employee.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "TOKEN验证失败，请重新登录");
        }
        return true;
    }
}
