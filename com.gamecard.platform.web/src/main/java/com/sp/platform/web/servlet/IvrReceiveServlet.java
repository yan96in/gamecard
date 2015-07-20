package com.sp.platform.web.servlet;

import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.service.CtccIvrService;
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
    CtccIvrService ctccIvrService;

    @Override
    public void init() throws ServletException {
        executor = (ThreadPoolTaskExecutor) AppContextHolder.getContext().getBean("threadPoolTaskExecutor");
        ctccIvrService = AppContextHolder.getContext().getBean("ctccIvrService", CtccIvrService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            LogEnum.SP.info("电信IVR网关收到一条消息[{}]", req.getRequestURI() + "?" + req.getQueryString());
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
        }
        String operateId = req.getParameter("operateid");
        String caller = req.getParameter("caller");
        String called = req.getParameter("called");
        String body = req.getParameter("body");

        String result = null;
        if (StringUtils.equals(operateId, "00101")) {
            result = ctccIvrService.checkUser(caller, called, body);
        } else if (StringUtils.equals(operateId, "00002")) {
            result = ctccIvrService.getCardConfig(caller, called, body);
        } else if (StringUtils.equals(operateId, "00003")) {
            result = ctccIvrService.getCard(caller, called, body);
        } else if (StringUtils.equals(operateId, "00102")) {
            result = ctccIvrService.saveKeyLog(caller, called, body);
        } else if (StringUtils.equals(operateId, "00103")) {
            result = ctccIvrService.saveBillLog(caller, called, body);
        } else {
            result = "0";
        }

        LogEnum.SP.info(caller + "返回消息[{}]", result);
        resp.getWriter().print(result);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
