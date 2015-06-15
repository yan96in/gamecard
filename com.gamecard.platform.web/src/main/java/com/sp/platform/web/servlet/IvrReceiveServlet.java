package com.sp.platform.web.servlet;

import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.util.AppContextHolder;
import com.sp.platform.util.LogEnum;
import com.sp.platform.web.task.IvrTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * User: yangl
 * Date: 13-5-26 上午12:16
 */
public class IvrReceiveServlet extends HttpServlet {
    ThreadPoolTaskExecutor executor;

    @Override
    public void init() throws ServletException {
        executor = (ThreadPoolTaskExecutor) AppContextHolder.getContext().getBean("threadPoolTaskExecutor");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            LogEnum.SP.info("电信IVR网关收到一条消息[{}]", req.getRequestURI() + "?" + req.getQueryString());
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
        }
        String operateid = req.getParameter("operateid");
        if(StringUtils.equals(operateid, "00101")){
            resp.getWriter().print("0");
        } else if(StringUtils.equals(operateid, "00002")){
            resp.getWriter().print("3,3,2,2,1;10,5,3&10,5,3&10,5&10,5&6;10,5,3&10,5,3&10,5&10,5&6");
        } else if(StringUtils.equals(operateid, "00003")){
            resp.getWriter().print("1&123&123");
        } else if(StringUtils.equals(operateid, "00102")){
            resp.getWriter().print("0");
        } else if(StringUtils.equals(operateid, "00103")){
            resp.getWriter().print("0");
        } else {
            resp.getWriter().print("0");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
