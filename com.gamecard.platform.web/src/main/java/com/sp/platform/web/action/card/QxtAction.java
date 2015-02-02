package com.sp.platform.web.action.card;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.yangl.common.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * Created by yanglei on 15/1/25.
 */
@Namespace("/qxt")
@Scope("prototype")
@Results({@Result(name = "main", location = "main.jsp"),
        @Result(name = "index", location = "index.jsp")})
public class QxtAction extends ActionSupport {
    @Autowired
    private UserCardLogSerivce userCardLogSerivce;
    @Autowired
    private PropertyUtils propertyUtils;

    private String Phone;
    private String msgContent;
    private String spNumber;

    @Action("mo")
    public void mo() {
        LogEnum.DEFAULT.info(parameters());
        Struts2Utils.getRequest().getRemoteHost();
        if (StringUtils.isBlank(Phone) || StringUtils.isBlank(msgContent) || StringUtils.isBlank(spNumber)) {
            Struts2Utils.renderText("0");
            return;
        }

        if(StringUtils.equals(propertyUtils.getProperty("qxt.card.mo.msg"), msgContent)) {
            Struts2Utils.renderText(String.valueOf(userCardLogSerivce.qxtSmsSuccess(Phone, spNumber)));
        } else {
            Struts2Utils.renderText("ok");
        }
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getSpNumber() {
        return spNumber;
    }

    public void setSpNumber(String spNumber) {
        this.spNumber = spNumber;
    }

    private String parameters() {
        return "Host:" + Struts2Utils.getRequest().getRemoteHost() + ", " +
                "parameters{" +
                "Phone='" + Phone + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", spNumber='" + spNumber + '\'' +
                '}';
    }
}
