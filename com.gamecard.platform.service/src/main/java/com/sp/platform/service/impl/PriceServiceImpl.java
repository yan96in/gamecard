package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.PaytypeDao;
import com.sp.platform.dao.PriceDao;
import com.sp.platform.entity.Paytype;
import com.sp.platform.entity.Price;
import com.sp.platform.service.PriceService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.Order;
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
public class PriceServiceImpl implements PriceService {
    @Autowired
    private PriceDao priceDao;
    @Autowired
    private PaytypeDao paytypeDao;

    @Override
    @Transactional(readOnly = true)
    public Price get(int id) {
        return priceDao.get(id);
    }

    @Transactional(readOnly = true)
    public Price getDetail(int id, int cardid) {
        Price price = null;
        try{
            price = priceDao.get(id);
            if(price != null){
                List<Paytype> paytypes = paytypeDao.getPaytypeByPriceId(cardid, price.getId());
                price.setPaytypes(paytypes);
            }
        }catch (Exception e){
            return null;
        }
        return price;
    }

    @Override
    public void delete(int id) {
        priceDao.delete(id);
    }

    @Override
    public void save(Price object) {
        priceDao.save(object);
    }

    @Override
    public List<Price> getAll() {
        return priceDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
