package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.PaytypeDao;
import com.sp.platform.dao.PriceDao;
import com.sp.platform.entity.Paytype;
import com.sp.platform.entity.Price;
import com.sp.platform.service.PriceService;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    @Transactional(readOnly = true)
    public List<Price> getByCardId(int cardid){
        return priceDao.getPriceByCardId(cardid);
    }

    @Transactional(readOnly = true)
    public Price getDetail(int id, int cardid) {
        Price price = null;
        try{
            price = priceDao.get(id);
            if(price != null){
                List<Paytype> temp = paytypeDao.getPaytypeByPriceId(cardid, price.getId());
                List<Paytype> paytypes = new ArrayList<Paytype>();
                Map<String, String> map = new HashMap<String, String>();
                for(Paytype paytype : temp){
                    if(StringUtils.isNotBlank(map.get(paytype.getOp()))){
                        continue;
                    }
                    map.put(paytype.getOp(), paytype.getOp());
                    paytypes.add(paytype);
                }
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
