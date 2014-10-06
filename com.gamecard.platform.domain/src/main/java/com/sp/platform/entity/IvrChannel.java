package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-25
 * Time: 下午10:30
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "gc_ivrchannel")
public class IvrChannel extends BaseEntity{
    private Integer cardId;
    private Integer priceId;
    private Integer paytypeId;
    private String called;
    private String detail;
    private String fee;
    private String province;
    private String note;

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public Integer getPaytypeId() {
        return paytypeId;
    }

    public void setPaytypeId(Integer paytypeId) {
        this.paytypeId = paytypeId;
    }

    public String getCalled() {
        return called;
    }

    public void setCalled(String called) {
        this.called = called;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "IvrChannel{" +
                "cardId=" + cardId +
                ", priceId=" + priceId +
                ", paytypeId=" + paytypeId +
                ", called='" + called + '\'' +
                ", detail='" + detail + '\'' +
                ", fee='" + fee + '\'' +
                ", province='" + province + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
