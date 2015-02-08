package com.sp.platform.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * User: yangl
 * Date: 13-5-26 下午4:32
 */
@Entity
@Table(name = "tbl_user_pc_card_log")
public class PcCardLog {
    private Integer id;
    private String mobile;
    private String province;
    private String city;
    private int channelid;
    private int cardId;
    private int priceId;
    private int fee;
    private String ext;
    private String resultcode;
    private String resultmsg;
    private String sid;
    private String cardno;
    private String cardpwd;
    /**
     * 0: 计费失败， 1：申请验证码成功， 2：下发点卡成功， 3：计费成功，取卡失败
     */
    private int status;
    private Date btime;
    private Date etime;
    private String cardShowName;

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

    public int getChannelid() {
        return channelid;
    }

    public void setChannelid(int channelid) {
        this.channelid = channelid;
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

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getResultmsg() {
        return resultmsg;
    }

    public void setResultmsg(String resultmsg) {
        this.resultmsg = resultmsg;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Transient
    public String getCardShowName() {
        return cardShowName;
    }

    public void setCardShowName(String cardShowName) {
        this.cardShowName = cardShowName;
    }

    @Override
    public String toString() {
        return "PcCardLog{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", channelid='" + channelid + '\'' +
                ", cardId=" + cardId +
                ", priceId=" + priceId +
                ", fee=" + fee +
                ", ext='" + ext + '\'' +
                ", resultcode='" + resultcode + '\'' +
                ", resultmsg='" + resultmsg + '\'' +
                ", sid='" + sid + '\'' +
                ", cardno='" + cardno + '\'' +
                ", cardpwd='" + cardpwd + '\'' +
                ", status='" + status + '\'' +
                ", btime=" + btime +
                ", etime=" + etime +
                '}';
    }
}
