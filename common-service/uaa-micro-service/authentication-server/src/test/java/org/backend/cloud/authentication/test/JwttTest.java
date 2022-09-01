package org.backend.cloud.authentication.test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

/**
 * 参考文档：https://github.com/jwtk/jjwt
 */
public class JwttTest {

  /**
   * <pre>
   * 生成JWT，使用HS256签名（不推荐HS256签名，不安全）
   *
   * JWT：叫Json Web Token。三部分组成：header.payload.sign。其中sign为空，不签名
   *    header 声明JWT的签名算法
   *    payload token中携带的明文数据
   *    sign 签名，一个JWT是否有效就看签名是否合法，防止伪造JWT
   *
   *    这三个部分各自base64后用点号拼接起来，
   *    第1和2部分base64解码后是明文的，因此千万不要在payload中写入重要的数据，
   *    通常用户ID也不要写入payload中，防止别人根据ID猜测用户量
   *
   * JWS：将JWT进行签名，sign那一部分有数据了
   * JWE：将JWT进行加密
   * </pre>
   */
  @Test
  public void createJwtSignWithHS256() {
    // Generating a safe HS256 Secret key
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    byte[] secretKey = key.getEncoded();
    // 将secretKey是字节码，利用base64编码可以转成字符串，方便复制数据。
    System.out.println("secretKeyString: " + Encoders.BASE64.encode(secretKey));
    // 生成带签名的JWT
    String jws = Jwts.builder()
        .setSubject("Joe")
        .signWith(key)
        .compact();
    System.out.println("jws: " + jws);
  }

  /**
   * 校验Jwt，使用HS256签名
   */
  @Test
  public void verifyJwtSignWithHS256() {
    // 因为我们生成key时将byte[]转成了String，现在要转回去
    String secretKeyString = "ad5DtBbK6b8vHXhlFVtHdjpdLHpFhGPQo91/9QHdscM=";
    byte[] secretKey = Decoders.BASE64.decode(secretKeyString);
    Key key = Keys.hmacShaKeyFor(secretKey);

    String jws = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.RYmsB69wojPxNPzXwKSLqPsOz6znX9DHV-w8m851pxs";
    String subject = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(jws)  // 会校验签名，如果前面失败，会抛异常
        .getBody().getSubject();

    System.out.println("subject: " + subject);
  }

  @Test
  public void jwtSignWithRSA256() {
    // 生成非对称的RSA秘钥对
    KeyPair keyPair = createKeyPair();

    // 利用私钥生成JWT
    String jws = Jwts.builder()
        .setSubject("Joe，John")
        .signWith(keyPair.getPrivate())
        .compact();
    System.out.println("JWS: " + jws);

    // 利用公钥解析JWT
    String subject = Jwts.parserBuilder()
        .setSigningKey(keyPair.getPublic())
        .build()
        .parseClaimsJws(jws)  // 会校验签名，如果前面失败，会抛异常
        .getBody().getSubject();

    System.out.println("subject: " + subject);
  }

  /**
   * 将公钥和私钥都写出到文件中
   *
   * @throws Exception
   */
  @Test
  public void writeKeyPairToFile() throws Exception {
    KeyPair keyPair = createKeyPair();

    // 将byte转成String，使用Base64编码一次。读取回来时，记得解码。不然会生成Key对象会报错
    String privateKeyBase64 = Encoders.BASE64.encode(keyPair.getPrivate().getEncoded());
    FileCopyUtils.copy(
        privateKeyBase64.getBytes(StandardCharsets.UTF_8),
        new FileOutputStream("d:/pri2.key")
    );

    String publicKeyBase64 = Encoders.BASE64.encode(keyPair.getPublic().getEncoded());
    FileCopyUtils.copy(
        publicKeyBase64.getBytes(StandardCharsets.UTF_8),
        new FileOutputStream("d:/pub2.key")
    );
  }

  /**
   * 读取Resource目录下的私钥文件，然后生成Jwt
   */
  @Test
  public void createJwtUsingPrivateKeyFile() {
    PrivateKey privateKey = getPrivateKeyFromResources();
    String jws = Jwts.builder()
        .setSubject("Joe，John")
        .signWith(privateKey)
        .compact();
    System.out.println("JWS: " + jws);
  }

  @Test
  public void verifyJwtUsingPublicKeyFile() {
    String jwt = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJKb2XvvIxKb2huIn0.qTYfYxFbX3MBvnetzgBEv84kW8R3oh1nMzHodEr5Hz6P-IQBzHYBXYb9lc68lxe6ITmJS6eJRpcd9Vy0z1hquAQ4gJ64OY5AVxosTY5TzXbpU9gKI8Fh8VyhP9bQbBwnojGy8yg_R8MYXLyXUGagjHsFp5E6BP3ywGxZCsaJdGHjH9g2TiegeZt2DVwfp2f80AIEad1AVRJ_FUkDX9WpN9ULUvFhelQefb8C7jA-rADSXOrSa1xRcWhgd7oSHlhtZVTfMJ_OeYSaXnaE4w-Ch-tMN3a33PCAGYOBu1wIVTMkVWoWPglOHnBtCEAQ63SkWXbpROg4fycyNGtE_MIG2w";
    PublicKey publicKey = getPublicKeyFromResources();
    // 利用公钥解析JWT
    String subject = Jwts.parserBuilder()
        .setSigningKey(publicKey)
        .build()
        .parseClaimsJws(jwt)  // 会校验签名，如果前面失败，会抛异常
        .getBody().getSubject();
    System.out.println("subject: " + subject);
  }

  /**
   * 使用JDK自带的工具类生成RSA签名的公钥 + 私钥
   */
  private KeyPair createKeyPair() {
    try {
      String salt = UUID.randomUUID().toString(); // 自定义 随机密码
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      SecureRandom secureRandom = new SecureRandom(salt.getBytes());
      keyPairGenerator.initialize(2048, secureRandom); // jjwt工具类强制要求RSA签名的长度要大于2048字节，否则认为不安全
      KeyPair keyPair = keyPairGenerator.genKeyPair();
      return keyPair;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private PublicKey getPublicKeyFromResources() {
    try {
      // 读取Resource目录下的公钥文件
      Resource data = new ClassPathResource("pub2.key");
      String publicKeyBase64 = FileCopyUtils.copyToString(new FileReader(data.getFile()));
      // 在写入文件时，我们利用base64编码将byte[]转成了字符串。现在读取回来后，需要将字符串转回到byte[]
      byte[] secretKey = Decoders.BASE64.decode(publicKeyBase64);

      // 利用JDK自带的工具生成公钥
      KeyFactory kf = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(secretKey);
      PublicKey publicKey = kf.generatePublic(keySpec);
      return publicKey;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private PrivateKey getPrivateKeyFromResources() {
    try {
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
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
