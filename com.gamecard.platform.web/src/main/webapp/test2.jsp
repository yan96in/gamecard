<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.sp.platform.util.LogEnum" %><%
    LogEnum.DEFAULT.info(request.getRequestURI() + "?" + request.getQueryString());
    LogEnum.DEFAULT.info(request.getHeaderNames().toString());
    java.util.Enumeration headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
        String headerName = (String) headerNames.nextElement();
        String headerValue = request.getHeader(headerName);
        LogEnum.DEFAULT.info(headerName+":"+headerValue);
    }
    String body = org.apache.commons.io.IOUtils.toString(request.getInputStream());
    LogEnum.DEFAULT.info(body);
    LogEnum.DEFAULT.info("-----------------------------------------------");
    Map<String, String> map = new HashMap<String, String>();
    String[] strs = body.split("&");
    for(String str : strs){
        String[] temp = str.split("=");
        map.put(temp[0], temp[1]);
    }
    String UPTRANSEQ = map.get("UPTRANSEQ");
    out.println("UPTRANSEQ_"+UPTRANSEQ);
%>