package com.sp.platform.service;

import com.sp.platform.common.PageView;
import com.sp.platform.entity.IvrCardLog;
import com.sp.platform.vo.PcBillVo;

import java.util.List;

/**
 * Created by yanglei on 15/7/26.
 */
public interface IvrCardLogService extends AbstractService<IvrCardLog>{

    List<PcBillVo> getBillInfo(PageView pageView);
}
