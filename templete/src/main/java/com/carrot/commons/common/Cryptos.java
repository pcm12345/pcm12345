package com.carrot.commons.common;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Cryptos {

  private final static String PBE_KEY_SALT = "carrotSecret";
  private final static String SECRET_KEY_32BIT = "carrot2020ssds_!@english20secret";
  private final static String SECRET_KEY_16BIT = SECRET_KEY_32BIT.substring(0, 16);


  /**
   * accessToken 암호화
   */
  public static String getSessionToken(String id) throws Exception {
    return encryptAES256(id);
  }

  /**
   * accessToken 복호화
   */
  public static boolean checkAccessToken(String id, String accessToken) throws Exception {
    return id.equals(decryptAES256(accessToken));
  }

  /**
   * 암호화
   */
  public static String encryptAES256(String msg) throws Exception {

    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[20];
    random.nextBytes(bytes);

    // Password-Based Key Derivation function 2
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    // 70000번 해시하여 256bit 길의 키 만들기
    PBEKeySpec spec = new PBEKeySpec(PBE_KEY_SALT.toCharArray(), bytes, 70000, 256);

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    //알고리즘/모드/패딩
    // CBC : Cipher Block Chaining Mode
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();

    // Initial Vector(1단계 암호화 블록용)
    byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();

    byte[] encryptedTextBytes = cipher.doFinal(msg.getBytes("UTF-8"));

    byte[] buffer = new byte[bytes.length + ivBytes.length + encryptedTextBytes.length];
    System.arraycopy(bytes, 0, buffer, 0, bytes.length);
    System.arraycopy(ivBytes, 0, buffer, bytes.length, ivBytes.length);
    System.arraycopy(encryptedTextBytes, 0, buffer, bytes.length + ivBytes.length,
        encryptedTextBytes.length);

    return Base64.getEncoder().encodeToString(buffer);
  }

  /**
   * AES256 복호화
   */
  public static String decryptAES256(String msg) throws Exception {

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(msg));

    byte[] saltBytes = new byte[20];
    buffer.get(saltBytes, 0, saltBytes.length);
    byte[] ivBytes = new byte[cipher.getBlockSize()];
    buffer.get(ivBytes, 0, ivBytes.length);
    byte[] encryptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];
    buffer.get(encryptedTextBytes);

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    PBEKeySpec spec = new PBEKeySpec(PBE_KEY_SALT.toCharArray(), saltBytes, 70000, 256);

    SecretKey secretKey = factory.generateSecret(spec);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

    byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
    return new String(decryptedTextBytes);

  }

  /**
   * SHA-256 암호화 캐럿글로벌 비밀번호용
   */
  public static String encryptSha256(String data) throws Exception {
    String retVal = "";
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(data.getBytes());

      byte byteData[] = md.digest();
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < byteData.length; i++) {
        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
      }

      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < byteData.length; i++) {
        String hex = Integer.toHexString(0xff & byteData[i]);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }

      retVal = hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      log.info("##### EncBySHA256 Error:" + e.toString());
    }

    return retVal;
  }

  /**
   * AES-256 secretKey 값이 같으면 같은 글자 암호화 시 계속 같은 암호화 값이 나옴
   */
  public static String aesEncode(String str)
      throws Exception {
    byte[] keyData = SECRET_KEY_32BIT.getBytes();
    SecretKey secureKey = new SecretKeySpec(keyData, "AES");
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(SECRET_KEY_16BIT.getBytes()));
    byte[] encrypted = c.doFinal(str.getBytes(StandardCharsets.UTF_8));
    return new String(Base64.getEncoder().encode(encrypted));
  }

  public static String aesDecode(String str)
      throws Exception {
    byte[] keyData = SECRET_KEY_32BIT.getBytes();
    SecretKey secureKey = new SecretKeySpec(keyData, "AES");
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(SECRET_KEY_16BIT.getBytes(
        StandardCharsets.UTF_8)));
    byte[] byteStr = Base64.getDecoder().decode(str.getBytes());
    return new String(c.doFinal(byteStr), StandardCharsets.UTF_8);
  }

}
