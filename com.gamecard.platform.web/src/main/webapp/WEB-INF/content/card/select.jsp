<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>
        哆啦网-选择支付方式
    </title>
    <meta name="description">
    <link rel="stylesheet" href="${stx}/card-resources/resources/base.css" media="all">
    <link rel="shortcut icon" href="${stx}/card-resources/img/favicon.ico">
    <link href="${stx}/card-resources/resources/blue.css" rel="stylesheet" type="text/css">
    <script src="${stx}/card-resources/resources/jquery.js" type="text/javascript"></script>
    <link href="${stx}/card-resources/resources/style.css" rel="stylesheet" type="text/css">
</head>

<body class="body">
<div class="" style="display: none; position: absolute;">
    <div class="aui_outer">
        <table class="aui_border">
            <tbody>
            <tr>
                <td class="aui_nw"></td>
                <td class="aui_n"></td>
                <td class="aui_ne"></td>
            </tr>
            <tr>
                <td class="aui_w"></td>
                <td class="aui_c">
                    <div class="aui_inner">
                        <table class="aui_dialog">
                            <tbody>
                            <tr>
                                <td colspan="2" class="aui_header">
                                    <div class="aui_titleBar">
                                        <div class="aui_title" style="cursor: move;"></div>
                                        <a class="aui_close" href="javascript:/*artDialog*/;">×</a></div>
                                </td>
                            </tr>
                            <tr>
                                <td class="aui_icon" style="display: none;">
                                    <div class="aui_iconBg"
                                         style="background-image: none; background-position: initial initial; background-repeat: initial initial;"></div>
                                </td>
                                <td class="aui_main" style="width: auto; height: auto;">
                                    <div class="aui_content" style="padding: 20px 25px;"></div>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="aui_footer">
                                    <div class="aui_buttons" style="display: none;"></div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </td>
                <td class="aui_e"></td>
            </tr>
            <tr>
                <td class="aui_sw"></td>
                <td class="aui_s"></td>
                <td class="aui_se" style="cursor: se-resize;"></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<div class="topnav">
    <div class="topnav-con"><span class="welcome">你好，欢迎来到哆啦网！</span>
        <p>
            <a href="javascript:void();" target="_blank">官网</a>|<a
                href="javascript:void();" target="_blank">支付验证通道</a>|<a
                href="javascript:void();" target="_blank">平台公告</a>|<a
                href="javascript:void();" target="_blank">帮助中心</a>
        </p>
    </div>
</div>
<div class="header">
    <div class="header-con">
        <h1 class="logo">
            <a href="http://www.71pay.cn/" title="哆啦网-短信声讯(话费)小额支付" target="_blank">
                哆啦网-短信声讯(话费)小额支付
            </a>
            <img src="${stx}/card-resources/resources/images/sub-logo.png">
        </h1>

        <p style="height:30px">
            如需帮助，请咨询客服热线<strong class="c-num">400-757-1234（9:00-22:00）</strong>
        </p>
    </div>
</div>
<div class="bodyer">
    <ol class="steps clearfix">
        <li>1、提交订单</li>
        <li class="on">2、选择支付通道</li>
        <li>3、确认支付</li>
        <li>4、获取商品</li>
    </ol>
    <ul class="order-info">
        <li>
            <span class="product-name">商品信息：
                <a href="javascript:void(0)">${card.name}_${price.description}${card.description}</a>
            </span>
        </li>
    </ul>
    <form id="form1" name="form1" action="channel.action" method="post">
        <ol class="tab-hd clearfix" id="divChannelClassList">
            <c:forEach var="paytype" items="${price.paytypes}">
                <li id="chc_${paytype.id}" ref="${paytype.id}">
                    <a <c:if test="${ paytypeId == paytype.id }"> class="current" </c:if> href="select.action?id=${card.id}&priceId=${price.id}&paytypeId=${paytype.id}">
                        <img src="${stx}/card-resources/resources/${paytype.img}" width="16" height="16">${paytype.name}
                    </a>
                </li>
            </c:forEach>
        </ol>
        <div class="tab-bd">
            <div id="divDetails" class="pannel">
                <fieldset>
                    <legend>哆啦网手机支付</legend>

                    <div class="main-con fl">
                        <p class="active"><span>提醒</span><font
                                style="color:#E53333;">提交成功后，请务必即时发送短信并确认，以免影响您的号码支付额度！</font></p>

                        <div class="field">
                            <label class="lab">输入手机号码：</label>
                            <input type="text" id="phoneNumber" name="phoneNumber" value="" maxlength="11"
                                   style="width: 172px; height: 34px; color: rgb(0, 0, 0);" class="input_correct">

                            <div id="txtPayPhoneTip" class="onShow">
                            </div>
                            <p class="loc" id="pPayPhoneArea" style=""></p>
                        </div>
                        <div class="field">
                            <label class="lab">选择支付方式：</label>

                            <div class="controls fixw" id="divPayTypeList" style="width: 360px;">
                                <label id="pt_10111" ref="10111" class="selected">
                                    <a href="javascript:void(0);" class="radio-box">
                                        <i class="icon icon-sms"></i>使用短信支付<i class="icon-chk"></i>
                                    </a>
                                </label>

                            </div>
                        </div>
                        <div class="field">
                            <label class="lab">选择支付通道：</label>

                            <div class="controls channels  fixw">
                                <div class="tips-box" id="divNoChannelTip" style="">
                                    <div class="tips-con">请先输入支付号码，然后再获取可用支付通道！</div>
                                </div>
                                <div id="divFeeTypeList" style="">
                                </div>

                            </div>
                        </div>
                        <div class="field">
                            <label class="lab"></label>
                            <div id="channelTip" style="border-width: 0px"></div>
                        </div>
                        <div class="btn-con">
                            <input id="btnOK" class="btn" type="submit" value="提交支付">
                            <a href="index.action">重新选择商品</a>
                        </div>
                        <div class="point-list" id="divChannelLimitInfo" style="width: 580px; margin-top: 20px;"><h3>
                            当前支付通道信息</h3>
                            <ol>
                                <li>支付限制：每个号码日限30元，月限90元，不含通信费，支付限额会动态调整，以最终支付结果为准；</li>
                                <li>
                                    开通地区：北京，天津，河北省，山西省，内蒙古自治区，辽宁省，吉林省，黑龙江省，上海，江苏省，浙江省，安徽省，福建省，江西省，山东省，河南省，湖北省，湖南省，广东省，广西壮族自治区，海南省，重庆，四川省，贵州省，云南省，西藏自治区，陕西省，青海省，宁夏回族自治区
                                </li>
                                <li>支付费用：包含你购买的“商品金额”和“通道费用”两部分，因此支付费用通常会高于您购买的商品金额；</li>
                                <li>提交支付：系统会根据你提交的商品品、支付号码、支付通道等信息处理支付请求，如您提交的支付请求受系统限制而不能支付，请选择其它支付方式或隔日重试；</li>
                            </ol>
                        </div>

                    </div>
                    <div class="sub-tips fr">
                        <div class="point-list" id="payTypeIntro"><h3>
                            中国${paytype.name}说明：
                        </h3>
                            <ol>
                                ${paytype.description}
                            </ol>
                        </div>

                        <div class="point-list1">
                            <h3 style="color:#0066CC">平台公告</h3>
                            <ol>
                                <li><a class="" target="blank" href="http://gateway.71pay.cn/Site/Content_57.shtml"
                                       title="通品网官网正式上线！！">[ 06/18]通品网官网正式上线！！</a></li>
                            </ol>
                        </div>

                    </div>
                </fieldset>

            </div>
            <div class="pannel">
            </div>
        </div>
    </form>
</div>

<div class="footer">
    <a href="javascript:void();" target="_blank">关于哆啦网</a>|<a
        href="javascript:void();" target="_blank">公司介绍</a>|<a
        href="javascript:void();" target="_blank">联系我们</a>|<a
        href="javascript:void();" target="_blank">帮助中心</a>

    <p>Copyright © 2012 – 2015 71pay. All Rights Reserved 北京世坤远大信息科技有限公司版权所有</p>

</div>

<script type="text/javascript">
    var channel1 = "";
    var channel2 = "";
    var feetypeStr= '';
    var submitflag = false;
    $(document).ready(function () {
        $("#form1").submit(function(){
            if(submitflag){
                return true;
            }else{
                $("#txtPayPhoneTip").removeClass("onShow").removeClass("onError").addClass("onFocus").html("请输入手机号码");
                $("#phoneNumber").focus();
                return false;
            }
        });

        $("#phoneNumber").focus(function(){
            submitflag = false;
            $(this).removeClass("input_error").addClass("input_show");
            $("#pPayPhoneArea").html("").hide();
            $("#divNoChannelTip").html("请先输入支付号码，然后再获取可用支付通道！").show();
            $("#divFeeTypeList").html("").hide();
            $("#txtPayPhoneTip").removeClass("onShow").removeClass("onError").addClass("onFocus").html("请输入手机号码");
        }).keyup(function(){
            flag = false;
            var phonenumber = $(this).val();
            if(phonenumber.length == 11){
                codeCheck(phonenumber);
            }
        }).blur(function(){
            var phonenumber = $(this).val();
            if(phonenumber.length == 11){
                codeCheck(phonenumber);
            } else {
                codeError();
            }
        });
    });

    function codeError(){
        $(this).removeClass("input_show").addClass("input_error")
        $("#txtPayPhoneTip").removeClass("onFocus").removeClass("onCorrect").addClass("onError").html("请输入正确的手机号码！");
    }
    function codeCheck(phoneNumber){
        $.ajax({
            type: "GET",
            url: "checkPhone.action",
            data: {phoneNumber:phoneNumber, id: ${card.id}, priceId: ${price.id}, paytypeId: ${paytype.id}},
            dataType: "json",
            success: function (data) {
                if (data.flag) {
                    flag = true;
                    $("#txtPayPhoneTip").removeClass("onFocus").addClass("onCorrect").html("OK!");
                    $("#pPayPhoneArea").html("号码所属：<strong>" + data.result.phoneVo.province + "省 " +
                        data.result.phoneVo.city + "市</strong></p>").show();
                    var flag = 0;
                    feetypeStr= '';
                    channel1= '';
                    channel2= '';
                    if(data.result.channels1 != null && data.result.channels1.length>0){
                        feetypeStr = feetypeStr + '<label id="ft_1" ref="101111" class="selected" onclick="javascript:funChannel(this);"><a href="javascript:void(0);" class="radio-box">大额通道(单次扣费)<i class="icon-triangle"></i></a></label>';
                    }
                    if(data.result.channels2 != null && data.result.channels2.length>0){
                        feetypeStr = feetypeStr + '<label id="ft_2" ref="102112" class="" onclick="javascript:funChannel(this);"><a href="javascript:void(0);" class="radio-box">点播通道(多条扣费)<i class="icon-triangle"></i></a></label>';
                    }
                    if(data.result.channels1 != null && data.result.channels1.length>0){
                        flag = flag + 1;
                        channel1 = channel1 + '<div class="channel-list" id="divPayChannelList"><ul>';
                        jQuery.each(data.result.channels1, function(index,channel){
                            channel1 = channel1 + '<li id="ch_'+channel.id+'" ref="'+channel.id+'" class="selected"><input type="radio" id="payChannel_'+channel.id+'" name="channelId" checked="checked" value="'+channel.id+'"><label for="payChannel_'+channel.id+'">短信支付'+(index+1)+'：需支付<strong class="c-num">'+channel.fee+'</strong>元话费（'+channel.fee+'元/次，共扣1次)</label>';
                        });
                        channel1 = channel1 + '</ul></div>';
                    }
                    if(data.result.channels2 != null && data.result.channels2.length>0){
                        channel2 = channel2 + '<div class="channel-list" id="divPayChannelList"><ul>';
                        jQuery.each(data.result.channels2, function(index,channel){
                            channel2 = channel2 + '<li id="ch_'+channel.id+'" ref="'+channel.id+'" class="selected"><input type="radio" id="payChannel_'+channel.id+'" name="channelId" checked="checked" value="'+channel.id+'"><label for="payChannel_'+channel.id+'">短信支付'+(index+1)+'：需支付<strong class="c-num">'+channel.fee+'</strong>元话费（'+channel.fee+'元/次，共扣'+channel.feecount+'次)</label>';
                        });
                        channel2 = channel2 + '</ul></div>';
                        flag = flag + 2;
                    }

                    if(flag > 0){
                        submitflag = true;
                        $("#divNoChannelTip").hide();
                        var html = "";
                        if(flag == 1 || flag == 3){
                            html = feetypeStr + channel1;
                        }else if(flag == 2){
                            html = feetypeStr + channel2;
                        }
                        $("#divFeeTypeList").html(html).show();
                    }else{
                        $("#divNoChannelTip").html("该号码无可用通道，请选择其它方式");
                    }
                }else {
                    codeError();
                }
            },
            onWait: "正在校验手机号码，请稍候..."
        });
    }

    function funChannel(obj){
        var id = obj.id;
        var str = "";
        if(id == 'ft_1'){
            str = feetypeStr + channel1;
        }else{
            str = feetypeStr + channel2;
        }

        $("#divFeeTypeList").html(str);
        if(id == 'ft_1'){
            $("#ft_1").attr("class","selected");
            $("#ft_2").attr("class","");
        }else{
            $("#ft_1").attr("class","");
            $("#ft_2").attr("class","selected");
        }
    }
</script>

</body>
</html>
