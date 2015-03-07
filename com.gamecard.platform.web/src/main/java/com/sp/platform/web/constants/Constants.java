package com.sp.platform.web.constants;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanglei on 15/2/8.
 */
public class Constants {
    public static final Map<String, String> errorMessage = new HashMap<String, String>();
    public static final Map<String, Integer> channelMap = new HashMap<String, Integer>();

    static {
        errorMessage.put("200000", "成功");
        errorMessage.put("200001", "系统内部错误");
        errorMessage.put("200002", "接入鉴权失败");
        errorMessage.put("201006", "用户余额不足");
        errorMessage.put("201005", "产品已订购，不能重复订购");
        errorMessage.put("200041", "packageID参数不合法");
        errorMessage.put("201002", "产品不存在");
        errorMessage.put("201004", "用户不存在");
        errorMessage.put("201007", "产品还未订购，不能退订");
        errorMessage.put("200058", "手机号码不合法");
        errorMessage.put("201021", "短信下发失败");
        errorMessage.put("201025", "Tcs无响应或异常");
        errorMessage.put("200004", "channelid参数不合法");
        errorMessage.put("201205", "用户状态不正常");
        errorMessage.put("201015", "计费失败");
        errorMessage.put("201022", "验证失败");
        errorMessage.put("200068", "短信验证码不一致");
        errorMessage.put("200080", "系统已受理请求，请稍后查看");
        errorMessage.put("201001", "内容不存在");
        errorMessage.put("200038", "参数不合法");
        errorMessage.put("200077", "主动充值超过次上限");
        errorMessage.put("200078", "主动充值超过天上限");
        errorMessage.put("200079", "主动充值超过月上限");

        channelMap.put("54282", 54);
        channelMap.put("84482", 55);
        channelMap.put("YXX82", 56);
    }

    public static String getErrorMessage(String key){
        if(StringUtils.isBlank(key)){
            return "";
        }
        String error = errorMessage.get(key);
        if(StringUtils.isNotBlank(error)){
            return error;
        }
        return key;
    }

    public static Integer getChannelId(String key){
        if(StringUtils.isBlank(key)){
            return 0;
        }
        Integer channelId = channelMap.get(key);
        if(channelId == null){
            return 0;
        }
        return channelId;
    }
}
