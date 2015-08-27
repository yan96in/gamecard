<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
    <meta charset="utf-8" content="">
    <title>哆啦网-确认支付</title>
    <meta name="description" content="">
    <link rel="shortcut icon" href="${stx}/card-resources/img/favicon.ico">
    <link rel="stylesheet" href="${stx}/card-resources/resources/base.css" media="all">
    <link rel="shortcut icon" href="${stx}/card-resources/img/favicon.ico">
    <link href="${stx}/card-resources/resources/blue.css" rel="stylesheet" type="text/css">
    <script src="${stx}/card-resources/resources/jquery.js" type="text/javascript"></script>
    <style type="text/css">
        #iframePay {
            margin-top: -200px;
            _margin-top: -220px;
        }

        #divIframePay {
            height: 150px;
            _height: 145px;
            overflow: hidden;
        }
    </style>
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
        <li>2、选择支付通道</li>
        <li class="on">3、获取点卡</li>
    </ol>
    <ol class="tab-hd clearfix">
        <li id="chc_${paytype.id}" ref="${paytype.id}">
            <a class="current" href="javascript:void();">
                <img src="${stx}/card-resources/resources/${paytype.img}" width="16" height="16">${paytype.name}
            </a>
        </li>
    </ol>
    <div class="tab-bd">
        <div class="pannel">
            <fieldset>
                <legend>哆啦网支付订单</legend>
                <div class="main-con sub-con">
                    <strong class="tips">
                        <font color="red"><b>${errorMsg}, 如有问题请联系客服，客服热线：400-0974-884。</b></font>
                    </strong>
                    <c:if test="${errorCode == 1}">
                        <br/>
                        <form action="${stx}/sp/lthj/getCard.action" id="form1" name="form1" method="post">
                            <input type="hidden" name="mobile" value="${mobile}"/>
                            <div class="field">
                                <label class="lab" style="text-align: left">请输入验证码：</label>
                                <input type="text" id="msgcontent" name="msgcontent" value="" maxlength="11"
                                       style="width: 172px; height: 34px; color: rgb(0, 0, 0);" class="input_correct"/>
                                <div id="txtCodeTip" class="onShow"></div>
                            </div>

                            <div class="btn-con">
                                <input class="btn" id="btnOK" type="submit" value="提交验证码" style="margin-left: 30px;">
                            </div>
                        </form>
                    </c:if>
                </div>
            </fieldset>
            <div class="point-list">
                <h3>支付说明</h3>
                <ol id="olFeeIntro">
                    <li>
                        此通道由中国移动通信帐户支付提供，中国移动通信账户支付是中国移动电子商务的支付方式之一。10658008是中国移动通信账户支付专用短信特服号码；
                    </li>
                    <li>
                        因平台升级，部分用户订购按次产品后24小时内将收到10086“*元/月”的短信提醒，不影响产品使用，无后续费用，如有疑问，请拨打客服电话；
                    </li>
                    <li>
                        本业务按次扣费，无免费试用，支付成功，即刻扣费，商品价格不含支付过程中产生的短信通信费；
                    </li>
                    <li>
                        因系统数据传输压力原因，部分省份用户在月底最后一天20:00后的扣费可能计入次月账单，请广大用户注意。
                    </li>
                    <li>
                        中国移动通信帐户支付服电话：4006125880（只收市话费，无长途费用）；125880(只支持移动手机，0.3元/分，无长途话费)；
                    </li>
                    <li>
                        在操作过程中，你的支付可能会因话费扣费数量限制而支付失败，若支付失败，请选择其它支付方式；
                    </li>
                    <li>
                        如有它疑问，请致电哆啦网支付客服热线：400-0974-884。
                    </li>
                </ol>
            </div>
        </div>
        <div class="pannel">
        </div>
    </div>
</div>
<form id="form99" name="form99" target="_blank" method="post" action=""></form>

<div class="footer">
    <a href="javascript:void();" target="_blank">关于哆啦网</a>|<a
        href="javascript:void();" target="_blank">公司介绍</a>|<a
        href="javascript:void();" target="_blank">联系我们</a>|<a
        href="javascript:void();" target="_blank">帮助中心</a>

    <p>Copyright © 2012 – 2015 duolawang. All Rights Reserved</p>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#form1").submit(function() {
                var len = $("#msgcontent").val().length;
                if (len < 6) {
                    $("#txtCodeTip").html("验证码有误");
                    $("#msgcontent").focus();
                    return false;
                } else {
                    return true;
                }
            });
        });
    </script>
</div>

<div style="display: none; position: fixed; left: 0px; top: 0px; width: 100%; height: 100%; cursor: move; opacity: 0; background-color: rgb(255, 255, 255); background-position: initial initial; background-repeat: initial initial;"></div>
</body>
</html>
