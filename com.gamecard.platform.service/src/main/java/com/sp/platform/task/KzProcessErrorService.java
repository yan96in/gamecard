package com.sp.platform.task;

import com.sp.platform.constants.Constants;
import com.sp.platform.entity.UserStepLog;
import com.sp.platform.service.BillTempService;
import com.sp.platform.service.UserStepLogService;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yanglei on 15/3/21.
 */
@Service
public class KzProcessErrorService {
    @Autowired
    private BillTempService billTempService;

    @Autowired
    private UserStepLogService userStepLogService;

    public void processError(){
        List<UserStepLog> list = userStepLogService.getCardMaxStepInfo();
        for(UserStepLog userStepLog : list){
            int count = billTempService.updateUserFlag(userStepLog.getMobile(), Constants.getChannelId(StringUtils.left(userStepLog.getMsgContent(), 5).toUpperCase()));
            LogEnum.DEFAULT.warn(userStepLog.getMobile() + " 修复错误数据 " + count + " 条。");
        }
    }
}
