package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-25
 * Time: 下午10:30
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "gc_paychannel")
public class Paychannel extends BaseEntity{
    private Integer cardId;
    private Integer priceId;
    private Integer paytypeId;
    private String msg;
    private String spnum;
    private int fee;
    private int feetype;
    private int feecount;
    private String province;
    private String callouturl;
    private String sendcardurl;
    private String description;
    private String note1;
    private String note2;
    private int errorFlg;
    private String errorMessage;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSpnum() {
        return spnum;
    }

    public void setSpnum(String spnum) {
        this.spnum = spnum;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getFeetype() {
        return feetype;
    }

    public void setFeetype(int feetype) {
        this.feetype = feetype;
    }

    public int getFeecount() {
        return feecount;
    }

    public void setFeecount(int feecount) {
        this.feecount = feecount;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public String getNote2() {
        return note2;
    }

    public void setNote2(String note2) {
        this.note2 = note2;
    }


    @Transient
    public int getErrorFlg() {
        return errorFlg;
    }

    public void setErrorFlg(int errorFlg) {
        this.errorFlg = errorFlg;
    }

    @Transient
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
