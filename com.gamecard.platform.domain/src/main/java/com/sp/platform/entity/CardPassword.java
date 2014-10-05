package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "gw_card_password")
public class CardPassword extends BaseEntity {
    private int cardid;
    private int priceid;
    private String cardno;
    private String password;
    private int state;

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

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
