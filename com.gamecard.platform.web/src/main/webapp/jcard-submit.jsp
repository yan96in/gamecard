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
    String timeStamp = new DateTime().toString("yyyyMMddHHmmss");
    String bill_id = timeStamp + "001";
    String temp = "agent_id=" + agentId +
            "&bill_id=" + bill_id +
            "&bill_time=" + timeStamp +
            "&user_account=" + account +
            "&charge_amt=0.1" +
            "&time_stamp=" + timeStamp;


    String sign = Encrypt.md532(temp + "|||yy5690002015").toLowerCase();

    temp = temp +
            "&phone=" + account +
            "&client_ip=" + "127.0.0.1" +
            "&sign=" + sign;

    String url = "http://Service.800j.com/Personal/Submit.aspx?" + temp;

    HttpGet get = new HttpGet(url);

    HttpClient client = new DefaultHttpClient();
    HttpResponse httpResponse = client.execute(get);
    System.out.println("帐号充值: " + url);
    String body = IOUtils.toString(httpResponse.getEntity().getContent(), "GBK");
    System.out.println(httpResponse.getStatusLine().getStatusCode() + " : " + body);
    out.print(body);
%>
