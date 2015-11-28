package com.sp.platform.web.sign;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ExampleForRSA {
    //private static String priKey = "30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100afc2b87368ebcadf36847de9b946bdd84031c38b7212ca8d53c050ebd19f81da0bedc9706b1d3ee627f8caa8f9455453a3b94f6f9fa6825175b7cd9b3ac41dda2b4b414da3b06792e2a7f8a29cdf730c3e9de624f776c1047e6350c4ec3818904eda5676ff28d6fbad797bb8118b8b43d25dfb549d6af05fc1e6e27ad58deda102030100010281803c281a77adde9e863da3a570fcb020c4465c8200555fe721ee1b71d6f4dd72554e25e9ff673a10503838282d237c3e3b8e5b1bbc29d994db0abe8949eb183adae2ef8989924502c83e7b1987c2f0f047b0c23003a969537ccfe835a8f355cc0805e227b629340381645fe8eaabb8b55eac98b2b53251ddcdf2a8693d3a31f941024100e02afc52388603f8134501347e866541a710d320bb5e422129ae49bb27734af2d49501282c1d9c0fb6db28b10610a6a313279fc658c9caaf8973cd57bfbae689024100c8b8050ed291f2b1f99c24f7fdad2fb3f5c3d2ef7e2d016cffab9814d0f5e2a6a49f2232a89bf28f625fdbdf1ab05f4f974f1bc55b744e148e6de6ffd38388590240718e74f975b6b5e60264ef625741dfa36d6d41769054046de63e6b0d4ac3c5d0d34d9423748d9f53fa8e74c3904df90c1d14f28438d02843d9fe76a41ff51ef9024100813940405fbf08e21e52b1d65b22b1d0898c8e7413e6c3c13a056e7f105d0d93bdee99e4c57ac93a18daa042cdfaf911d3d76188b92d72cb773b387a41e1afd902403af31eb496d935419a7a19041780ad1532ae802650d498398cb37d7f726129b2347ff8a056f2b675de266e7e93b40805f41d22823cd63c2f8ae8098f97dd311e";
    //private static String pubKey = "30819f300d06092a864886f70d010101050003818d0030818902818100a9640a77a6a2966cc76817951a5313cd6663733abc7338707bc8d4d43bafe39d47eb12c24caa484c5301e6f999362a57e947c47429f2d686187730258c726bfdba1af08751e01feb308a195b68778b82cbae123299ed3ac0057176e82975ff92b5110ce0bb2242b16370d68276adcb5d5a2038588a0080af0c33e8c432f7a93f0203010001";
    private static String priKey = "30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100d073f81d7aa7938fa415726754eb8b4ef7890776e0edd7b60e527f3e459283efe929f9e0d872b7284fbfdd53d9398c045fb620b4458f0f89d5cac3158a1f2714ab33737e3a1e4f081bcb20c58e29d5f3d76ef699437cb854c1423ab17ba3cb14b9b88b0c1b4a0e9bdcdba5d42b7f91efc771d324a805b8d55214e867cd9b859f02030100010281803b622e36c7b0619b341560080beb263e92ca18b44713bab604927fe94f8c93c58ed904748774f6b4a733736be5a03d4129bb0aca105ed7005f456a1a02c5292e788076be084f42493fe542dbeb555db188a3cf7f91302f2b0476b42b4baf0f3d64dce7bbf80612742f3c3a51591f854ec47e7250c40eeed2022359d201cbaf11024100ee4ad5042ab4f5eb23e58e00eba08741e6da40233af71f3b9aa26265f45022530816febb4745a7a2cfa7d93923168c7b6b55bcf199d1faf9b00a85797594a71b024100dff17cbd26c94497e0f6ace2fd82af28c3110cbe9640361511de55b418dbff3ea27d341bf56b2dfba23788f50b864dcb52afce94608133932a9e879af4276fcd024064be185ec19b6dd8727dab52724b94c364d0e28ac27415ad424be2a3cded4f05a186ef3cc7fe07c84a5860fac9fdcf78a2414637325bd6ea56e5abd654c27aff02407edc887386f3619e405cbd57e19114b51cbc1de221afc2737129e8971bb61504607c58f6b5b2bfdbdaeb66f49a50bce53fb4446c9db6d386bd100d75e4762ac9024100c53cf02071cfa2a9f1da93848c217367f0f98749c390769c8e153e2fd46a26b11780e15ea5702c55666f30aa92339df44d76041f96d9663d7f96e1602ca23d08";
    private static String pubKey = "30819f300d06092a864886f70d010101050003818d0030818902818100d073f81d7aa7938fa415726754eb8b4ef7890776e0edd7b60e527f3e459283efe929f9e0d872b7284fbfdd53d9398c045fb620b4458f0f89d5cac3158a1f2714ab33737e3a1e4f081bcb20c58e29d5f3d76ef699437cb854c1423ab17ba3cb14b9b88b0c1b4a0e9bdcdba5d42b7f91efc771d324a805b8d55214e867cd9b859f0203010001";

    public static void main(String[] args) {
        ExampleForRSA temp = new ExampleForRSA();
        ExampleForRSA.genRSAKeyPair();
        //String sign_org = "9cf568a87a5d557d162b117cfe2d1ac826ce7c9059c2a79024115ad241a5fc26aa7c8820fefa2f9b8ffe408b2f6636bfc1ebeeb5c37aa4074e5d6b9fc090a05c15df2fbee5a17774a07554bcdd6da09fd0e2f522c43be22ca1ee725fd17e128aa8b03b749b73a8eb32e064919bdc2b8f657632674a90e758bc60e1aa9ca159f9";
        //String source = "hello, this is test! your siteid is 9163. thankyou";
        //String sign = temp.generateSHA1withRSASigature("hello, this is test! your siteid is 9163. thankyou");
        String sign_org = "6c09da319234ffd32636fa694e1f08aabf4cec6cb5d60168ae90da5e06d79abe6309b669c6f06aaba0050a0e13745c3e72c847893baf14e95a44394be6e2e8bde6750983ea229259f41560450e2076198156fc47088e4ad038abf850070369b254b74b83b338fc1103cf68f3a423ce90d4197bb1e3118c1f08cef1739666a049";
        String source = "smslove139020.0120060719http://202.108.248.35/result.jsptest00";
        String sign = temp.generateSHA1withRSASigature("smslove139020.0120060719http://202.108.248.35/result.jsptest00");

        System.out.println(sign);

        //System.out.println(new String(hexStrToBytes(sign)));

        boolean result = temp.verifySHA1withRSASigature(sign_org, source);
        System.out.println(result);


        String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCurS+gyXEGyNxKcu1Ja0L6uN7/TBMNQw/DgicvftExXrus1zTPL5jSe/fOjAqssO52Pla0UlugIAgazYn/HLjEWvtgSjsqi65R+4FbC95BROKR1qhsAo2xb25EZ/Ab94khxlYBTtAfSFcT9dIXP6rmmW2wSlnIOSSxLplfj7I4jQIDAQAB";
        System.out.println(bytesToHexStr(Base64.decode(key)));
        key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK/6WckIPN7yPIpY6xAPpiuh1HlEO5q1snIB4p5xIG70/w4VBb15QXD/ENMRqG0O9ta247ExskEXe3Jw26zbQmjo+QHObV9kXMEsTNrjV5U609XhsHyRMHeg3+uMwzV/2C0sm5IZR0gf/vdgzuDAb51+3lXr4Kv7eWRKsnf0XfXhAgMBAAECgYBqVSvLfV7KmDRcpD3lBo+x7aclX0VkxkV8/gahFloysXcsWyeMxaf1TyorX5AV1eTw+LG8b/r1ueqty+PxHsuQ95To33Ph28KWUAXQH86MdBvMw+SmnfR4+918udswXyKtMdpsCUzlO0bSn1OvozjYLCYdRd01dy4x4Z1+RjZRAQJBAOOLroeda8pyNbR6OPZw2pYZneX32ZML0waXsHY//2VygqLex/qcuLKwnL7HixrEsFpcTUJj7lCDWdiQQ30fh/UCQQDF+9rZ0EDeduRNISNjjI76HF9KUnqi8h9GC3WVlQoTp3lgn/Hq/qlwib0qhW1+7avX38myUJrINZgm8N4S/H69AkEAh+XzrbEaIIXHh2t7u8u48O6JvEAjpMvqE8Tisi0Utp0GYomVfBq/wJD0fIimjq0r+juoNN+EBHf+X/YBKKh9RQJAHtESAp9YyYfmB19mG8OwKZwq9O2bqytW1NdJySu2stJ5oSGkTTiwdRTrfefg4EXsXqC1y2yiexFkioMpffkRXQJACRDZY2qWTbZvW15TjupPHFvz/DK+scq7FTGgjTW7Z9LZYI93CSUeNinlGBDQYgqV/A+fJlWoi6FBK79ilC7prQ==";
        System.out.println(bytesToHexStr(Base64.decode(key)));
    }

    /*
     * 产生签名
     */
    public String generateSHA1withRSASigature(String src) {
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
            RSAPublicKey pubKey = (RSAPublicKey) fac.generatePublic(keySpec);


            sigEng.initVerify(pubKey);
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
