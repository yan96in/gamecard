package com.sp.platform.web.servlet;

import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.util.AppContextHolder;
import com.sp.platform.util.LogEnum;
import com.sp.platform.web.task.SmsTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * User: yangl
 * Date: 13-5-26 上午12:16
 */
public class HljwServlet extends HttpServlet {
    ThreadPoolTaskExecutor executor;
    Map<String, String> platformMap = new HashMap<String, String>();

    @Override
    public void init() throws ServletException {
        executor = (ThreadPoolTaskExecutor) AppContextHolder.getContext().getBean("threadPoolTaskExecutor");
        platformMap.put("SMS9500M", "DELIVRD");
        platformMap.put("SMS1518M", "DELIVRD");
        platformMap.put("MMS9500M", "Retrieved");
        platformMap.put("MMS1518M", "Retrieved");

        platformMap.put("SMS9500U", "0");
        platformMap.put("SMS9500D", "Retrieved");

        platformMap.put("SMS9500D", "DELIVRD");
        platformMap.put("SMS1518D", "DELIVRD");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String spId = StringUtils.substringAfter(req.getRequestURI(), req.getServletPath() + "/");
            spId = StringUtils.substringBefore(spId, "/");
            LogEnum.SP.info("通道[{}]同步一条话单[{}]", spId, req.getRequestURI() + "?" + req.getQueryString());

            Map<String, String> map = SpInfoCache.getSmsSyncInfo(spId);
            if (map != null) {
                SmsTask smsTask = AppContextHolder.getContext().getBean(SmsTask.class);
                smsTask.setMobile(req.getParameter(map.get("mobile")));
                smsTask.setSpnum(req.getParameter(map.get("spnum")));
                if (StringUtils.isNotBlank(req.getParameter(map.get("msg")))) {
                    smsTask.setMsg(URLDecoder.decode(req.getParameter(map.get("msg")), "GBK"));
                }

                String linkId = req.getParameter(map.get("linkid"));
                if(StringUtils.isBlank(linkId)){
                    linkId = req.getParameter("FLinkID");
                }
                smsTask.setLinkid(linkId);

                String status = req.getParameter(map.get("status"));
                String reportCode = platformMap.get(req.getParameter("PlatForm"));

                if (StringUtils.isNotBlank(status)) {
                    if (StringUtils.isNotBlank(reportCode)) {
                        if (status.equalsIgnoreCase(reportCode)) {
                            smsTask.setStatus("DELIVRD");
                        } else {
                            smsTask.setStatus("0");
                        }
                    } else {
                        smsTask.setStatus(status);
                    }
                }

                LogEnum.DEFAULT.info("准备处理SP[{}]的一条SMS数据{}", spId, smsTask);
                executor.execute(smsTask);
                resp.getWriter().print(map.get("return"));
                return;
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
        }
        resp.getWriter().print("error");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

}
