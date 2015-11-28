package com.sp.platform.web.sign;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by yanglei on 15/11/28.
 */
public class EsalesRAS {

    public static final String pubKey = "30819f300d06092a864886f70d010101050003818d0030818902818100aead2fa0c97106c8dc4a72ed496b42fab8deff4c130d430fc382272f7ed1315ebbacd734cf2f98d27bf7ce8c0aacb0ee763e56b4525ba020081acd89ff1cb8c45afb604a3b2a8bae51fb815b0bde4144e291d6a86c028db16f6e4467f01bf78921c656014ed01f485713f5d2173faae6996db04a59c83924b12e995f8fb2388d0203010001";

    public static void main(String[] args) {
        String pKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCurS+gyXEGyNxKcu1Ja0L6uN7/TBMNQw/DgicvftExXrus1zTPL5jSe/fOjAqssO52Pla0UlugIAgazYn/HLjEWvtgSjsqi65R+4FbC95BROKR1qhsAo2xb25EZ/Ab94khxlYBTtAfSFcT9dIXP6rmmW2wSlnIOSSxLplfj7I4jQIDAQAB";
        System.out.println(bytesToHexStr(Base64.decode(pKey)));

        EsalesRAS ras = new EsalesRAS();
        String sign_org = "993991d16da5eef7576ec6052a336a0c5a886287af63800ced020f4b9faf680f07ac8fd441cfc050f78ee785b046a350bd05217dcc88da74390129a7df0cbdd0469ca97417cf172f94a54532288e7fff8a1ccebeefbc5e5d2fc477fda7de28b554b4d2fda907abcd87b624afa438933dfa52e7fe5b54e4a38bb6a40b94e2b9e6";
        String source = "ARG_ERRsign289632";
        System.out.println(ras.verifySHA1withRSASigature(sign_org, source));
    }

    /*
     * 产生签名
     */
    public String generateSHA1withRSASigature(String src, String priKey) {
        try {

            Signature sigEng = Signature.getInstance("SHA1withRSA");

            byte[] pribyte = hexStrToBytes(priKey.trim());

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);

            KeyFactory fac = KeyFactory.getInstance("RSA");
//            RSAPublicKey pubKey = (RSAPublicKey)fac.generatePublic(keySpec);
            RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
            sigEng.initSign(privateKey);
            sigEng.update(src.getBytes());

            byte[] signature = sigEng.sign();
            return bytesToHexStr(signature);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
            return null;
        }
    }

    /*
     * 验证签名
     */
    public boolean verifySHA1withRSASigature(String sign, String src) {
        try {

            Signature sigEng = Signature.getInstance("SHA1withRSA");

            byte[] pubbyte = hexStrToBytes(pubKey.trim());

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) fac.generatePublic(keySpec);


            sigEng.initVerify(publicKey);
            sigEng.update(src.getBytes());

            byte[] sign1 = hexStrToBytes(sign);
            return sigEng.verify(sign1);

        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
            return false;
        }
    }


    /*
     * 产生RSA公私钥对
     */
    public static void genRSAKeyPair() {
        KeyPairGenerator rsaKeyGen = null;
        KeyPair rsaKeyPair = null;
        try {
            System.out.println("Generating a pair of RSA key ... ");
            rsaKeyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
            random.setSeed(System.currentTimeMillis());

            //rsaKeyGen.initialize(1024, random);
            rsaKeyGen.initialize(1024);
            rsaKeyPair = rsaKeyGen.genKeyPair();
            PublicKey rsaPublic = rsaKeyPair.getPublic();
            PrivateKey rsaPrivate = rsaKeyPair.getPrivate();

            System.out.println("公钥：" + bytesToHexStr(rsaPublic.getEncoded()));
            System.out.println("私钥：" + bytesToHexStr(rsaPrivate.getEncoded()));
            System.out.println("1024-bit RSA key GENERATED.");
        } catch (Exception e) {
            System.out.println("genRSAKeyPair：" + e);
        }

    }


    /**
     * 将字节数组转换为16进制字符串的形式.
     */
    public static final String bytesToHexStr(
            byte[] bcd) {
        StringBuffer s = new StringBuffer(bcd.length * 2);

        for (int i = 0; i < bcd.length; i++) {
            s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
            s.append(bcdLookup[bcd[i] & 0x0f]);
        }

        return s.toString();
    }


    /**
     * 将16进制字符串还原为字节数组.
     */
    public static final byte[] hexStrToBytes(
            String s) {
        byte[] bytes;

        bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(
                    s.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }

    private static final char[] bcdLookup = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
}
