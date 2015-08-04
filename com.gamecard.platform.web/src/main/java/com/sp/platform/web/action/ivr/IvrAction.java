package com.sp.platform.web.action.ivr;

import org.apache.struts2.convention.annotation.*;
import org.springframework.context.annotation.Scope;

/**
 * Created by yanglei on 15/8/3.
 */
@Namespace("/ivr")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({
        @Result(name = "list", location = "bill_list.jsp"),
        @Result(name = "province_list", location = "province_bill_list.jsp"),
        @Result(name = "userbilllist", location = "user_bill_list.jsp"),
        @Result(name = "usercardlist", location = "user_card_list.jsp")})
public class IvrAction {

}
