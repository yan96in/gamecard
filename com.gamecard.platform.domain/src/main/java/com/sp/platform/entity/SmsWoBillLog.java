package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-3
 * Time: 上午12:02
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "sms_wo_bill_log")
public class SmsWoBillLog extends BaseEntity {
    private Integer cpid;
    private String randomid;
    private String mobile;
    private String province;
    private Integer woCompanyId;

    private String outTradeNo;
    private String timeStamp;
    private String subject;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalFee;
    private BigDecimal cpamount;
    private String callbackUrl;
    private String callbackData;
    private String appKey;
    private String appName;
    private String iapId;
    private String imsi;
    private String imei;
    private String signature;

    private String resultCode;
    private String resultDescription;
    private String transactionId;
    private String sms;
    private String accessNo;
    private int syncFlg;
    private int type;
    private String status;
    private String chargeResultCode;

    public Integer getCpid() {
        return cpid;
    }

    public void setCpid(Integer cpid) {
        this.cpid = cpid;
    }

    public String getRandomid() {
        return randomid;
    }

    public void setRandomid(String randomid) {
        this.randomid = randomid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getCpamount() {
        return cpamount;
    }

    public void setCpamount(BigDecimal cpamount) {
        this.cpamount = cpamount;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIapId() {
        return iapId;
    }

    public void setIapId(String iapId) {
        this.iapId = iapId;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getAccessNo() {
        return accessNo;
    }

    public void setAccessNo(String accessNo) {
        this.accessNo = accessNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChargeResultCode() {
        return chargeResultCode;
    }

    public void setChargeResultCode(String chargeResultCode) {
        this.chargeResultCode = chargeResultCode;
    }

    public int getSyncFlg() {
        return syncFlg;
    }

    public void setSyncFlg(int syncFlg) {
        this.syncFlg = syncFlg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Integer getWoCompanyId() {
        return woCompanyId;
    }

    public void setWoCompanyId(Integer woCompanyId) {
        this.woCompanyId = woCompanyId;
    }
}
