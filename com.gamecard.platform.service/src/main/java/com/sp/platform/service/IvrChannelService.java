package com.sp.platform.service;

import com.sp.platform.entity.IvrChannel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-10-6
 * Time: 上午12:04
 * To change this template use File | Settings | File Templates.
 */
public interface IvrChannelService extends AbstractService<IvrChannel> {
    public List<IvrChannel> find(int cardId, int priceId, int paytypeId);
}
