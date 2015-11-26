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
<%@ page import="com.sp.platform.web.sign.Base64" %>
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
    //私钥加密过程
    byte[] cipherData = RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(""), key.getBytes());
    String signature = Base64.encode(cipherData);

    builder.append("&sign=").append(signature);
    LogEnum.DEFAULT.info(builder.toString());

//    String url = "http://esales.163.com/script/interface/dc_input";
    String url = "https://esales.dev.webapp.163.com:8043/script/interface/dc_input";

    HttpClient httpClient = com.sp.platform.util.HttpUtils.getSecuredHttpClient();
    HttpGet get = new HttpGet(url + builder.toString());
    HttpResponse httpResponse = httpClient.execute(get);
    String body = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
    LogEnum.DEFAULT.info(String.valueOf(httpResponse.getStatusLine().getStatusCode()) + " : " + body);
%>