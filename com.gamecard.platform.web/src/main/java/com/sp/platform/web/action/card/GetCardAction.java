package com.sp.platform.web.action.card;

import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.LogEnum;
import com.yangl.common.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-9-3
 * Time: 下午11:03
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/card")
@Scope("prototype")
public class GetCardAction {
    private String Phone;
    private String UID;
    private String btime;
    private String etime;
    @Autowired
    private UserCardLogSerivce userCardLogSerivce;

    @Action("callsuccess")
    public void callsuccess() {
        LogEnum.DEFAULT.info(parameters());
        Struts2Utils.getRequest().getRemoteHost();
        if (StringUtils.isBlank(Phone) || StringUtils.isBlank(UID) || StringUtils.isBlank(btime) || StringUtils.isBlank(etime)) {
            Struts2Utils.renderText("0");
            return;
        }
        Struts2Utils.renderText(String.valueOf(userCardLogSerivce.calloutsucess(Integer.parseInt(UID))));
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    private String parameters() {
        return "Host:" + Struts2Utils.getRequest().getRemoteHost() + ", " +
                "parameters{" +
                "Phone='" + Phone + '\'' +
                ", UID='" + UID + '\'' +
                ", btime='" + btime + '\'' +
                ", etime='" + etime + '\'' +
                '}';
    }
}
