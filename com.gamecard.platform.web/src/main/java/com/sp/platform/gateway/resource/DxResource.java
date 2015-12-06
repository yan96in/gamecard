package com.sp.platform.gateway.resource;

import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.common.Constants;
import com.sp.platform.entity.CpNum;
import com.sp.platform.entity.PcCardLog;
import com.sp.platform.entity.SmsWoBillLog;
import com.sp.platform.entity.User;
import com.sp.platform.gateway.constant.Status;
import com.sp.platform.gateway.exception.BusinessException;
import com.sp.platform.gateway.response.wo.PaymentChargeResponse;
import com.sp.platform.service.SmsWoBillLogService;
import com.sp.platform.service.sp.DxService;
import com.sp.platform.util.*;
import com.sp.platform.vo.LtPcResult;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanglei on 15/11/15.
 */
@Controller
@Path("/dx")
public class DxResource extends BaseResource {
    @Autowired
    private PropertyUtils propertyUtils;
    @Autowired
    private DxService spService;
    @Autowired
    private SmsWoBillLogService billLogService;
    @Autowired
    private CacheCheckUser cacheCheckUser;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GET
    @Path("getpaymentcode")
    public Response getPaymentCode(
            @QueryParam("cid") String cid,
            @QueryParam("phoneNum") String phoneNum,
            @QueryParam("price") String price,
            @QueryParam("ext") String ext) {
        boolean logFlag = true;
        Map<String, Object> map = new HashMap<String, Object>();
        LtPcResult result = null;
        BigDecimal fee = null;
        try {
            CpNum cpNum = validateParameter(cid, phoneNum, price);

            //测试手机转低资费
            price = getTestUserPrice(phoneNum, price);

            User user = CpSyncCache.getCp(Integer.parseInt(cid));

            fee = new BigDecimal(price).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN);

            result = spService.sendYdCode(phoneNum, fee.intValue(), "");  // 联通WO+

            if (result != null && result.isFlag()) {
                LogEnum.DEFAULT.info(cid + user.getShowname() + " 申请联通WO+下发验证码 成功: " + result.toString());
                return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("linkid=" + result.getSid()).build();
            } else {
                LogEnum.DEFAULT.info(cid + user.getShowname() + " 申请联通WO+下发验证码 失败: " + result.toString());
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("error").build();
            }
        } catch (BusinessException e) {
            logFlag = false;
            LogEnum.DEFAULT.error(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("error").build();
        } catch (Exception e) {
            logFlag = false;
            LogEnum.DEFAULT.error(e.toString());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("error").build();
        } finally {
            if (logFlag) {
                saveLog(cid, phoneNum, fee, map, result, ext);
            }
        }
    }

    @GET
    @Path("paymentcharge")
    public Response paymentCharge(
            @QueryParam("linkid") String linkid,
            @QueryParam("paymentCode") String paymentCode) {
        PaymentChargeResponse response = null;
        SmsWoBillLog woBillLog = null;
        try {
            if (StringUtils.isBlank(linkid) || StringUtils.isBlank(paymentCode)) {
                throw new BusinessException(Status.WO_ERROR_1010);
            }
            if (StringUtils.equals("var2", paymentCode)) {
                throw new BusinessException(Status.WO_ERROR_604);
            }

            woBillLog = billLogService.getByLinkIdOrTradeNo(linkid);
            if (woBillLog == null) {
                throw new BusinessException(Status.WO_ERROR_621);
            }

            //如果状态已经提交，直接返回原来结果
            if (StringUtils.isNotBlank(woBillLog.getStatus()) && StringUtils.equals("4", woBillLog.getStatus())) {
                return getReturnResponse(woBillLog);
            }

            woBillLog.setDescription(paymentCode);
            PcCardLog pcCardLog = new PcCardLog();
            pcCardLog.setFee(woBillLog.getTotalFee().intValue());
            pcCardLog.setCardId(1);
            if (600 == pcCardLog.getFee()) {
                pcCardLog.setPriceId(19);
            } else if (1000 == pcCardLog.getFee()) {
                pcCardLog.setPriceId(21);
            } else if (2000 == pcCardLog.getFee()) {
                pcCardLog.setPriceId(16);
            }

            String body = spService.commitPaymentCode(woBillLog.getMobile(), paymentCode, linkid, 21, pcCardLog);

            boolean isOK = (propertyUtils.getProperty("pc.dx.success.result", "00").equals(body));

            if (isOK) {
                woBillLog.setStatus("4");
                woBillLog.setCpamount(woBillLog.getTotalFee());
                LogEnum.DEFAULT.info(linkid + " 电信计费成功: " + body);

                cacheCheckUser.addCallerFee(woBillLog.getMobile() + Constants.split_str + "pc6", woBillLog.getTotalFee().intValue());
                cacheCheckUser.addCalledProvinceFee(woBillLog.getProvince() + Constants.split_str + "pc216", woBillLog.getTotalFee().intValue(), false);
                cacheCheckUser.addCalledFee("pc216", woBillLog.getTotalFee().intValue(), false);
            } else {
                woBillLog.setStatus("8");
                woBillLog.setCpamount(new BigDecimal(0));
                LogEnum.DEFAULT.info(linkid + "电信计费失败: " + body);
            }

            woBillLog.setChargeResultCode(body);
            woBillLog.setResultDescription(String.valueOf(isOK));
            woBillLog.setUtime(new Date());
            syncToCP(woBillLog);

            return getReturnResponse(woBillLog);
        } catch (BusinessException e) {
            LogEnum.DEFAULT.error(e.toString());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("status=616").build();
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("status=616").build();
        }
    }

    protected Response getReturnResponse(SmsWoBillLog woBillLog) {
        String returnBody = "status=";
        if (woBillLog.getType() == 1) {
            returnBody = returnBody + "8";
        } else {
            returnBody = returnBody + ("4".equals(woBillLog.getStatus()) ? 0 : woBillLog.getChargeResultCode());
        }
        if (StringUtils.isNotBlank(woBillLog.getCallbackData())) {
            returnBody = returnBody + "@ext=" + woBillLog.getCallbackData();
        }
        return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(returnBody).build();
    }

    protected CpNum validateParameter(String cid, String phoneNum, String price) throws Exception {
        if (StringUtils.isBlank(cid) || StringUtils.isBlank(phoneNum) || StringUtils.isBlank(price)) {
            throw new Exception("参数异常");
        }

        CpNum cpNum = CpSyncCache.getCpNum(Integer.parseInt(cid));
        if (cpNum == null) {
            throw new Exception("渠道" + cid + "不存在");
        }

        if (StringUtils.indexOf(propertyUtils.getProperty("black.cp"), cid + ",") >= 0) {
            throw new Exception("渠道黑名单" + cid);
        }

        if (StringUtils.indexOf(propertyUtils.getProperty("dx.price.list"), price + ",") < 0) {
            throw new Exception("超出价格范围:" + price);
        }


        if (Integer.parseInt(price) > 2000) {
            throw new Exception("超出价格范围:" + price);
        }
        String cpType = "djf";
        /****************** 凌晨判断 ******************/
        if (propertyUtils.getProperty("black.hour.ignore.cpId").indexOf(cid + ",") < 0) {
            DateTime dateTime = new DateTime();
            int hour = dateTime.getHourOfDay();
            if (hour >= propertyUtils.getInteger("black.min.hour")
                    && hour <= propertyUtils.getInteger("black.max.hour")) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append(cid + " 屏蔽 [")
                        .append(propertyUtils.getProperty("black.min.hour"))
                        .append("-")
                        .append(propertyUtils.getProperty("black.max.hour"))
                        .append("] 时间段的代计费合作业务");
                LogEnum.DEFAULT.error(errorMessage.toString());
                throw new Exception("超出时间限制");
            }
            cpType = "zxf";
        }

        boolean isValid = callerLimit(phoneNum,
                propertyUtils.getInteger("user.max.day.fee." + cpType),
                propertyUtils.getInteger("user.max.week.fee." + cpType),
                propertyUtils.getInteger("user.max.month.fee." + cpType),
                "电信");

        String province = HaoduanCache.getProvince(phoneNum);
        if (isValid) {
            int provinceMaxFee = propertyUtils.getInteger("pc.dx.province.day.limit.21." + province);
            if (provinceMaxFee == 0) {
                provinceMaxFee = propertyUtils.getInteger("pc.dx.province.day.limit.21");
            }
            isValid = provinceLimit(phoneNum, province, 21, provinceMaxFee, "电信");
        }

        if (isValid) {
            isValid = checkYdTotalLimit();
        }

        if (!isValid) {
            throw new Exception("超出限制");
        }
        return cpNum;
    }


    private boolean callerLimit(String phoneNumber, int maxDayFee, int maxWeekFee, int maxMonthFee, String channelType) {
        //----------------------------- 用户日上限 -----------------------
        int type = getPcSpType(channelType);

        int tempFee = cacheCheckUser.getCallerDayFee(phoneNumber + Constants.split_str + "pc" + type);

        if (maxDayFee > 0 && tempFee >= maxDayFee) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 超用户日上限 ").append(maxDayFee)
                    .append(",日费用：")
                    .append(tempFee).toString());
            return false;
        }

        //----------------------------- 用户月上限 -----------------------
        tempFee = cacheCheckUser.getCallerMonthFee(phoneNumber + Constants.split_str + "pc" + type);
        //如果没有设置上限， 或者费用未达到上限，继续
        if (maxMonthFee > 0 && tempFee >= maxMonthFee) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 超用户月上限 ").append(maxMonthFee)
                    .append(",月费用：")
                    .append(tempFee).toString());
            return false;
        }

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(-7);
        String sql = "select sum(totalFee)/100 from sms_wo_bill_log where status=4 and ctime>='"
                + dateTime.toString("yyyy-MM-dd") + "' and mobile='" + phoneNumber + "'";

        Integer fee = jdbcTemplate.queryForObject(sql, Integer.class);
        if (fee != null && maxWeekFee > 0 && fee >= maxWeekFee) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 超用户周上限 ").append(maxWeekFee)
                    .append(",周费用：")
                    .append(fee).toString());
            return false;
        }

        dateTime = new DateTime();
        dateTime = dateTime.plusMinutes(-3);
        sql = "select count(*) from sms_wo_bill_log where status=4 and ctime>='"
                + dateTime.toString("yyyy-MM-dd HH:mm:ss") + "' and mobile='" + phoneNumber + "'";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        if (count >= 1) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 三分钟内只能成单一次 ").append(count).toString());
            return false;
        }

        return true;
    }

    private boolean provinceLimit(String phoneNumber, String province, int paytypeId, int maxDayFee, String channelType) {
        //----------------------------- 省份日上限 -----------------------
        int type = getPcSpType(channelType);

        int tempFee = cacheCheckUser.getCalledProvinceDayFee(province + Constants.split_str + "pc" + paytypeId + type);

        if (maxDayFee > 0 && tempFee >= maxDayFee) {
            LogEnum.DEFAULT.info(channelType + "  " + new StringBuilder(phoneNumber).
                    append("---- 超省份日上限 ").append(maxDayFee)
                    .append(",日费用：")
                    .append(tempFee).toString());
            return false;
        }

        StringBuilder message = new StringBuilder(channelType + "  " + phoneNumber).
                append("---- 省份").append(province).append("收入正常，")
                .append("日费用：")
                .append(tempFee).append("  最高上限:" + maxDayFee);
        return true;
    }

    private boolean checkYdTotalLimit() {
        DateTime dateTime = new DateTime();
        String today = dateTime.toString("yyyy-MM-dd");
        String sql = "select sum(fee)/100 from tbl_user_pc_card_log where channelid=21 and ext='6' and status=2 and btime>='"
                + today + "'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        int max = propertyUtils.getInteger("yd.daily.fee.limit", 10000);
        if (count != null && count >= max) {
            LogEnum.DEFAULT.info("电信翼支付 一天上限为" + new StringBuilder().append(max).append(" ").append(count).toString());
            return false;
        }

        sql = "select sum(fee)/100 from tbl_user_pc_card_log where channelid=21 and ext='6' and status=2 and btime>='"
                + dateTime.toString("yyyy-MM-dd HH:00:00") + "'";
        count = jdbcTemplate.queryForObject(sql, Integer.class);
        max = propertyUtils.getInteger("yd.hour.fee.limit", 2000);
        if (count != null && count >= max) {
            LogEnum.DEFAULT.info("电信翼支付  一小时上限为" + new StringBuilder().append(max).append(" ").append(count).toString());
            return false;
        }

        sql = "select sum(fee)/100 from tbl_user_pc_card_log where channelid=21 and ext='6' and btime>='"
                + dateTime.toString("yyyy-MM-dd HH:00:00") + "'";
        count = jdbcTemplate.queryForObject(sql, Integer.class);
        max = propertyUtils.getInteger("yd.hour.fee.limit", 2000) * 2;
        if (count != null && count >= max) {
            LogEnum.DEFAULT.info("电信翼支付  一小时请求上限为" + new StringBuilder().append(max).append(" ").append(count).toString());
            return false;
        }
        return true;
    }

    protected String getTestUserPrice(String phoneNum, String price) {
        if (StringUtils.contains(propertyUtils.getProperty("test.user.list"), phoneNum)) {
            return "1";
        }
        return price;
    }


    private void saveLog(String cid, String phoneNum, BigDecimal price, Map<String, Object> map, LtPcResult result, String ext) {
        SmsWoBillLog smsWoBillLog = new SmsWoBillLog();
        try {
            smsWoBillLog.setCpid(Integer.parseInt(cid));
            smsWoBillLog.setRandomid("1");
            smsWoBillLog.setMobile(phoneNum);
            smsWoBillLog.setTotalFee(price);
            smsWoBillLog.setPrice(smsWoBillLog.getTotalFee());
            smsWoBillLog.setQuantity(1);
            smsWoBillLog.setOutTradeNo(IdUtils.idGenerator("yd"));
            smsWoBillLog.setProvince(HaoduanCache.getProvince(phoneNum));
            smsWoBillLog.setAppKey("dlw");
            smsWoBillLog.setAppName("哆啦网");
            smsWoBillLog.setSubject("金币");
            smsWoBillLog.setIapId("jb");
            smsWoBillLog.setCallbackData(ext);
            smsWoBillLog.setWoCompanyId(2);
            if (result != null) {
                smsWoBillLog.setResultCode(result.getResultCode());
                smsWoBillLog.setResultDescription(result.getResultMessage());
                smsWoBillLog.setTransactionId(result.getSid());
            }
            Date now = new Date();
            smsWoBillLog.setCtime(now);
            smsWoBillLog.setUtime(now);
            billLogService.save(smsWoBillLog);
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
        }
    }


    private String syncToCP(SmsWoBillLog smsWoBillLog) {
        String chargeResultCode = smsWoBillLog.getChargeResultCode();
        try {
            String cpId = smsWoBillLog.getCpid().toString();
            User user = CpSyncCache.getCp(Integer.parseInt(cpId));
            if (user != null) {
                if (StringUtils.equals("4", smsWoBillLog.getStatus())) {
                    boolean reduceFlg = false;
                    CpNum cpNum = CpSyncCache.getCpNum(smsWoBillLog.getCpid());
                    if (cpNum != null) {
                        int reduce = cpNum.getReduce();
                        if (reduce > 0) {
                            int cpWoDayCount = cacheCheckUser.getWoDayCount(cpId + "-WO");
                            if (ReduceAlgorithm.isReduce(cpId, reduce, cpWoDayCount)) {
                                reduceFlg = true;
                                chargeResultCode = "8";
                                smsWoBillLog.setChargeResultCode(chargeResultCode);
                                smsWoBillLog.setType(1);
                                LogEnum.DEFAULT.info(new StringBuilder(smsWoBillLog.toString()).
                                        append("---- 坏帐信息 key=").append(cpId)
                                        .append(",坏帐比例：").append(reduce)
                                        .append("%， 成功访问数为：").append(cpWoDayCount)
                                        .append(" 被合作方默认设置坏帐...").toString());
                            }
                        }
                    }
                    cacheCheckUser.addWoFee(cpId,
                            smsWoBillLog.getTotalFee().intValue(),
                            reduceFlg, smsWoBillLog.getWoCompanyId(), "WO");

                    cacheCheckUser.addWoCompanyFee(smsWoBillLog.getTotalFee().intValue(),
                            smsWoBillLog.getWoCompanyId(), "WO");
                    if (reduceFlg) {
                        smsWoBillLog.setCpamount(new BigDecimal(0));
                        smsWoBillLog.setSyncFlg(5);
                    }
                }
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
            smsWoBillLog.setSyncFlg(1);
        } finally {
            billLogService.save(smsWoBillLog);
        }
        return chargeResultCode;
    }
}
