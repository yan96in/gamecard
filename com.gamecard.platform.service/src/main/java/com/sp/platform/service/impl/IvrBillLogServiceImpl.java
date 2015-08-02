package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.IvrBillLogDao;
import com.sp.platform.entity.IvrBillLog;
import com.sp.platform.service.IvrBillLogService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yanglei on 15/7/26.
 */
@Service
@Transactional
public class IvrBillLogServiceImpl implements IvrBillLogService {
    @Autowired
    private IvrBillLogDao ivrBillLogDao;

    @Override
    public IvrBillLog get(int id) {
        return ivrBillLogDao.get(id);
    }

    @Override
    public void delete(int id) {
        ivrBillLogDao.delete(id);
    }

    @Override
    public void save(IvrBillLog object) {
        ivrBillLogDao.save(object);
    }

    @Override
    public List<IvrBillLog> getAll() {
        return ivrBillLogDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(IvrBillLog.class);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        dc.add(Restrictions.ge("ctime", DateTime.parse(pageView.getBtime(), format).toDate()));
        dc.add(Restrictions.le("ctime", DateTime.parse(pageView.getEtime(), format).toDate()));
        dc.addOrder(Order.desc("ctime"));
        return ivrBillLogDao.findPageByCriteria(dc, page, orders);
    }
}
