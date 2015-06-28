<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %><%
    System.out.println(request.getRequestURI() + "?" + request.getQueryString());
    System.out.println(request.getHeaderNames());
    java.util.Enumeration headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
        String headerName = (String) headerNames.nextElement();
        String headerValue = request.getHeader(headerName);
        System.out.println(headerName+":"+headerValue);
    }
    String body = org.apache.commons.io.IOUtils.toString(request.getInputStream());
    System.out.println(body);
    System.out.println("-----------------------------------------------");
    Map<String, String> map = new HashMap<String, String>();
    String[] strs = body.split("&");
    for(String str : strs){
        String[] temp = str.split("=");
        map.put(temp[0], temp[1]);
    }
    String UPTRANSEQ = map.get("UPTRANSEQ");
    out.println("UPTRANSEQ_"+UPTRANSEQ);
%>