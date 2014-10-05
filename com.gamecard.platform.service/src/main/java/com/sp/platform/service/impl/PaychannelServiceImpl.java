package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.PaychannelDao;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.service.PaychannelService;
import com.sp.platform.vo.ChannelVo;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-25
 * Time: 下午10:36
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class PaychannelServiceImpl implements PaychannelService {
    @Autowired
    private PaychannelDao paychannelDao;

    @Override
    public Paychannel get(int id) {
        return paychannelDao.get(id);
    }

    @Override
    public void delete(int id) {
        paychannelDao.delete(id);
    }

    @Override
    public void save(Paychannel object) {
        paychannelDao.save(object);
    }

    @Override
    public List<Paychannel> getAll() {
        return paychannelDao.getAll();
    }

    public ChannelVo findChannels(int cardId, int priceId, int paytypeId, String province){
        ChannelVo chanels = new ChannelVo();
        chanels.setChannels1(find(cardId, priceId, paytypeId, 1, province));
        chanels.setChannels2(find(cardId, priceId, paytypeId, 2, province));
        return chanels;
    }

    public List<Paychannel> find(int cardId, int priceId, int paytypeId, int feetype, String province){
        DetachedCriteria dc = DetachedCriteria.forClass(Paychannel.class);
        dc.add(Restrictions.eq("cardId", cardId));
        dc.add(Restrictions.eq("priceId", priceId));
        dc.add(Restrictions.eq("paytypeId", paytypeId));
        dc.add(Restrictions.eq("feetype", feetype));
        dc.add(Restrictions.or(Restrictions.like("province", province, MatchMode.ANYWHERE), Restrictions.eq("province", "ALL")));
        return paychannelDao.findByCriteria(dc);
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
