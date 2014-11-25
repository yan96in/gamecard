package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.PcCardLogDao;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.service.PcCardLogService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-11-25
 * Time: 下午10:33
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class PcCardLogServiceImpl implements PcCardLogService {
    @Autowired
    private PcCardLogDao pcCardLogDao;
    @Override
    public PcCardLog get(int id) {
        return pcCardLogDao.get(id);
    }

    @Override
    public void delete(int id) {
        pcCardLogDao.delete(id);
    }

    @Override
    public void save(PcCardLog object) {
        pcCardLogDao.save(object);
    }

    @Override
    public List<PcCardLog> getAll() {
        return pcCardLogDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PcCardLog getPcCard(int cardId, int priceId, String phone, String code, String sid) {
        return null;
    }
}
