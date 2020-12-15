package com.anji.sp.util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by raodeming on 2020/7/1.
 */
public class RSAUtil {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        byte[] decodedKey = Base64.getDecoder().decode(privateKey.getBytes());
        byte[] decodedKey = Base64Util.decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        byte[] decodedKey = Base64.getDecoder().decode(publicKey.getBytes());
        byte[] decodedKey = Base64Util.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
//        return Base64.getEncoder().encodeToString(encryptedData);
        return Base64Util.encode(encryptedData);
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] dataBytes = Base64.getDecoder().decode(data);
        byte[] dataBytes = Base64Util.decode(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
//        return Base64.getEncoder().encodeToString(signature.sign());
        return Base64Util.encode(signature.sign());
    }

    /**
     * 验签
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
//        return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
        return signature.verify(Base64Util.decode(sign));
    }

    public static void main(String[] args) {
        try {
            // 生成密钥对
            KeyPair keyPair = getKeyPair();
//            String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
//            String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKey = Base64Util.encode(keyPair.getPrivate().getEncoded());
            String publicKey = Base64Util.encode(keyPair.getPublic().getEncoded());
            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);
//
//            long old = System.currentTimeMillis();
//            SpAppLogVO SpAppLogVO = new SpAppLogVO();
//            SpAppLogVO.setAppKey("64cf5a851f37c6c0ab7a3186a2377d5d");
//            SpAppLogVO.setDeviceId("BAGSDGKSJHJHJSHJSH32HJ23H24JfaGAS");
//            SpAppLogVO.setPlatform("Android");
//            System.out.println(SpAppLogVO);
//            String jsonString = JSONObject.toJSONString(SpAppLogVO);
//            System.out.println(jsonString);
//            String encryptData = encrypt(jsonString, getPublicKey(publicKey));
//            System.out.println("加密后内容:" + encryptData);
//            long now = System.currentTimeMillis();
//            System.out.println("加密后时间:" + (now-old));
//            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
//            System.out.println("解密后内容:" + decryptData);
//            long now1 = System.currentTimeMillis();
//            System.out.println("解密后时间:" + (now1-now));
//            SpAppLogVO SpAppLogVO1 = JSONObject.parseObject(decryptData, SpAppLogVO.class);
//            System.out.println(SpAppLogVO1);
//            String jsonString1 = JSONObject.toJSONString(SpAppLogVO1);
//            System.out.println(jsonString1);

//
//            JSON J = {
//                    "appKey": "64cf5a851f37c6c0ab7a3186a2377d5d",
//                    "deviceId": "BAGSDGKSJHJHJSHJSH32HJ23H24JfaGAS",
//                    "platform": "Android"
//	};


//            // RSA加密
            String data = "这是加密的json文件内容";
            String encryptData = RSAUtil.encrypt(data, getPublicKey(publicKey));
            System.out.println("加密后内容:" + encryptData);
            // RSA解密
            String decryptData = RSAUtil.decrypt(encryptData, getPrivateKey(privateKey));
            System.out.println("解密后内容:" + decryptData);

            // RSA签名
            String sign = RSAUtil.sign(data, RSAUtil.getPrivateKey(privateKey));
            System.out.println("签名：" + sign);
            // RSA验签
            boolean result = RSAUtil.verify(data, getPublicKey(publicKey), sign);
            System.out.print("验签结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("加解密异常");
        }
    }
}
