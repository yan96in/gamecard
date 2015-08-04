package com.sp.platform.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yanglei on 15/7/26.
 */
@Entity
@Table(name = "ivr_card_log")
public class IvrCardLog {
    private int id;
    private String caller;
    private String called;
    private int cardid;
    private int priceid;
    private String card;
    private String password;
    private String province;
    private String city;
    private Date ctime;
    private Date utime;
    private String cardShowName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }

    public int getCardid() {
        return cardid;
    }

    public void setCardid(int cardid) {
        this.cardid = cardid;
    }

    public int getPriceid() {
        return priceid;
    }

    public void setPriceid(int priceid) {
        this.priceid = priceid;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    @Transient
    public String getCardShowName() {
        return cardShowName;
    }

    public void setCardShowName(String cardShowName) {
        this.cardShowName = cardShowName;
    }
}
