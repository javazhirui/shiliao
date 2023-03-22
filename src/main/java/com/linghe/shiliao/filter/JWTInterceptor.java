package com.linghe.shiliao.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * jwt拦截器
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {

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
                return true; // 放行; 去访问接口吧
            } catch (ExpiredJwtException e) {
                // 登录过期
                String errorMessage = "登录过期, 请重新登录";
                doResponse(response, errorMessage);
            } catch (UnsupportedJwtException e) {
                String errorMessage = "Token不合法, 请自重";
                doResponse(response, errorMessage);
            } catch (Exception e) {
                String errorMessage = "token有误,请重新登陆";
                doResponse(response, errorMessage);
            }
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
