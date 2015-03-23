package com.sp.platform.service;

import com.sp.platform.entity.UserStepLog;

import java.util.List;

/**
 * Created by yanglei on 15/1/18.
 */
public interface UserStepLogService extends AbstractService<UserStepLog> {
    List<UserStepLog> getCardMaxStepInfo();
}
