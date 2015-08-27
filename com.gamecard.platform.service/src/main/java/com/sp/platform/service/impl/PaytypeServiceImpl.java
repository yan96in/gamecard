package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.PaytypeDao;
import com.sp.platform.dao.PriceDao;
import com.sp.platform.entity.Paytype;
import com.sp.platform.service.PaytypeService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:47
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class PaytypeServiceImpl implements PaytypeService {
    @Autowired
    private PaytypeDao paytypeDao;

    @Override
    public Paytype get(int id) {
        return paytypeDao.get(id);
    }

    @Override
    public void delete(int id) {
        paytypeDao.delete(id);
    }

    @Override
    public void save(Paytype object) {
        paytypeDao.save(object);
    }

    @Override
    public List<Paytype> getAll() {
        return paytypeDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }

    @Override
    public List findByOi(Integer paytypeId) {
        DetachedCriteria dc = DetachedCriteria.forClass(Paytype.class);
        dc.add(Restrictions.eq("oi", paytypeId));
        return paytypeDao.findByCriteria(dc);
    }
}
