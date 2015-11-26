<%@ page import="com.sp.platform.util.IdUtils" %>
<%@ page import="com.sp.platform.util.LogEnum" %>
<%@ page import="com.sp.platform.web.util.Encrypt" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.client.methods.HttpGet" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.sp.platform.web.sign.RSA" %>
<%@ page import="com.sp.platform.web.sign.RSAEncrypt" %>
<%@ page import="java.net.URLEncoder" %>
<%
    String orderId = IdUtils.idGenerator("wy");
    DateTime date = new DateTime();
    String d = date.toString("yyyyMMddHHmmss");
    String site_id = "95212";
    String user_id = "61981700";
    String order_id = orderId;
    String order_time = d;
    String urs = "jayven";
    String reason = "1";
    String pts = "100";
    StringBuilder builder = new StringBuilder();
    builder.append("?site_id=").append(site_id);
    builder.append("&user_id=").append(user_id);
    builder.append("&order_id=").append(orderId);
    builder.append("&order_time=").append(d);
    builder.append("&urs=").append(urs);
    builder.append("&reason=").append(reason);
    builder.append("&pts=").append(pts);

    String key = site_id + user_id + order_id + order_time + urs + reason + pts;
    LogEnum.DEFAULT.info(key);
    RSAEncrypt rsaEncrypt = new RSAEncrypt();

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
    //加密
    byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), key.getBytes());
    String signature = RSAEncrypt.byteArrayToString(cipher);

//    String signature = RSA.sign(key, "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK/6WckIPN7yPIpY6xAPpiuh1HlEO5q1snIB4p5xIG70/w4VBb15QXD/ENMRqG0O9ta247ExskEXe3Jw26zbQmjo+QHObV9kXMEsTNrjV5U609XhsHyRMHeg3+uMwzV/2C0sm5IZR0gf/vdgzuDAb51+3lXr4Kv7eWRKsnf0XfXhAgMBAAECgYBqVSvLfV7KmDRcpD3lBo+x7aclX0VkxkV8/gahFloysXcsWyeMxaf1TyorX5AV1eTw+LG8b/r1ueqty+PxHsuQ95To33Ph28KWUAXQH86MdBvMw+SmnfR4+918udswXyKtMdpsCUzlO0bSn1OvozjYLCYdRd01dy4x4Z1+RjZRAQJBAOOLroeda8pyNbR6OPZw2pYZneX32ZML0waXsHY//2VygqLex/qcuLKwnL7HixrEsFpcTUJj7lCDWdiQQ30fh/UCQQDF+9rZ0EDeduRNISNjjI76HF9KUnqi8h9GC3WVlQoTp3lgn/Hq/qlwib0qhW1+7avX38myUJrINZgm8N4S/H69AkEAh+XzrbEaIIXHh2t7u8u48O6JvEAjpMvqE8Tisi0Utp0GYomVfBq/wJD0fIimjq0r+juoNN+EBHf+X/YBKKh9RQJAHtESAp9YyYfmB19mG8OwKZwq9O2bqytW1NdJySu2stJ5oSGkTTiwdRTrfefg4EXsXqC1y2yiexFkioMpffkRXQJACRDZY2qWTbZvW15TjupPHFvz/DK+scq7FTGgjTW7Z9LZYI93CSUeNinlGBDQYgqV/A+fJlWoi6FBK79ilC7prQ==", "utf-8");
    builder.append("&sign=").append(signature);
    LogEnum.DEFAULT.info(URLEncoder.encode(builder.toString(), "utf-8"));

//    String url = "http://esales.163.com/script/interface/dc_input";
    String url = "https://esales.dev.webapp.163.com:8043/script/interface/dc_input";

    HttpClient httpClient = com.sp.platform.util.HttpUtils.getSecuredHttpClient();
    HttpGet get = new HttpGet(url + URLEncoder.encode(builder.toString(), "utf-8"));
    HttpResponse httpResponse = httpClient.execute(get);
    String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
    LogEnum.DEFAULT.info(String.valueOf(httpResponse.getStatusLine().getStatusCode()) + " : " + body);
%>