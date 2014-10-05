package com.sp.platform.vo;

import com.sp.platform.entity.Paychannel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-25
 * Time: 下午10:52
 * To change this template use File | Settings | File Templates.
 */
public class ChannelVo {
    private PhoneVo phoneVo;
    private List<Paychannel> channels1;
    private List<Paychannel> channels2;

    public PhoneVo getPhoneVo() {
        return phoneVo;
    }

    public void setPhoneVo(PhoneVo phoneVo) {
        this.phoneVo = phoneVo;
    }

    public List<Paychannel> getChannels1() {
        return channels1;
    }

    public void setChannels1(List<Paychannel> channels1) {
        this.channels1 = channels1;
    }

    public List<Paychannel> getChannels2() {
        return channels2;
    }

    public void setChannels2(List<Paychannel> channels2) {
        this.channels2 = channels2;
    }
}
