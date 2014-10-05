package com.sp.platform.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * User: yangl
 * Date: 13-5-26 下午4:32
 */
@Entity
@Table(name = "tbl_user_card_log")
public class UserCardLog {
    private Integer id;
    private String mobile;
    private String channelid;
    private String province;
    private String city;
    private int flag;
    private String smsids;
    private String callouturl;
    private String sendcardurl;
    private int sendnum;
    private String cardno;
    private String cardpwd;
    private Date btime;
    private Date etime;
    private int cardId;
    private int priceId;

    public UserCardLog() {
    }

    public UserCardLog(String mobile, String channelid, String province, String city, String smsids, int flag, String callouturl, String sendcardurl, Date btime, Date etime, int cardId, int priceId) {
        this.mobile = mobile;
        this.channelid = channelid;
        this.province = province;
        this.city = city;
        this.smsids = smsids;
        this.flag = flag;
        this.callouturl = callouturl;
        this.sendcardurl = sendcardurl;
        this.btime = btime;
        this.etime = etime;
        this.sendnum = 0;
        this.cardId = cardId;
        this.priceId = priceId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
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

    public String getSmsids() {
        return smsids;
    }

    public void setSmsids(String smsids) {
        this.smsids = smsids;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCallouturl() {
        return callouturl;
    }

    public void setCallouturl(String callouturl) {
        this.callouturl = callouturl;
    }

    public String getSendcardurl() {
        return sendcardurl;
    }

    public void setSendcardurl(String sendcardurl) {
        this.sendcardurl = sendcardurl;
    }

    public int getSendnum() {
        return sendnum;
    }

    public void setSendnum(int sendnum) {
        this.sendnum = sendnum;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getCardpwd() {
        return cardpwd;
    }

    public void setCardpwd(String cardpwd) {
        this.cardpwd = cardpwd;
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

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    @Override
    public String toString() {
        return "UserCardLog{" +
                "cardpwd='" + cardpwd + '\'' +
                ", cardno='" + cardno + '\'' +
                ", sendnum=" + sendnum +
                ", sendcardurl='" + sendcardurl + '\'' +
                ", callouturl='" + callouturl + '\'' +
                ", smsids='" + smsids + '\'' +
                ", flag=" + flag +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", channelid='" + channelid + '\'' +
                ", mobile='" + mobile + '\'' +
                ", id=" + id +
                ", cardId=" + cardId +
                ", priceId=" + priceId +
                '}';
    }
}
