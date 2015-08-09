package com.sp.platform.service.impl;

import com.sp.platform.common.PageView;
import com.sp.platform.dao.UserStepLogDao;
import com.sp.platform.entity.UserStepLog;
import com.sp.platform.service.UserStepLogService;
import com.sp.platform.util.StringUtil;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @Override
    public List<UserStepLog> getCardMaxStepInfo() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusMinutes(-6);
        DetachedCriteria dc = DetachedCriteria.forClass(UserStepLog.class);
        dc.add(Restrictions.gt("btime", dateTime.toDate()));
        dc.add(Restrictions.in("step", new String[]{"5", "8"}));
        return userStepLogDao.findByCriteria(dc);
    }

    @Override
    public int getChannelId(String mobile, String msgContent) {
        String sql = "select step from tbl_user_step_log where businessId='10658307' and " +
                "mobile='" + mobile + "' and msgContent='" + msgContent + "' order by id desc limit 1";
        String channelId = jdbcTemplate.queryForObject(sql, String.class);
        if (StringUtils.isNotBlank(channelId)) {
            return Integer.parseInt(channelId);
        }
        return 0;
    }
}
