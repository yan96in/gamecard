package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.UserStepLogDao;
import com.sp.platform.entity.UserStepLog;
import com.sp.platform.service.UserStepLogService;
import com.yangl.common.hibernate.PaginationSupport;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yanglei on 15/1/18.
 */
@Service
@Transactional
public class UserStepLogServiceImpl implements UserStepLogService {
    @Autowired
    private UserStepLogDao userStepLogDao;

    @Override
    public UserStepLog get(int id) {
        return userStepLogDao.get(id);
    }

    @Override
    public void delete(int id) {
        userStepLogDao.delete(id);
    }

    @Override
    public void save(UserStepLog object) {
        userStepLogDao.save(object);
    }

    @Override
    public List<UserStepLog> getAll() {
        return userStepLogDao.getAll();
    }

    @Override
    public PaginationSupport getPage(PaginationSupport page, Order[] orders, PageView pageView) {
        return null;
    }
}
