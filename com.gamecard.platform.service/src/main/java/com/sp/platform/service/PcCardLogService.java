package com.sp.platform.service;

import com.sp.platform.entity.PcCardLog;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-25
 * Time: 下午10:32
 * To change this template use File | Settings | File Templates.
 */
public interface PcCardLogService extends AbstractService<PcCardLog> {
    public PcCardLog getPcCard(int cardId, int priceId, String phone, String code, String sid) throws Exception;
}
