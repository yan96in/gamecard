package com.sp.platform.web.action.card;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.common.Constants;
import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.entity.UserStepLog;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.BillTempService;
import com.sp.platform.service.UserStepLogService;
import com.sp.platform.util.CacheCheckUser;
import com.sp.platform.util.LogEnum;
import com.yangl.common.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.Date;

/**
 * Created by yanglei on 15/1/18.
 */
@Namespace("/kz/card")
@Scope("prototype")
@Results({@Result(name = "main", location = "main.jsp"),
        @Result(name = "index", location = "index.jsp")})
public class KzCardAction extends ActionSupport {
    @Autowired
    private UserStepLogService userStepLogService;
    @Autowired
    private BillTempService billTempService;
    @Autowired
    private BillLogService billLogService;
    @Autowired
    private CacheCheckUser cacheCheckUser;

    private String mobile;
    private String lock;
    private String step;
    private String msgContent;
    private String carrier;
    private String longcode;
    private String linkid;
    private String fee;
    private String content;
    private String status;
    private String businessId;

    @Action("mo")
    public void mo() {
        LogEnum.DEFAULT.info("空中短信上行：" + toString());

        SmsBillLog billLog = new SmsBillLog(mobile, longcode, content, linkid, status);

        if (StringUtils.isBlank(billLog.getSpnum())) {
            saveMr(billLog);
            return;
        }

        billLog.setProvince(HaoduanCache.getProvince(mobile));
        billLog.setCity(HaoduanCache.getCity(mobile));
        billLog.setFee(Integer.parseInt(fee));
        billLog.setSfid(3);
        billLog.setCpid(2);
        billLog.setChannelid(com.sp.platform.constants.Constants.getChannelId(StringUtils.left(content, 5).toUpperCase()));
        billLog.setParentid(billLog.getCpid());

        saveBill(billLog, Integer.parseInt(fee), false);
        Struts2Utils.renderText("ok");
    }

    @Action("mt")
    public void mt() {
        LogEnum.DEFAULT.info("空中短信下行：" + toString());

        UserStepLog userStepLog = new UserStepLog();
        userStepLog.setMobile(mobile);
        userStepLog.setStatus(lock);
        userStepLog.setStep(step);
        userStepLog.setMsgContent(msgContent);
        userStepLog.setProvince(HaoduanCache.getProvince(mobile));
        userStepLog.setCity(HaoduanCache.getCity(mobile));
        userStepLog.setBusinessId(businessId);
        Date now = new Date();
        userStepLog.setBtime(now);
        userStepLog.setEtime(now);
        userStepLogService.save(userStepLog);
        Struts2Utils.renderText("ok");
    }

    @Action("mr")
    public void mr() {
        LogEnum.DEFAULT.info("空中短信状态报告：" + toString());

        SmsBillLog billLog = new SmsBillLog(mobile, longcode, content, linkid, status);
        saveMr(billLog);

        Struts2Utils.renderText("ok");
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
            String tempKey = longcode + Constants.split_str + content;

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
                String tempKey = longcode + Constants.split_str + content;

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
            String syncUrl = CpSyncCache.getSyncUrl(longcode + Constants.split_str + content);
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
                String tempKey = longcode + Constants.split_str + content;

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
        LogEnum.DEFAULT.info("保存{}话单 mobile:{}, spnum:{}, msg:{}, linkid:{}, status:{}", temp, mobile, longcode, content, linkid, status);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getLongcode() {
        return longcode;
    }

    public void setLongcode(String longcode) {
        this.longcode = longcode;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "KzCardAction{" +
                "mobile='" + mobile + '\'' +
                ", lock='" + lock + '\'' +
                ", step='" + step + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", carrier='" + carrier + '\'' +
                ", longcode='" + longcode + '\'' +
                ", linkid='" + linkid + '\'' +
                ", fee='" + fee + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", businessId='" + businessId + '\'' +
                '}';
    }
}
