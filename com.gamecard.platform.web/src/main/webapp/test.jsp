<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    System.out.println("--------------------");
    Random random = new Random(System.currentTimeMillis());
    int i = random.nextInt();
    i = Math.abs(i);
    out.print("true:13601010013:021:01:北京:cmcc:12345" + i + ":10");
%>
