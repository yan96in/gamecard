package com.sp.platform.gateway.resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.entity.CpNum;
import com.sp.platform.entity.SmsWoBillLog;
import com.sp.platform.entity.User;
import com.sp.platform.gateway.constant.Status;
import com.sp.platform.gateway.exception.BusinessException;
import com.sp.platform.gateway.response.wo.PaymentChargeResponse;
import com.sp.platform.service.SmsWoBillLogService;
import com.sp.platform.service.sp.SpYdService;
import com.sp.platform.util.*;
import com.sp.platform.vo.LtPcResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Path("/yd")
public class YdResource extends BaseResource {
    @Autowired
    private PropertyUtils propertyUtils;
    @Autowired
    private SpYdService spYdService;
    @Autowired
    private SmsWoBillLogService billLogService;
    @Autowired
    private CacheCheckUser cacheCheckUser;

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
        try {
            CpNum cpNum = validateParameter(cid, phoneNum, price);

            //测试手机转低资费
            price = getTestUserPrice(phoneNum, price);

            User user = CpSyncCache.getCp(Integer.parseInt(cid));

            result = spYdService.sendYdCode(phoneNum, Integer.parseInt(price));  // 移动游戏

            if (result != null && result.isFlag()) {
                LogEnum.DEFAULT.info(cid + user.getShowname() + " 申请移动游戏下发验证码 成功: " + result.toString());
                return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("linkid=" + result.getSid()).build();
            } else {
                LogEnum.DEFAULT.info(cid + user.getShowname() + " 申请移动游戏下发验证码 失败: " + result.toString());
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
                saveLog(cid, phoneNum, price, map, result, ext);
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
            if (StringUtils.isNotBlank(woBillLog.getStatus())) {
                return getReturnResponse(woBillLog);
            }

            woBillLog.setDescription(paymentCode);

            String body = spYdService.commitPaymentCode(woBillLog.getMobile(), linkid, paymentCode);
            JSONObject object = JSON.parseObject(body);
            String resultCode = object.getString("resultCode");
            String resultMsg = object.getString("resultMsg");

            if (StringUtils.equals(resultCode, "20000")) {
                woBillLog.setStatus("4");
                woBillLog.setCpamount(woBillLog.getTotalFee());
                LogEnum.DEFAULT.info(linkid + " 移动计费成功: " + object.toString());
            } else {
                woBillLog.setStatus("8");
                woBillLog.setCpamount(new BigDecimal(0));
                LogEnum.DEFAULT.info(linkid + "移动计费失败: " + object.toString());
            }

            woBillLog.setChargeResultCode(resultCode);
            woBillLog.setResultDescription(resultMsg);
            woBillLog.setUtime(new Date());
            syncToCP(woBillLog);

            return getReturnResponse(woBillLog);
        } catch (BusinessException e) {
            LogEnum.DEFAULT.error(e.toString());
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("status=" + e.getMessage()).build();
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

        if (StringUtils.indexOf(propertyUtils.getProperty("black.cp"), cid) >= 0) {
            throw new Exception("渠道黑名单" + cid);
        }

        return cpNum;
    }

    protected String getTestUserPrice(String phoneNum, String price) {
        if (StringUtils.contains(propertyUtils.getProperty("test.user.list"), phoneNum)) {
            return "0.01";
        }
        return price;
    }


    private void saveLog(String cid, String phoneNum, String price, Map<String, Object> map, LtPcResult result, String ext) {
        SmsWoBillLog smsWoBillLog = new SmsWoBillLog();
        try {
            smsWoBillLog.setCpid(Integer.parseInt(cid));
            smsWoBillLog.setRandomid("1");
            smsWoBillLog.setMobile(phoneNum);
            smsWoBillLog.setTotalFee(new BigDecimal(price));
            smsWoBillLog.setPrice(smsWoBillLog.getTotalFee());
            smsWoBillLog.setQuantity(1);
            smsWoBillLog.setOutTradeNo(IdUtils.idGenerator("yd"));
            smsWoBillLog.setProvince(HaoduanCache.getProvince(phoneNum));
            smsWoBillLog.setAppKey("cwbk");
            smsWoBillLog.setAppName("成王败寇");
            smsWoBillLog.setSubject("元宝");
            smsWoBillLog.setIapId("iapYb");
            smsWoBillLog.setCallbackData(ext);
            smsWoBillLog.setWoCompanyId(0);
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
                                chargeResultCode = "200076";
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
