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
            <a href="${stx}/card/main.action" title="哆啦网-短信声讯(话费)小额支付" target="_blank">
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
    <form id="form1" name="form1" action="getPcCard.action" method="post">
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
                        <p class="active"><span>提醒</span>
                            <font style="color:#E53333;">收到验证码后，请务必即时提交并确认，以免影响您的支付！</font>
                        </p>

                        <div class="field">
                            <label class="lab">输入手机号码：</label>
                            <input type="text" id="phoneNumber" name="phoneNumber" value="" maxlength="11"
                                   style="width: 172px; height: 34px; color: rgb(0, 0, 0);" class="input_correct">
                            <input type="hidden" id="id" value="${card.id}" name="id"/>
                            <input type="hidden" id="priceId" value="${price.id}" name="priceId"/>
                            <input type="hidden" id="paytypeId" value="${paytype.id}" name="paytypeId"/>
                            <input type="hidden" id="channelId" value="0" name="channelId"/>
                            <input type="hidden" id="type" value="0" name="type"/>
                            <input type="hidden" id="sid" value="0" name="sid"/>
                            <div id="txtPayPhoneTip" class="onShow">
                            </div>
                            <p class="loc" id="pPayPhoneArea" style=""></p>
                        </div>
                        <div class="field">
                            <label class="lab">选择支付方式：</label>

                            <div class="controls fixw" id="divPayTypeList" style="width: 360px;">
                                <label id="pt_10111" ref="10111" class="selected">
                                    <a href="javascript:void(0);" class="radio-box">
                                        <i class="icon icon-sms"></i>使用话费支付<i class="icon-chk"></i>
                                    </a>
                                </label>

                            </div>
                        </div>
                        <div class="field">
                            <label class="lab">请输入验证码：</label>
                            <input type="text" id="identifyingCode" name="identifyingCode" value="" maxlength="11"
                                   style="display: none; width: 172px; height: 34px; color: rgb(0, 0, 0);" class="input_correct"/>
                            <input id="btnCode" class="btn2" style="display: none;" type="button" onclick="sendCode();" value="获取验证码">
                            <div class="controls channels  fixw">
                                <div id="divNoChannelTip" class="tips-con">请先输入支付号码，获取可用支付验证码！</div>
                            </div>
                        </div>
                        <div class="field">
                            <label class="lab"></label>
                            <div id="channelTip" style="border-width: 0px"></div>
                        </div>
                        <div class="btn-con">
                            <input id="btnOK" class="btn" type="submit" value="提交支付">
                            <a href="index.action?id=${card.id}">重新选择面值</a>
                        </div>
                        <div class="point-list" id="divChannelLimitInfo" style="width: 580px; margin-top: 20px;"><h3>
                            当前支付通道信息</h3>
                            <ol>
                                <li>支付限制：每个号码日限30元，月限90元，不含通信费，支付限额会动态调整，以最终支付结果为准；</li>
                                <li>
                                    开通地区：福建,湖北,广东,青海,山东,陕西,海南,浙江,贵州,新疆,河北,吉林,辽宁,内蒙古
                                </li>
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
                                <li><a class="" target="blank" href="http://www.duolawang.com/card/main.action"
                                       title="哆啦网正式上线！！">哆啦网官网正式上线！！</a></li>
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

    <p>Copyright © 2012 – 2015 duolawang. All Rights Reserved 北京世坤远大信息科技有限公司版权所有</p>

</div>

<script type="text/javascript">
    var channel1 = "";
    var channel2 = "";
    var feetypeStr= '';
    var submitflag = false;
    $(document).ready(function () {
        $("#form1").submit(function(){
            if(submitflag){
				var pccode = $("#identifyingCode").val();
				if (pccode == null || pccode == undefined || pccode == '') {
					alert('请输入验证码！');
					return false;
				}
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
            $("#divNoChannelTip").html("请先输入支付号码，获取可用支付验证码！").show();
            $("#divFeeTypeList").html("").hide();
            $("#btnCode").hide();
            $("#identifyingCode").hide();
            $("#channelId").val("0");
            $("#sid").val("0");
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

    function codeError2(message){
        $(this).removeClass("input_show").addClass("input_error")
        $("#txtPayPhoneTip").removeClass("onFocus").removeClass("onCorrect").addClass("onError").html(message);
    }

    function codeCheck(phoneNumber){
        $.ajax({
            type: "GET",
            url: "checkPcPhone.action",
            data: {phoneNumber:phoneNumber, id: ${card.id}, priceId: ${price.id}, paytypeId: ${paytype.id}},
            dataType: "json",
            success: function (data) {
                if (data.flag) {
                    $("#txtPayPhoneTip").removeClass("onFocus").addClass("onCorrect").html("OK!");
                    $("#pPayPhoneArea").html("号码所属：<strong>" + data.result.phoneVo.province + "省 " +
                        data.result.phoneVo.city + "市</strong></p>").show();

                    var flag = 0;
                    if(data.result.pcflag){
                        flag = flag + 1;
                    }

                    if(flag > 0){
                        $("#btnCode").show();
                        $("#divNoChannelTip").html("<font color='red'><b>请点击“获取验证码”.</b></font>");
                    }else{
                        $("#divNoChannelTip").html("<font color='red'><b>该号码所属省份无法使用此通道，请选择其它方式</b></font>");
                    }
                }else {
                    codeError2(data.msg);
                }
            },
            onWait: "正在校验手机号码，请稍候..."
        });
    }

    function sendCode(){
		$("#divNoChannelTip").html("");
        $("#channelId").val("0");
        $("#sid").val("0");
        var phoneNumber = $("#phoneNumber").val();
        $.ajax({
        type: "GET",
        url: "sendPcCode.action",
        data: {phoneNumber:phoneNumber, id: ${card.id}, priceId: ${price.id}, paytypeId: ${paytype.id}},
        dataType: "json",
        success: function (data) {
        if (data.flag) {
			var flag = 0;
			if(data.result.pcflag){
				flag = flag + 1;
			}

			if(flag > 0){
				submitflag = true;
				$("#identifyingCode").show();
                $("#channelId").val(data.result.channelId);
                $("#sid").val(data.result.sid);
                $("#type").val(data.result.type);
				$("#divNoChannelTip").html("<font color='red'><b>请输入收到的验证码，完成支付</b></font>");
			}else{
				$("#identifyingCode").hide();
				$("#btnCode").hide();
                if('3' == data.result.resultCode){
                    $("#divNoChannelTip").html("<font color='red'><b>请您过60分钟以后再尝试购买</b></font>");
                } else {
                    $("#divNoChannelTip").html("<font color='red'><b>该号码无法使用此通道，请选择其它方式</b></font>");
                }
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
