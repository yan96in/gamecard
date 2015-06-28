package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.BlackCodeDao;
import com.sp.platform.entity.BlackCode;
import com.sp.platform.service.BlackCodeService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yanglei on 15/6/28.
 */
@Service
@Transactional
public class BlackCodeServiceImpl implements BlackCodeService {
    @Autowired
    private BlackCodeDao blackCodeDao;

    @Override
    public BlackCode get(int id) {
        return blackCodeDao.get(id);
    }

    @Override
    public void delete(int id) {
        blackCodeDao.delete(id);
    }

    @Override
    public void save(BlackCode object) {
        blackCodeDao.save(object);
    }

    @Override
    public List<BlackCode> getAll() {
        return blackCodeDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
