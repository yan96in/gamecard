package com.sp.platform.web.jcaptcha;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageCheckServlet extends HttpServlet {

    private static final long serialVersionUID = -5382766069139170499L;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    protected void doGet(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) throws ServletException,
            IOException {
        try {
            String code = httpServletRequest.getParameter("checkCode");
            code = new String(code.getBytes("ISO-8859-1"), "GB2312");
            System.out.println(code);
            if (CaptchaServiceSingleton.getInstance().validateCaptchaResponse(code, httpServletRequest.getSession()))
                httpServletResponse.getWriter().write("true");
            else {
                httpServletResponse.getWriter().write("false");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
    }

    protected void doPost(HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) throws ServletException,
            IOException {
        doGet(httpServletRequest, httpServletResponse);
    }
}  
