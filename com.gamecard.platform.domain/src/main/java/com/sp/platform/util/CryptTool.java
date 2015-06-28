package com.sp.platform.util;

/**
 * CryptTool 封装了一些加密工具方法, 包括 3DES, MD5 等.
 *
 * @author hxq
 * @version 1.0 2006-01-10
 */
public class CryptTool {

    public CryptTool() {
    }

    private final static String[] hexDigits = {
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "A", "B", "C", "D", "E", "F"};

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5 摘要计算(byte[]).
     *
     * @param src byte[]
     * @return byte[] 16 bit digest
     * @throws Exception
     */
    public static byte[] md5Digest(byte[] src) throws Exception {
        java.security.MessageDigest alg = java.security.MessageDigest
                .getInstance("MD5"); // MD5 is 16 bit message digest

        return alg.digest(src);
    }

    /**
     * MD5 摘要计算(String).
     *
     * @param src String
     * @return String
     * @throws Exception
     */
    public static String md5Digest(String src) throws Exception {
        return byteArrayToHexString(md5Digest(src.getBytes()));
    }

    /**
     * Test crypt
     */
    public static void main(String[] args) {
        try {
            // 获得的明文数据
            String desStr = "MERCHANTID=123456789&ORDERSEQ=20060314000001&ORDERDATE=20060314&ORDERAMOUNT=10000";
            System.out.println("原文字符串 desStr ＝＝ " + desStr);
            //生成MAC
            String MAC = md5Digest(desStr);
            System.out.println("MAC == " + MAC);

            // 使用key值生成 SIGN
            String keyStr = "123456";// 使用固定key
            //获得的明文数据
            desStr = "UPTRANSEQ=20080101000001&MERCHANTID=0250000001&ORDERID=2006050112564931556&PAYMENT=10000&RETNCODE=00&RETNINFO=00&PAYDATE =20060101";
            //将key值和明文数据组织成一个待签名的串
            desStr = desStr + "&KEY=" + keyStr;
            System.out.println("原文字符串 desStr ＝＝ " + desStr);
            //生成 SIGN
            String SIGN = md5Digest(desStr);
            System.out.println("SIGN == " + SIGN);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}