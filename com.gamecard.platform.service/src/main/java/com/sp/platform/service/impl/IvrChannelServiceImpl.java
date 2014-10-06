package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.IvrChannelDao;
import com.sp.platform.entity.IvrChannel;
import com.sp.platform.service.IvrChannelService;
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
 * Date: 14-10-6
 * Time: 上午12:05
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class IvrChannelServiceImpl implements IvrChannelService {
    @Autowired
    private IvrChannelDao dao;

    @Override
    public IvrChannel get(int id) {
        return dao.get(id);
    }

    @Override
    public void delete(int id) {
        dao.delete(id);
    }

    @Override
    public void save(IvrChannel object) {
        dao.save(object);
    }

    @Override
    public List<IvrChannel> getAll() {
        return dao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }

    @Override
    public List<IvrChannel> find(int cardId, int priceId, int paytypeId) {
        DetachedCriteria dc = DetachedCriteria.forClass(IvrChannel.class);
        dc.add(Restrictions.eq("cardId", cardId));
        dc.add(Restrictions.eq("priceId", priceId));
        dc.add(Restrictions.eq("paytypeId", paytypeId));
        return dao.findByCriteria(dc);
    }
}
