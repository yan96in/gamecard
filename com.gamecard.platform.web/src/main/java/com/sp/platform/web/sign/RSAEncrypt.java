package com.sp.platform.web.sign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;

public class RSAEncrypt {

    public static final String DEFAULT_PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCurS+gyXEGyNxKcu1Ja0L6uN7/" +
                    "TBMNQw/DgicvftExXrus1zTPL5jSe/fOjAqssO52Pla0UlugIAgazYn/HLjEWvtg" +
                    "Sjsqi65R+4FbC95BROKR1qhsAo2xb25EZ/Ab94khxlYBTtAfSFcT9dIXP6rmmW2w" +
                    "SlnIOSSxLplfj7I4jQIDAQAB";

    public static final String DEFAULT_PRIVATE_KEY =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK/6WckIPN7yPIpY" +
                    "6xAPpiuh1HlEO5q1snIB4p5xIG70/w4VBb15QXD/ENMRqG0O9ta247ExskEXe3Jw" +
                    "26zbQmjo+QHObV9kXMEsTNrjV5U609XhsHyRMHeg3+uMwzV/2C0sm5IZR0gf/vdg" +
                    "zuDAb51+3lXr4Kv7eWRKsnf0XfXhAgMBAAECgYBqVSvLfV7KmDRcpD3lBo+x7acl" +
                    "X0VkxkV8/gahFloysXcsWyeMxaf1TyorX5AV1eTw+LG8b/r1ueqty+PxHsuQ95To" +
                    "33Ph28KWUAXQH86MdBvMw+SmnfR4+918udswXyKtMdpsCUzlO0bSn1OvozjYLCYd" +
                    "Rd01dy4x4Z1+RjZRAQJBAOOLroeda8pyNbR6OPZw2pYZneX32ZML0waXsHY//2Vy" +
                    "gqLex/qcuLKwnL7HixrEsFpcTUJj7lCDWdiQQ30fh/UCQQDF+9rZ0EDeduRNISNj" +
                    "jI76HF9KUnqi8h9GC3WVlQoTp3lgn/Hq/qlwib0qhW1+7avX38myUJrINZgm8N4S" +
                    "/H69AkEAh+XzrbEaIIXHh2t7u8u48O6JvEAjpMvqE8Tisi0Utp0GYomVfBq/wJD0" +
                    "fIimjq0r+juoNN+EBHf+X/YBKKh9RQJAHtESAp9YyYfmB19mG8OwKZwq9O2bqytW" +
                    "1NdJySu2stJ5oSGkTTiwdRTrfefg4EXsXqC1y2yiexFkioMpffkRXQJACRDZY2qW" +
                    "TbZvW15TjupPHFvz/DK+scq7FTGgjTW7Z9LZYI93CSUeNinlGBDQYgqV/A+fJlWo" +
                    "i6FBK79ilC7prQ==";

    /**
     * 私钥
     */
    private RSAPrivateKey privateKey;

    /**
     * 公钥
     */
    private RSAPublicKey publicKey;

    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 获取私钥
     *
     * @return 当前的私钥对象
     */
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 获取公钥
     *
     * @return 当前的公钥对象
     */
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    public void genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }


    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(String publicKeyStr) throws Exception {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (IOException e) {
            throw new Exception("公钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     *
     * @param in 私钥文件
     * @return 是否成功
     * @throws Exception
     */
    public void loadPrivateKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public void loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (IOException e) {
            throw new Exception("私钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 解密过程
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }


    /**
     * 字节数据转十六进制字符串
     *
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        RSAEncrypt rsaEncrypt = new RSAEncrypt();
        //rsaEncrypt.genKeyPair();

        //加载公钥
        try {
            rsaEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
            System.out.println("加载公钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载公钥失败");
        }

        //加载私钥
        try {
            rsaEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
            System.out.println("加载私钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载私钥失败");
        }

        //测试字符串
        String encryptStr = "Test String chaijunkun";

        try {
            //加密
            byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), encryptStr.getBytes());
            //解密
            byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), cipher);

            System.out.println("密文长度:" + cipher.length);
            System.out.println(RSAEncrypt.byteArrayToString(cipher));
            System.out.println("明文长度:" + plainText.length);
            System.out.println(RSAEncrypt.byteArrayToString(plainText));
            System.out.println(new String(plainText));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println(RSA.sign(encryptStr, "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK/6WckIPN7yPIpY6xAPpiuh1HlEO5q1snIB4p5xIG70/w4VBb15QXD/ENMRqG0O9ta247ExskEXe3Jw26zbQmjo+QHObV9kXMEsTNrjV5U609XhsHyRMHeg3+uMwzV/2C0sm5IZR0gf/vdgzuDAb51+3lXr4Kv7eWRKsnf0XfXhAgMBAAECgYBqVSvLfV7KmDRcpD3lBo+x7aclX0VkxkV8/gahFloysXcsWyeMxaf1TyorX5AV1eTw+LG8b/r1ueqty+PxHsuQ95To33Ph28KWUAXQH86MdBvMw+SmnfR4+918udswXyKtMdpsCUzlO0bSn1OvozjYLCYdRd01dy4x4Z1+RjZRAQJBAOOLroeda8pyNbR6OPZw2pYZneX32ZML0waXsHY//2VygqLex/qcuLKwnL7HixrEsFpcTUJj7lCDWdiQQ30fh/UCQQDF+9rZ0EDeduRNISNjjI76HF9KUnqi8h9GC3WVlQoTp3lgn/Hq/qlwib0qhW1+7avX38myUJrINZgm8N4S/H69AkEAh+XzrbEaIIXHh2t7u8u48O6JvEAjpMvqE8Tisi0Utp0GYomVfBq/wJD0fIimjq0r+juoNN+EBHf+X/YBKKh9RQJAHtESAp9YyYfmB19mG8OwKZwq9O2bqytW1NdJySu2stJ5oSGkTTiwdRTrfefg4EXsXqC1y2yiexFkioMpffkRXQJACRDZY2qWTbZvW15TjupPHFvz/DK+scq7FTGgjTW7Z9LZYI93CSUeNinlGBDQYgqV/A+fJlWoi6FBK79ilC7prQ==", "utf-8"));
    }
}