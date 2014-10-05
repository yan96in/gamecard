package com.sp.platform.vo;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-24
 * Time: 下午11:18
 * To change this template use File | Settings | File Templates.
 */
public class PhoneVo {
    private String number;
    private String province;
    private String city;

    public PhoneVo() {
    }

    public PhoneVo(String number, String province, String city) {
        this.number = number;
        this.province = province;
        this.city = city;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
}
