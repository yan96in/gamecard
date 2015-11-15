package com.sp.platform.vo;

import lombok.ToString;

@ToString
public class LtPcResult {
    ChannelVo chanels;
    String resultCode;
    String resultMessage;
    String sid;
    boolean flag;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public ChannelVo getChanels() {
        return chanels;
    }

    public void setChanels(ChannelVo chanels) {
        this.chanels = chanels;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}