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
    <style type="text/css">
            /*二级页面表格*/
        .content{
            line-height:30px;
            border:1px solid #D9D9D9;
            border-collapse:collapse;
            text-align:center;
            width:900px;
            margin-left:10px;
            padding:2px;
        }

        .content th{
            color:#CC6600;
            background:#FFF9BD;
            font-size:13px;
            border:1px solid #D9D9D9;
        }

        .content td{
            border:1px solid  #D9D9D9;
            font-size:12px;
        }
    </style>
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
            <a href="http://www.duolawang.com/card/main.action" title="哆啦网-短信声讯(话费)小额支付" target="_blank">
                哆啦网-短信声讯(话费)小额支付
            </a>
            <img src="${stx}/card-resources/resources/images/sub-logo.png">
        </h1>

        <p style="height:30px">
            如需帮助，请咨询客服热线<strong class="c-num">400-0974-884（9:00-22:00）</strong>
        </p>
    </div>
</div>
<div class="bodyer">
    <ol class="steps clearfix">
        <li>1、提交订单</li>
        <li class="on">2、选择支付通道</li>
        <li>3、通道详情</li>
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
                        <table class="content" border="0" cellspacing="0" cellpadding="3" width="100%">
                            <tr>
                                <th width="9%">
                                    声讯号码
                                </th>
                                <th width="13%">
                                    点卡类别
                                </th>
                                <th width="16%">
                                    购买方式
                                </th>
                                <th width="19%">
                                    <strong>资费说明</strong>
                                </th>
                                <th width="19%">
                                    <strong>开通省份</strong>
                                </th>
                                <th width="24%">
                                    备注
                                </th>
                            </tr>
                            <c:forEach var="channel" items="${list}">
                                <tr>
                                    <td>${channel.called}</td>
                                    <td>${card.name}_${price.description}</td>
                                    <td>${channel.detail}</td>
                                    <td>${channel.fee}</td>
                                    <td>${channel.province}</td>
                                    <td>${channel.note}</td>
                                </tr>
                            </c:forEach>
                        </table>
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

    <p>Copyright © 2012 – 2015 duola. All Rights Reserved 北京世坤远大信息科技有限公司版权所有</p>

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
    $("#divNoChannelTip").html("<font color='red'><b>该号码所属省份无可用通道，请选择其它方式</b></font>");
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
