package com.sp.platform.web.constants;

/**
 * Created by yanglei on 15/4/24.
 */
public class LthjService {
    private String mo;
    private String serviceid;
    private String msg;
    private Integer channelid;

    public String getMo() {
        return mo;
    }

    public void setMo(String mo) {
        this.mo = mo;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getChannelid() {
        return channelid;
    }

    public void setChannelid(Integer channelid) {
        this.channelid = channelid;
    }

    @Override
    public String toString() {
        return "LthjService{" +
                "mo='" + mo + '\'' +
                ", serviceid='" + serviceid + '\'' +
                ", msg='" + msg + '\'' +
                ", channelid=" + channelid +
                '}';
    }
}
