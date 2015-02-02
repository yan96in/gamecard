package com.sp.platform.service;

import com.sp.platform.entity.Paychannel;
import com.sp.platform.vo.ChannelVo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:46
 * To change this template use File | Settings | File Templates.
 */
public interface PaychannelService extends AbstractService<Paychannel> {
    public ChannelVo findChannels(int cardId, int priceId, int paytypeId, String province, String phone);
    public ChannelVo findPcChannels(int cardId, int priceId, int paytypeId, String province, String phone);
    public ChannelVo sendPcCode(int cardId, int priceId, int paytypeId, String province, String phone);
    public List<Paychannel> find(int cardId, int priceId, int paytypeId, int feetype, String province, String phone, String msg);
}
