package org.backend.cloud.authentication.handler;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.backend.cloud.authentication.model.SystemUserDetails;
import org.backend.cloud.common.web.model.Result;
import org.backend.cloud.common.web.utils.JSON;
import org.backend.cloud.model.user.dto.UsernameAndPasswordDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

/**
 * 登录成功，事件处理器
 */
@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    // 保存登录信息到Redis
    SystemUserDetails userDetails = (SystemUserDetails) authentication.getPrincipal();
    String token = userDetails.getUser().getUserId().toString();
    String key = new StringBuffer("user").append(":").append(token).toString();
    String jwt = toJwt(userDetails);
    redisTemplate.opsForValue().set(key, jwt);

    // 虽然APPLICATION_JSON_UTF8_VALUE过时了，但也要用。因为Postman工具不声明utf-8编码就会出现乱码
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    Map<String, Object> responseData = new LinkedHashMap<>();
    responseData.put("username", userDetails.getUsername());
    responseData.put("token", jwt);
    PrintWriter writer = response.getWriter();
    writer.print(JSON.stringify(Result.ok(responseData, "登录成功")));
    writer.flush();
    writer.close();
  }

  private String toJwt(SystemUserDetails userDetails) {
    //添加构成JWT的参数
    Map<String, Object> headMap = new HashMap();
    headMap.put("alg", SignatureAlgorithm.RS256.getValue());//使用RS256签名算法
    headMap.put("typ", "JWT");

    Map body = new HashMap();
    UsernameAndPasswordDto user = userDetails.getUser();
    body.put("userId", user.getUserId().toString());
    body.put("username", user.getUsername());
    if (user.getUserId() == 1L) {
      body.put("authorities", "admin");
    } else if (user.getUserId() == 2L) {
      body.put("authorities", "admin2");
    }

    String jwt = Jwts.builder()
        .setHeader(headMap)
        .setClaims(body)
        .signWith(getPriKey())
        .compact();

    return jwt;
  }

  private PrivateKey privateKey;

  //获取私钥
  public PrivateKey getPriKey() {
    if (this.privateKey != null) {
      return this.privateKey;
    }
    try {
      this.privateKey = getPrivateKeyFromResources();
      return this.privateKey;
    } catch (Exception e) {
      log.error("生成Jwt失败", e);
      throw new RuntimeException("生成Jwt失败");
    }
  }

  private PrivateKey getPrivateKeyFromResources() throws Exception {
    // 读取Resource目录下的私钥文件
    Resource data = new ClassPathResource("pri2.key");
    String publicKeyBase64 = FileCopyUtils.copyToString(new FileReader(data.getFile()));
    // 在写入文件时，我们利用base64编码将byte[]转成了字符串。现在读取回来后，需要将字符串转回到byte[]
    byte[] secretKey = Decoders.BASE64.decode(publicKeyBase64);

    // 利用JDK自带的工具生成私钥
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(secretKey);
    PrivateKey privateKey = kf.generatePrivate(keySpec);
    return privateKey;
  }
}
