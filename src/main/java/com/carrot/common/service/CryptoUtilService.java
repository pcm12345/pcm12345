package com.carrot.common.service;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CryptoUtilService {
	
	private static String key = "carrotSecret";
	
	/**
	 * accessToken 암호화
	 * @param id
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public String getSessionToken(String id) throws Exception {
		
		/*
		String idxEnc = encryptAES256(idx+"");
		String pwdEnc = encryptAES256(pwd);
		String separator = "<U"  + idx + ">";
		*/
		String idEnc = encryptAES256(id);
		
		//return idxEnc + separator + pwdEnc;
		return idEnc;
		
	}
	
	/**
	 * accessToken 복호화
	 * @param id
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public boolean checkAccessToken(String id, String accessToken) throws Exception {
		
		boolean result = false;
		String idDec = decryptAES256(accessToken);
		
		if(idDec.equals(id)) {
			result = true;
		} else {
			result = false;
		}
		
		return result;
		
	}
	
	/**
	 * 아래 AES256 암호화는 secretKey가 같아도 같은 값을 다른값으로 암호화 해줌(salt 처리가 됨)
	 */
	/**
	 * 암호화
	 * @param msg
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptAES256(String msg) throws Exception {
		
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		byte[] saltBytes = bytes;
		
		// Password-Based Key Derivation function 2
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		// 70000번 해시하여 256bit 길의 키 만들기
		PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);
		
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		//알고리즘/모드/패딩
		// CBC : Cipher Block Chaining Mode
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE,  secret);
		AlgorithmParameters params = cipher.getParameters();
		
		// Initial Vector(1단계 암호화 블록용)
		byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
		
		byte[] encryptedTextBytes = cipher.doFinal(msg.getBytes("UTF-8"));
		
		byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
		System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
		System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
		System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);
		
		return Base64.getEncoder().encodeToString(buffer);
		
	}
	
	/**
	 * 복호화
	 * @param msg
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptAES256(String msg) throws Exception {
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(msg));
		
		byte[] saltBytes = new byte[20];
		buffer.get(saltBytes, 0, saltBytes.length);
		byte[] ivBytes = new byte[cipher.getBlockSize()];
		buffer.get(ivBytes, 0, ivBytes.length);
		byte[] encryoptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];
		buffer.get(encryoptedTextBytes);
		
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);
		
		SecretKey secretKey = factory.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
		
		byte[] decryptedTextBytes = cipher.doFinal(encryoptedTextBytes);
		return new String(decryptedTextBytes);
		
	}
	
	/**
	 * SHA-256 암호화
	 * 캐럿글로벌 비밀번호용
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptSha256(String data) throws Exception {
		  String retVal = "";
		  try {
		  	MessageDigest md = MessageDigest.getInstance("SHA-256");
		  	md.update(data.getBytes());

		  	byte byteData[] = md.digest();
		  	StringBuffer sb = new StringBuffer();
		    
		    for(int i=0; i<byteData.length; i++) {
		      sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
		    }

		    StringBuffer hexString = new StringBuffer();
		    for(int i=0; i<byteData.length;i++) {
		    String hex = Integer.toHexString(0xff & byteData[i]);
		    if(hex.length() == 1) {
		      hexString.append('0');
		    }
		      hexString.append(hex);
		    }

		  	retVal = hexString.toString();
		  } catch(NoSuchAlgorithmException e){
		  	log.info("##### EncBySHA256 Error:" + e.toString());
		  }
		  
		  return retVal;
	}
	
	/**
	 * 아래 AES 256은 secretKey값이 같으면 같은 글자 암호화 시 계속 같은 암호화 값이 나옴
	 */
	private static volatile CryptoUtilService INSTANCE;

	final static String secretKey = "carrot2020ssds_!@english20secret"; //32bit
    static String IV = ""; //16bit

    public static CryptoUtilService getInstance() {
        if (INSTANCE == null) {
            synchronized (CryptoUtilService.class) {
                if (INSTANCE == null)
                    INSTANCE = new CryptoUtilService();
            }
        }
        return INSTANCE;
    }

    private CryptoUtilService() {
        IV = secretKey.substring(0, 16);
    }

    //암호화
    public static String AES_Encode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyData = secretKey.getBytes();

        SecretKey secureKey = new SecretKeySpec(keyData, "AES");

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes()));

        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.getEncoder().encode(encrypted));

        return enStr;
    }

    //복호화
    public static String AES_Decode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyData = secretKey.getBytes();
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));

        byte[] byteStr = Base64.getDecoder().decode(str.getBytes());

        return new String(c.doFinal(byteStr), "UTF-8");
    }

}
