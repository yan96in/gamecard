package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.SmsWoBillLogDao;
import com.sp.platform.entity.SmsWoBillLog;
import com.sp.platform.service.SmsWoBillLogService;
import com.sp.platform.vo.BillVo;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-3
 * Time: 上午12:18
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class SmsWoBillLogServiceImpl implements SmsWoBillLogService {
    @Autowired
    private SmsWoBillLogDao smsWoBillLogDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public SmsWoBillLog get(int id) {
        return smsWoBillLogDao.get(id);
    }

    public SmsWoBillLog getByLinkId(String linkId) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsWoBillLog.class);
        dc.add(Restrictions.eq("transactionId", linkId));
        List<SmsWoBillLog> list = smsWoBillLogDao.findByCriteria(dc);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public SmsWoBillLog getByLinkIdOrTradeNo(String linkId) {
        DetachedCriteria dc = DetachedCriteria.forClass(SmsWoBillLog.class);
        dc.add(Restrictions.or(Restrictions.eq("transactionId", linkId), Restrictions.eq("outTradeNo", linkId)));
        List<SmsWoBillLog> list = smsWoBillLogDao.findByCriteria(dc);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillVo> getBillInfo(PageView pageView) {
        return smsWoBillLogDao.getBillInfo(pageView);
    }

    @Override
    public List<BillVo> getCpBillInfo(PageView pageView) {
        return smsWoBillLogDao.getCpBillInfo(pageView);
    }

    @Override
    public List<SmsWoBillLog> find(String tradeNo, String mobile) {
        if (StringUtils.isBlank(tradeNo) && StringUtils.isBlank(mobile)) {
            return null;
        }

        DetachedCriteria dc = DetachedCriteria.forClass(SmsWoBillLog.class);
        if (StringUtils.isNotBlank(tradeNo)) {
            dc.add(Restrictions.eq("outTradeNo", tradeNo));
        }
        if (StringUtils.isNotBlank(mobile)) {
            dc.add(Restrictions.eq("mobile", mobile));
        }
        return smsWoBillLogDao.findByCriteria(dc);
    }

    @Override
    public List<Integer> findCpIdByAppName(String appName) {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(-7);
        Object[] o = {dateTime.toDate(), appName};
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "select distinct cpid from sms_wo_bill_log where ctime>=? and appname=?", o);
        List<Integer> result = new ArrayList<Integer>();
        for (Map<String, Object> map : list) {
            result.add((Integer) map.get("cpid"));
        }
        return result;
    }

    @Override
    public void delete(int id) {
        smsWoBillLogDao.delete(id);
    }

    @Override
    public void save(SmsWoBillLog object) {
        smsWoBillLogDao.save(object);
    }

    @Override
    public List<SmsWoBillLog> getAll() {
        return smsWoBillLogDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
