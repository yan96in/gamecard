package com.sp.platform.service;

import com.sp.platform.common.PageView;
import com.sp.platform.entity.BillLog;
import com.sp.platform.entity.BillTemp;
import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.entity.SmsBillTemp;

import java.util.List;

/**
 * User: yangl
 * Date: 13-5-26 下午8:33
 */
public interface BillTempService extends AbstractService<BillTemp>{
    public void save(BillLog billLog);

    public List<SmsBillTemp> getNeedProcessData();

    public void addSendNum(int id);

    public List<BillTemp> getByCaller(String caller);
    public List<SmsBillTemp> getSmsByCaller(String caller);

    void sync(int id);
    void syncsms(int id);

    public SmsBillTemp getByLinkid(String linkid);

    public void save(SmsBillTemp smsBillTemp);

    public void saveMr(SmsBillLog billLog);

    public void saveMo(SmsBillLog billLog);

    public void saveBill(SmsBillLog billLog);

    public List<SmsBillTemp> getCalloutData();

    public List<SmsBillTemp> getSyncSmsBill2();

    public void addSmsSendNum(int id);

    public void deleteSmsTemp(Integer id);

    public void deleteSmsTempByLinkId(String linkId);

    public List getSmsByCaller(PageView pageView);

    public int updateUserFlag(String mobile, Integer msgContent);

    int getProvinceFee(String phoneNumber);

    SmsBillTemp getByCode(String mobile, String msgcontent);
}
