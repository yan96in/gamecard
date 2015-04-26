package com.sp.platform.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * User: yangl
 * Date: 13-5-26 下午4:32
 */
@Entity
@Table(name = "sms_bill_temp")
public class SmsBillTemp {
    private Integer id;
    private String mobile;
    private String spnum;
    private String msg;
    private String paymentcode;
    private String linkid;
    private String status;
    private Date btime;
    private Date etime;
    private String province;
    private String city;
    private int fee = 0;
    private int sfid = 0;
    private int cpid = 0;
    private int channelid = 0;
    /**
     * 扣量标志 0：不扣量 1：扣量
     */
    private int type;
    private int sendnum;
    private String syncurl;
    private int parentid;
    /**
     * 1:上行 2:状态报告 3:计费成功 4:预约外呼 5:外呼中 6:外呼成功 7:下发成功
     */
    private int flag;

    public SmsBillTemp() {
    }

    public SmsBillTemp(String mobile, String spnum, String msg, String linkid, String status) {
        this.mobile = mobile;
        this.spnum = spnum;
        this.msg = msg;
        this.linkid = linkid;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public int getSendnum() {
        return sendnum;
    }

    public void setSendnum(int sendnum) {
        this.sendnum = sendnum;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSpnum() {
        return spnum;
    }

    public void setSpnum(String spnum) {
        this.spnum = spnum;
    }

    public Date getBtime() {
        return btime;
    }

    public void setBtime(Date btime) {
        this.btime = btime;
    }

    public Date getEtime() {
        return etime;
    }

    public void setEtime(Date etime) {
        this.etime = etime;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getSfid() {
        return sfid;
    }

    public void setSfid(int sfid) {
        this.sfid = sfid;
    }

    public int getCpid() {
        return cpid;
    }

    public void setCpid(int cpid) {
        this.cpid = cpid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getChannelid() {
        return channelid;
    }

    public void setChannelid(int channelid) {
        this.channelid = channelid;
    }

    public String getPaymentcode() {
        return paymentcode;
    }

    public void setPaymentcode(String paymentcode) {
        this.paymentcode = paymentcode;
    }

    @Override
    public String toString() {
        return "SmsBillTemp{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", spnum='" + spnum + '\'' +
                ", msg='" + msg + '\'' +
                ", paymentcode='" + paymentcode + '\'' +
                ", linkid='" + linkid + '\'' +
                ", status='" + status + '\'' +
                ", btime=" + btime +
                ", etime=" + etime +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", fee=" + fee +
                ", sfid=" + sfid +
                ", cpid=" + cpid +
                ", channelid=" + channelid +
                ", type=" + type +
                ", sendnum=" + sendnum +
                ", syncurl='" + syncurl + '\'' +
                ", parentid=" + parentid +
                '}';
    }
}
