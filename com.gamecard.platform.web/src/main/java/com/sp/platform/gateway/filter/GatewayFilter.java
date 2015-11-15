package com.sp.platform.gateway.filter;

import com.sp.platform.util.IdUtils;
import com.sp.platform.util.LogEnum;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * @author yang lei
 */
public class GatewayFilter implements ContainerRequestFilter,ContainerResponseFilter{

    @Context
    private HttpServletRequest httpServletRequest;
    
    private static ThreadLocal<StringBuilder> requestRawData = new ThreadLocal<StringBuilder>();
    
    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        long requestTime = (Long) httpServletRequest.getAttribute("requestTime");
        String threadId = (String) httpServletRequest.getAttribute("threadId");

        LogEnum.SP.info(threadId + " 返回：" + response.getEntity().toString() + (", 用时 {millsec=" + (System.currentTimeMillis() - requestTime) + "} ").toString());
        return response;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        try {
            String threadId = IdUtils.idGenerator("GW");
            httpServletRequest.setAttribute("requestTime", System.currentTimeMillis());
            httpServletRequest.setAttribute("threadId", threadId);
            requestRawData.set(new StringBuilder());
            String data = IOUtils.toString(request.getEntityInputStream());
            LogEnum.SP.info(threadId + " 接收一条请求, URI:" + request.getRequestUri() + "  Data: " + data);
            request.setEntityInputStream(IOUtils.toInputStream(data));
        } catch (Exception e) {
            System.out.println(e);
        }
        return request;
    }
}
