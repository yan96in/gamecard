package com.sp.platform.service.impl;

import com.sp.platform.cache.CardCache;
import com.sp.platform.common.PageView;
import com.sp.platform.dao.IvrCardLogDao;
import com.sp.platform.entity.IvrCardLog;
import com.sp.platform.service.IvrCardLogService;
import com.sp.platform.vo.PcBillVo;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
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
public class IvrCardLogServiceImpl implements IvrCardLogService {
    @Autowired
    private IvrCardLogDao ivrCardLogDao;

    @Override
    public IvrCardLog get(int id) {
        return ivrCardLogDao.get(id);
    }

    @Override
    public void delete(int id) {
        ivrCardLogDao.delete(id);
    }

    @Override
    public void save(IvrCardLog object) {
        ivrCardLogDao.save(object);
    }

    @Override
    public List<IvrCardLog> getAll() {
        return ivrCardLogDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        DetachedCriteria dc = DetachedCriteria.forClass(IvrCardLog.class);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(pageView.getCaller())) {
            dc.add(Restrictions.eq("caller", pageView.getCaller()));
        }
        if (StringUtils.isNotBlank(pageView.getCalled())) {
            dc.add(Restrictions.eq("called", pageView.getCalled()));
        }

        dc.add(Restrictions.ge("ctime", DateTime.parse(pageView.getBtime(), format).toDate()));
        dc.add(Restrictions.le("ctime", DateTime.parse(pageView.getEtime(), format).toDate()));
        dc.addOrder(Order.desc("ctime"));
        return ivrCardLogDao.findPageByCriteria(dc, page, orders);
    }

    @Override
    public List<PcBillVo> getBillInfo(PageView pageView) {
        if(pageView.getCpid() == 2){
            List<PcBillVo> list = ivrCardLogDao.getCardBillInfo(pageView);
            if(list.size() > 1){
                for(int i=0; i<list.size()-1; i++){
                    PcBillVo vo = list.get(i);
                    vo.setCardid(CardCache.getCard(Integer.parseInt(vo.getCardid())).getName());
                    vo.setPriceid(CardCache.getPrice(Integer.parseInt(vo.getPriceid())).getDescription());
                }
            }
            return list;
        }
        return ivrCardLogDao.getBillInfo(pageView);
    }
}
