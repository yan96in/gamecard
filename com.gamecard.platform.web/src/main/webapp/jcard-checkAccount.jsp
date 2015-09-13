<%@ page language="java" import="com.sp.platform.util.Encrypt" pageEncoding="utf-8" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.client.methods.HttpGet" %>
<%@ page import="org.apache.http.impl.client.DefaultHttpClient" %>
<%@ page import="org.joda.time.DateTime" %>
<%
    String agentId = "2000106";
    String account = "13552922122";
    DateTime dateTime = new DateTime();
    String timeStamp = dateTime.toString("yyyyMMddHHmmss");
    String temp = "agent_id=2000106&user_account=13552922122&time_stamp=" + timeStamp + "|||yy5690002015";

    String sign = Encrypt.md532(temp).toLowerCase();
    String url = "http://Service.800j.com/Personal/CheckAccount.aspx?" +
            "agent_id=" + agentId +
            "&user_account=" + account +
            "&time_stamp=" + timeStamp +
            "&sign=" + sign;


    HttpGet get = new HttpGet(url);

    HttpClient client = new DefaultHttpClient();
    HttpResponse httpResponse = client.execute(get);
    System.out.println("检查帐号: " + url);
    String body = IOUtils.toString(httpResponse.getEntity().getContent(), "GBK");
    System.out.println(httpResponse.getStatusLine().getStatusCode() + " : " + body);
    out.print(body);
%>
