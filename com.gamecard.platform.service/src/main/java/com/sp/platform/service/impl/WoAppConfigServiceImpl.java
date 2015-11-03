package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.WoAppConfigDao;
import com.sp.platform.entity.WoAppConfig;
import com.sp.platform.service.WoAppConfigService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yanglei on 15/5/14.
 */
@Service
@Transactional
public class WoAppConfigServiceImpl implements WoAppConfigService {
    @Autowired
    private WoAppConfigDao woAppConfigDao;

    @Override
    public WoAppConfig get(int id) {
        return woAppConfigDao.get(id);
    }

    @Override
    public void delete(int id) {
        woAppConfigDao.delete(id);
    }

    @Override
    public void save(WoAppConfig object) {
        woAppConfigDao.save(object);
    }

    @Override
    public List<WoAppConfig> getAll() {
        return woAppConfigDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
