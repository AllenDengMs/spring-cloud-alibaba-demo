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
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.backend.cloud.api.client.user.UserServiceClient;
import org.backend.cloud.authentication.constains.LoginSessionRedisKey;
import org.backend.cloud.authentication.model.SystemUserDetails;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.common.web.model.Result;
import org.backend.cloud.model.user.dto.UserLoginInfo;
import org.backend.cloud.model.user.entity.SystemUser;
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

  @Autowired
  private UserServiceClient userServiceClient;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    // 保存登录信息到Redis(面向用户时，使用有状态的登录)。
    SystemUserDetails userDetails = (SystemUserDetails) authentication.getPrincipal();
    String token = userDetails.getUser().getUserId().toString(); // TODO 这里应该是随机字符串，本地测试，直接用id算了
    // 生成一份jwt，用于资源服务器的授权。
    String jwt = toJwt(userDetails);
    redisTemplate.opsForValue().set(LoginSessionRedisKey.loginInfoKey(token), jwt);

    // 虽然APPLICATION_JSON_UTF8_VALUE过时了，但也要用。因为Postman工具不声明utf-8编码就会出现乱码
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    Map<String, Object> responseData = new LinkedHashMap<>();
    responseData.put("username", userDetails.getUsername());
    responseData.put("jws", jwt);
    PrintWriter writer = response.getWriter();
    writer.print(JSON.stringify(Result.success(responseData, "登录成功")));
    writer.flush();
    writer.close();
  }

  private String toJwt(SystemUserDetails userDetails) {
    //添加构成JWT的参数
    Map<String, Object> headMap = new HashMap();
    headMap.put("alg", SignatureAlgorithm.RS256.getValue());//使用RS256签名算法
    headMap.put("typ", "JWT");

    SystemUser systemUser = userDetails.getUser();
    Set<String> authorities = userServiceClient
        .getAuthoritiesByUserId(systemUser.getUserId().toString());
    UserLoginInfo userLoginInfo = UserLoginInfo.builder()
        .userId(systemUser.getUserId())
        .nickName(systemUser.getNickName())
        .userName(systemUser.getUserName())
        .authorities(authorities)
        .build();

    Map body = JSON.parse(JSON.stringify(userLoginInfo), HashMap.class);;
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
