package com.sp.platform.constants;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanglei on 15/2/8.
 */
public class Constants {
    public static final Map<String, Integer> channelMap = new HashMap<String, Integer>();
    public static final Map<Integer, Integer> sfidMap = new HashMap<Integer, Integer>();

    public static Map<Integer, String> feeConfig = new HashMap<Integer, String>();
    public static Map<String, String> resultCode = new HashMap<String, String>();

    static {

        initKzChannelConfig();
        sfidMap.put(56, 5);
        sfidMap.put(59, 5);
        sfidMap.put(62, 5);
        sfidMap.put(65, 5);
        sfidMap.put(68, 5);

        sfidMap.put(55, 3);
        sfidMap.put(57, 3);
        sfidMap.put(58, 3);
        sfidMap.put(60, 3);
        sfidMap.put(61, 3);
        sfidMap.put(63, 3);
        sfidMap.put(64, 3);
        sfidMap.put(66, 3);
        sfidMap.put(67, 3);
        sfidMap.put(69, 3);
        sfidMap.put(70, 3);
        sfidMap.put(71, 3);

        feeConfig.put(100, "006077839001"); //1元
        feeConfig.put(1000, "006077839010"); //10元
        feeConfig.put(1500, "006077839015"); //15元
        feeConfig.put(3000, "006077839027"); //30元

        resultCode.put("2000", "成功");
        resultCode.put("1002", "手机号非法或者非中国移动手机号");
        resultCode.put("4001", "系统内部错误");
        resultCode.put("4002", "接口请求单位时");
        resultCode.put("4003", "请求错误");
        resultCode.put("4004", "essionkey校");
        resultCode.put("4005", "订单信息有误");
        resultCode.put("4006", "参数有误");
        resultCode.put("4007", "订单不存在");
        resultCode.put("4008", "查询次数达到上限");
        resultCode.put("4009", "订单未完成");
        resultCode.put("5000", "应用校验失败");
        resultCode.put("5001", "P校验失败");
        resultCode.put("5002", "禁止调用的方法");
        resultCode.put("5003", "校验串错误");
        resultCode.put("5004", "缺少必要参数");
        resultCode.put("5005", "非法访问或调用");
        resultCode.put("5006", "时间戳超时");
        resultCode.put("5007", "接口调用过于频繁");
        resultCode.put("5008", "账号禁止充值");
        resultCode.put("5009", "充值请求过快");
        resultCode.put("200000", "成功");
        resultCode.put("200001", "系统内部错误");
        resultCode.put("200002", "接入鉴权失败");
        resultCode.put("201006", "用户余额不足");
        resultCode.put("201005", "产品已订购，不能重复订购");
        resultCode.put("200041", "packageID参数不合法");
        resultCode.put("201002", "产品不存在");
        resultCode.put("201004", "用户不存在");
        resultCode.put("201007", "产品还未订购，不能退订");
        resultCode.put("200058", "手机号码不合法");
        resultCode.put("201021", "短信下发失败");
        resultCode.put("201025", "Tcs无响应或异常");
        resultCode.put("200004", "channelid参数不合法");
        resultCode.put("201205", "用户状态不正常");
        resultCode.put("201015", "计费失败");
        resultCode.put("201022", "验证失败");
        resultCode.put("200068", "短信验证码不一致");
        resultCode.put("200080", "系统已受理请求，请稍后查看");
        resultCode.put("201001", "内容不存在");
        resultCode.put("200038", "参数不合法");
        resultCode.put("200077", "主动充值超过次上限");
        resultCode.put("200078", "主动充值超过天上限");
        resultCode.put("200079", "主动充值超过月上限");
        resultCode.put("201211", "游戏使用鉴权未通过");
        resultCode.put("200081", "contentID参数不合法");
        resultCode.put("201268", "Props ID does not correspond with the service ID");
    }

    private static void initKzChannelConfig() {
        channelMap.put("54282", 54);
        channelMap.put("84482", 55);
        channelMap.put("YXX82", 56);
        channelMap.put("54252", 57);
        channelMap.put("84452", 58);
        channelMap.put("YXX52", 59);

        channelMap.put("54251", 60);
        channelMap.put("84451", 61);
        channelMap.put("YXX51", 62);
        channelMap.put("54281", 63);
        channelMap.put("84481", 64);
        channelMap.put("YXX81", 65);
        //空中五元
        channelMap.put("54283", 66);
        channelMap.put("84483", 67);
        channelMap.put("YXX83", 68);

        //天下5元
        channelMap.put("54284", 69);
        channelMap.put("84484", 70);
        channelMap.put("YXX84", 71);
    }

    public static Integer getChannelId(String key) {
        if (StringUtils.isBlank(key)) {
            return 0;
        }
        Integer channelId = channelMap.get(key);
        if (channelId == null) {
            return 0;
        }
        return channelId;
    }

    public static Integer getSfId(Integer key) {
        Integer sfId = channelMap.get(key);
        if (sfId == null) {
            return 3;
        }
        return sfId;
    }

    public static void main(String[] args) {

        initKzChannelConfig();

        System.out.println(channelMap.size());
    }
}
