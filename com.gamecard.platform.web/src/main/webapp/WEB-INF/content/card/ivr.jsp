<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>
        中鸿九五-选择支付方式
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
    <div class="topnav-con"><span class="welcome">你好，欢迎来到中鸿九五！</span>
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
            <a href="http://www.duolawang.com/card/main.action" title="中鸿九五-短信声讯(话费)小额支付" target="_blank">
                中鸿九五-短信声讯(话费)小额支付
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
                <c:set var="pt" value="${paytype.oi}"/>
                <c:if test="${ 1 == paytype.oi }">
                    <c:set var="pt" value="17"/>
                </c:if>
                <c:if test="${ 3 == paytype.oi }">
                    <c:set var="pt" value="24"/>
                </c:if>
                <li id="chc_${paytype.id}" ref="${paytype.oi}">
                    <a <c:if test="${ paytypeId == pt }"> class="current" </c:if> href="select.action?id=${card.id}&priceId=${price.id}&paytypeId=${pt}">
                        <img src="${stx}/card-resources/resources/${paytype.img}" width="16" height="16">${paytype.op}
                    </a>
                </li>
            </c:forEach>
        </ol>
        <div class="tab-bd">
            <div id="divDetails" class="pannel">
                <fieldset>
                    <legend>中鸿九五手机支付</legend>

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
                            <tr>
                                <td colspan="6" align="left">
                                    &nbsp;&nbsp;<a href="index.action?id=${card.id}" style="font-size: 14px">重新选择面值</a>
                                </td>
                            </tr>
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
    <a href="javascript:void();" target="_blank">关于中鸿九五</a>|<a
        href="javascript:void();" target="_blank">公司介绍</a>|<a
        href="javascript:void();" target="_blank">联系我们</a>|<a
        href="javascript:void();" target="_blank">帮助中心</a>

    <p>Copyright © 2012 – 2015 duola. All Rights Reserved</p>

</div>

</body>
</html>
