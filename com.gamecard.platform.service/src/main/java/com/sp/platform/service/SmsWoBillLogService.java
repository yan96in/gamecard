package com.sp.platform.service;

import com.sp.platform.common.PageView;
import com.sp.platform.entity.SmsWoBillLog;
import com.sp.platform.vo.BillVo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-3
 * Time: 上午12:18
 * To change this template use File | Settings | File Templates.
 */
public interface SmsWoBillLogService extends AbstractService<SmsWoBillLog>{
    SmsWoBillLog getByLinkId(String transactionId);
    SmsWoBillLog getByLinkIdOrTradeNo(String transactionId);
    List<BillVo> getBillInfo(PageView pageView);

    List<BillVo> getCpBillInfo(PageView pageView);

    List<SmsWoBillLog> find(String tradeNo, String mobile);
    List<Integer> findCpIdByAppName(String appName);
}
