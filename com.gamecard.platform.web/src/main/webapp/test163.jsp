<%@ page import="com.sp.platform.util.IdUtils" %>
<%@ page import="com.sp.platform.util.LogEnum" %>
<%@ page import="com.sp.platform.web.util.Encrypt" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.client.methods.HttpGet" %>
<%@ page import="org.joda.time.DateTime" %>
<%
    StringBuilder builder = new StringBuilder();
    builder.append("?site_id=").append("95212");
    builder.append("&user_id=").append("61981700");
    String orderId = IdUtils.idGenerator("wy");
    builder.append("&order_id=").append(orderId);
    DateTime date = new DateTime();
    String d = date.toString("yyyyMMddHHmmss");
    builder.append("&order_time=").append(d);
    builder.append("&urs=").append("jayven");
    builder.append("&reason=").append("1");
    builder.append("&pts=").append("100");

    String key = "9521261981700" + orderId + d + "jayven1100";
    LogEnum.DEFAULT.info(key);
    String signature = Encrypt.encryptS1B(key);
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