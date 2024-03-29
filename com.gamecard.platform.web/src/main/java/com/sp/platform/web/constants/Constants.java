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
    public static final Map<String, LthjService> lthjMap = new HashMap<String, LthjService>();
    static {
        initErrorMessage();

        initKzChannelConfig();

        initLthjConfig();
    }

    private static void initLthjConfig() {
        LthjService service = new LthjService();
        service.setMo("1162");
        service.setServiceid("ZHDXSD10Y");
        service.setMsg("您已成功订购盛大3元点券,资费10元，不含通信费，客服电话4000974884");
        service.setChannelid(74);
        lthjMap.put("1162", service);

        service = new LthjService();
        service.setMo("1146");
        service.setServiceid("ZHDXSD30Y");
        service.setMsg("您已成功订购盛大9元点券,资费30元，不含通信费，客服电话4000974884");
        service.setChannelid(75);
        lthjMap.put("1146", service);

        service = new LthjService();
        service.setMo("1147");
        service.setServiceid("ZHDXTX30Y");
        service.setMsg("您已成功订购天下通10元点券,资费30元，不含通信费，客服电话4000974884");
        service.setChannelid(76);
        lthjMap.put("1147", service);

        service = new LthjService();
        service.setMo("1163");
        service.setServiceid("ZHDXJW10Y");
        service.setMsg("您已成功订购骏网3元点券,资费10元，不含通信费，客服电话4000974884");
        service.setChannelid(72);
        lthjMap.put("1163", service);

        service = new LthjService();
        service.setMo("1148");
        service.setServiceid("ZHDXJW30Y");
        service.setMsg("您已成功订购骏网9元点券,资费30元，不含通信费，客服电话4000974884");
        service.setChannelid(73);
        lthjMap.put("1148", service);

        service = new LthjService();
        service.setMo("1082");
        service.setServiceid("ZHDXWM20Y");
        service.setMsg("您已成功订购完美6元点券,资费20元，不含通信费，客服电话4000974884");
        service.setChannelid(78);
        lthjMap.put("1082", service);

        service = new LthjService();
        service.setMo("1149");
        service.setServiceid("ZHDTH30Y");
        service.setMsg("您已成功订购天宏10元点券,资费30元，不含通信费，客服电话4000974884");
        service.setChannelid(77);
        lthjMap.put("1149", service);
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

    private static void initErrorMessage() {
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

    public static LthjService getLthjService(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return lthjMap.get(key);
    }

    public static void main(String[] args) {

        initKzChannelConfig();

        System.out.println(channelMap.size());
    }
}
