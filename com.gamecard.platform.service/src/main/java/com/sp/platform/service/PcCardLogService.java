package com.sp.platform.service;

import com.sp.platform.common.PageView;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.vo.PcBillVo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-25
 * Time: 下午10:32
 * To change this template use File | Settings | File Templates.
 */
public interface PcCardLogService extends AbstractService<PcCardLog> {
    public PcCardLog getPcCard(int cardId, int priceId, String phone, String code, String sid, int paytypeId, int channelType) throws Exception;

    List<PcBillVo> getBillInfo(PageView pageView);
}
