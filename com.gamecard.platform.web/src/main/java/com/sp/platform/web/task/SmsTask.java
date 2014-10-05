package com.sp.platform.web.task;

import com.sp.platform.cache.ChannelCache;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.cache.SnumCache;
import com.sp.platform.common.Constants;
import com.sp.platform.entity.ServiceNum;
import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.BillTempService;
import com.sp.platform.util.CacheCheckUser;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * User: yangl
 * Date: 13-5-26 上午1:04
 */
@Service
@Scope("prototype")
public class SmsTask implements Runnable {
    private String mobile;
    private String spnum;
    private String msg;
    private String linkid;
    private String status;

    @Autowired
    private CacheCheckUser cacheCheckUser;
    @Autowired
    private BillLogService billLogService;
    @Autowired
    private BillTempService billTempService;

    private int fee;
    private String key;
    private String key2;
    private SmsBillLog billLog;

    public SmsTask() {
    }

    public SmsTask(String mobile, String spnum, String msg, String linkid, String status) {
        this.mobile = mobile;
        this.spnum = spnum;
        this.msg = msg;
        this.linkid = linkid;
        this.status = status;
    }

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        boolean flag = true;
        try {
            stopWatch.start();
            String cacheKey = spnum + Constants.split_str + msg;

            ServiceNum snum = SnumCache.get(cacheKey);
            int fee = snum == null ? 100 : snum.getFee();

            if (fee < 0 || fee > 3000) {
                LogEnum.BILL_ERROR.error("{}, {}, {}, {}, {}", mobile, spnum, msg, linkid, status);
                return;
            }
            billLog = new SmsBillLog(mobile, spnum, msg, linkid, status);

            if (StringUtils.isBlank(billLog.getSpnum())) {
                saveMr(billLog);
                return;
            }

            billLog.setProvince(HaoduanCache.getProvince(mobile));
            billLog.setCity(HaoduanCache.getCity(mobile));
            billLog.setFee(fee);
            billLog.setSfid(SnumCache.getSpid(cacheKey));
            billLog.setCpid(CpSyncCache.getCpId(cacheKey));
            billLog.setChannelid(ChannelCache.getChannelId(cacheKey));
            int parentId = CpSyncCache.getParentId(billLog.getCpid());
            if (parentId > 0) {
                billLog.setParentid(parentId);
            } else {
                billLog.setParentid(billLog.getCpid());
            }

            saveBill(billLog, fee, false);
        } catch (Exception e) {
            flag = false;
            LogEnum.DEFAULT.error(e.toString());
            e.printStackTrace();
        } finally {
            stopWatch.stop();
            LogEnum.DEFAULT.info("接收话单[{}]，执行结果: {}, 耗时----：{}", this, flag, stopWatch.getTime());
        }
    }

    private void saveMr(SmsBillLog billLog) {
        SmsBillTemp smsBillTemp = billTempService.getByLinkid(billLog.getLinkid());
        if (smsBillTemp == null) {
            billTempService.saveMr(billLog);
        } else {
            if (smsBillTemp.getFlag() >= 3 || smsBillTemp.getFlag() == 2) {
                return;
            } else {
                smsBillTemp.setStatus(billLog.getStatus());
                smsBillTemp.setEtime(new Date());
                smsBillTemp.setFlag(3);
                billTempService.save(smsBillTemp);
            }
        }
    }

    private void saveMo(SmsBillLog billLog, SmsBillTemp smsBillTemp, boolean flag, int fee) {
        if (smsBillTemp == null) {
            // 长号码_指令
            String tempKey = spnum + Constants.split_str + msg;

            //缓存记录用户费用、长号费用
            cacheCheckUser.addCalledFee(tempKey, fee, flag);
            cacheCheckUser.addCalledProvinceFee(tempKey + Constants.split_str + billLog.getProvince(), fee, flag);
            cacheCheckUser.addCallerFee(mobile + Constants.split_str + tempKey, fee);

            billTempService.saveMo(billLog);
        } else {
            if (smsBillTemp.getFlag() >= 3 || smsBillTemp.getFlag() == 1) {
                return;
            } else {
                // 长号码_指令
                String tempKey = spnum + Constants.split_str + msg;

                //缓存记录用户费用、长号费用
                cacheCheckUser.addCalledFee(tempKey, fee, flag);
                cacheCheckUser.addCalledProvinceFee(tempKey + Constants.split_str + billLog.getProvince(), fee, flag);
                cacheCheckUser.addCallerFee(mobile + Constants.split_str + tempKey, fee);

                smsBillTemp.setMobile(billLog.getMobile());
                smsBillTemp.setType(billLog.getType());
                smsBillTemp.setSpnum(billLog.getSpnum());
                smsBillTemp.setMsg(billLog.getMsg());
                smsBillTemp.setBtime(new Date());
                smsBillTemp.setProvince(billLog.getProvince());
                smsBillTemp.setCity(billLog.getCity());
                smsBillTemp.setFee(billLog.getFee());
                smsBillTemp.setSfid(billLog.getSfid());
                smsBillTemp.setCpid(billLog.getCpid());
                smsBillTemp.setChannelid(billLog.getChannelid());
                smsBillTemp.setFlag(3);
                smsBillTemp.setSyncurl(billLog.getSyncurl());
                smsBillTemp.setParentid(billLog.getParentid());
                billTempService.save(smsBillTemp);
            }
        }
    }

    private void saveBill(SmsBillLog billLog, int fee, boolean flag) {
        SmsBillTemp smsBillTemp = billTempService.getByLinkid(billLog.getLinkid());
        if (smsBillTemp != null && smsBillTemp.getFlag() >= 3) {
            return;
        }

        String temp = flag == true ? "扣量" : "普通";

        if (flag) { // 扣量
            billLog.setType(1);
        } else {
            String syncUrl = CpSyncCache.getSyncUrl(spnum + Constants.split_str + msg);
            if (StringUtils.isNotBlank(syncUrl)) {
                billLog.setSyncurl(syncUrl);
                temp += "同步";
            }
        }
        if (StringUtils.isBlank(billLog.getStatus())) {
            saveMo(billLog, smsBillTemp, flag, fee);
        } else {
            if (smsBillTemp != null) {
                saveMr(billLog);
            } else {
                // 长号码_指令
                String tempKey = spnum + Constants.split_str + msg;

                //缓存记录用户费用、长号费用
                cacheCheckUser.addCalledFee(tempKey, fee, flag);
                cacheCheckUser.addCalledProvinceFee(tempKey + Constants.split_str + billLog.getProvince(), fee, flag);
                cacheCheckUser.addCallerFee(mobile + Constants.split_str + tempKey, fee);

                billTempService.saveBill(billLog);
            }
        }

        if (HaoduanCache.NA.equals(billLog.getProvince())) {
            billLogService.saveNaHaoduan(mobile);
            LogEnum.DEFAULT.info("保存未知号码[{}]", mobile);
        }
        LogEnum.DEFAULT.info("保存{}话单 mobile:{}, spnum:{}, msg:{}, linkid:{}, status:{}", temp, mobile, spnum, msg, linkid, status);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSpnum() {
        return spnum;
    }

    public void setSpnum(String spnum) {
        this.spnum = spnum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SmsTask{" +
                "mobile='" + mobile + '\'' +
                ", spnum='" + spnum + '\'' +
                ", msg='" + msg + '\'' +
                ", linkid='" + linkid + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
