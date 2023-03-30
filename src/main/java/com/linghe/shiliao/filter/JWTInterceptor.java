package com.linghe.shiliao.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linghe.shiliao.common.CustomException;
import com.linghe.shiliao.utils.JwtUtils;
import com.linghe.shiliao.utils.RedisCache;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * jwt拦截器
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisCache redisCache;

    @Value("${jwtDeadTime}")
    private String jwtDeadTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        if (token == null) {
            String errorMessage = "请先登录";
            // 没有传 token; 提示请先登录
            doResponse(response, errorMessage);
        } else {
            try {
                // 获取 解析器parser; 验证token
                JwtParser parser = Jwts.parser();
                parser.setSigningKey("ukc8BDbRigUDaY6pZFfWus2jZWLPHO"); // 解析token 的 SigningKey 必须和生成的token时设置的密码一致
                // 如果 token正确(密码正确, 有效期内) 则正常执行, 否则抛出 异常
                Jws<Claims> claimsJws = parser.parseClaimsJws(token);
                String signature = claimsJws.getSignature();
                String userId = JwtUtils.getUserIdByJwtToken(request);
                String ruleId = JwtUtils.getRuleIdByJwtToken(request);
                String uuid = JwtUtils.getUuidByJwtToken(request);
                String loginUuid = redisCache.getCacheObject("login_" + userId);
                if (null != loginUuid && !StringUtils.equals(loginUuid, uuid)) {
                    String errorMessage = "该账号可能已退出或已在其它设备登录,请重新登陆";
                    doResponse(response, errorMessage);
                    return false;
                }
                Object tokenRedis = redisCache.getCacheObject("token_" + userId);
                if (ObjectUtils.isEmpty(tokenRedis)) {
                    String newToken = JwtUtils.getJwtToken(userId, ruleId, uuid);
                    int i = Integer.parseInt(jwtDeadTime);
                    redisCache.setCacheObject("token_" + userId, newToken, i / 2, TimeUnit.SECONDS);
                    redisCache.setCacheObject("login_" + userId, uuid, i, TimeUnit.SECONDS);
                    response.setHeader("token", newToken);
                    response.setHeader("flush", "token刷新");
                    return true;
                }
                response.setHeader("token",token);
                return true; // 放行; 去访问接口吧
            } catch (ExpiredJwtException e) {
                // 登录过期
                String errorMessage = "登录过期, 请重新登录";
                doResponse(response, errorMessage);
            } catch (UnsupportedJwtException e) {
                String errorMessage = "Token不合法, 请自重";
                doResponse(response, errorMessage);
            } /*catch (Exception e) {
                String errorMessage = "token有误,请重新登陆";
                doResponse(response, errorMessage);
            }*/
        }
        return false;
    }

    private void doResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String s = new ObjectMapper().writeValueAsString(errorMessage);
        out.print(s);
        out.flush();
        out.close();
    }
}
