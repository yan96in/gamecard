package com.sp.platform.vo;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-12-16
 * Time: 上午12:01
 * To change this template use File | Settings | File Templates.
 */
public class PcBillVo {
    private String date;
    private String province;
    private String cardid;
    private String priceid;
    private String status;
    private String num;
    private String fee;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getPriceid() {
        return priceid;
    }

    public void setPriceid(String priceid) {
        this.priceid = priceid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
