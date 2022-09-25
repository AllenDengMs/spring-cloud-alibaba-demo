package org.backend.cloud.authentication.utils;

import io.jsonwebtoken.io.Decoders;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

public class JwtUtil {

  private static Logger log = LoggerFactory.getLogger(JwtUtil.class);

  private static PrivateKey privateKey;

  //获取私钥
  public static PrivateKey getPriKey() {
    if (privateKey != null) {
      return privateKey;
    }
    try {
      privateKey = getPrivateKeyFromResources();
      return privateKey;
    } catch (Exception e) {
      log.error("生成Jwt失败", e);
      throw new RuntimeException("生成Jwt失败");
    }
  }

  private static PrivateKey getPrivateKeyFromResources() throws Exception {
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
