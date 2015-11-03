package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yanglei on 15/5/14.
 */
@Entity
@Table(name = "tbl_wo_app_config")
public class WoAppConfig extends BaseEntity {
    private String appName;
    private String appKey;
    private String appSecret;
    private String appToken;
    private int status;  // 1: valid  0: inValid
    private int woApiType;  // 2, 小额计费 3, 计费2.0
    private int woCompanyId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWoApiType() {
        return woApiType;
    }

    public void setWoApiType(int woApiType) {
        this.woApiType = woApiType;
    }

    public int getWoCompanyId() {
        return woCompanyId;
    }

    public void setWoCompanyId(int woCompanyId) {
        this.woCompanyId = woCompanyId;
    }
}
