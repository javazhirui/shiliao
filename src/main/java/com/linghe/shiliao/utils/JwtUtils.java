package com.linghe.shiliao.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JWT工具类
 */
@Component
public class JwtUtils {

    /**
     * 秘钥
     */
    public static final String SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    private static String jwtDeadTime;

    @Value("${jwtDeadTime}")
    public void setJwtDeadTime(String jwtDeadTime) {
        JwtUtils.jwtDeadTime = jwtDeadTime;
    }

    /**
     * 生成token字符串的方法
     *
     * @param userId
     * @return
     */
    public static String getJwtToken(String userId, String ruleId, String uuid) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("ruleId", ruleId);
        map.put("uuid", uuid);
        String JwtToken = Jwts.builder()
                //JWT头信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS2256")
                //设置分类；设置过期时间 一个当前时间，一个加上设置的过期时间常量
                .setSubject("lin-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * Integer.parseInt(jwtDeadTime))))
                //设置token主体信息，存储用户信息
                .addClaims(map)
                //.signWith(SignatureAlgorithm.ES256, SECRET)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     *
     * @Param jwtToken
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            //验证token
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     *
     * @Param request
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("token");
            if (StringUtils.isEmpty(token)) {
                return false;
            }
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取userId
     *
     * @Param request
     */
    public static String getUserIdByJwtToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (String) body.get("userId");
    }

    public static String getRuleIdByJwtToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (String) body.get("ruleId");
    }

    public static String getUuidByJwtToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (String) body.get("uuid");
    }
}
