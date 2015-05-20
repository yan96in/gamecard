package com.sp.platform.web.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yanglei on 15/5/20.
 */
@Component
public class StatusInterceptor extends AbstractInterceptor {
    @Autowired
    private PropertyUtils propertyUtils;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        try {
            if (StringUtils.equals("false", propertyUtils.getProperty("status"))) {
                return "close";
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
        }

        return invocation.invoke();
    }
}
