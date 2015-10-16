<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8" content="">
    <title>中鸿九五-点卡销售</title>
    <meta name="description" content="">
    <link rel="shortcut icon" href="${stx}/card-resources/img/favicon.ico">
    <link href="${stx}/card-resources/resources/base.css" rel="stylesheet" type="text/css">
    <script src="${stx}/card-resources/resources/jquery.js" type="text/javascript"></script>
    <script src="${stx}/card-resources/resources/formValidator.js" type="text/javascript"></script>
    <script src="${stx}/card-resources/resources/formValidatorRegex.js" type="text/javascript"></script>
    <style type="text/css">
        * {
            margin: 0px auto;
            padding: 0px;
        }

        #all {
            width: 950px;
        }

        #top {
            width: 950px;
            height: 174px;
            background-image: url(${stx}/card-resources/resources/images/index_r1_c1.jpg);
            background-repeat: no-repeat;
            float: left;
        }

        #mid {
            background-image: url(${stx}/card-resources/resources/images/index_r3_c2.gif);
            height: auto;
            width: 950px;
            float: left;
        }

        #foot {
            background-image: url(${stx}/card-resources/resources/images/index_r7_c1.gif);
            background-repeat: no-repeat;
            height: 60px;
            width: 950px;
            float: left;
        }
    </style>

    <link href="${stx}/card-resources/resources/style.css" rel="stylesheet" type="text/css">
</head>

<body class="body">

<div id="all">
    <div id="top">
    </div>
    <div id="mid">
        <div class="cols-main" style="margin-left: 150px;">
            <form id="form1" name="form1" action="select.action" method="post">
                <fieldset>
                    <legend>中鸿九五订单提交-卡密</legend>
                    <p class="active"><span>公告</span><a target="_blank" href="javascript:void();">移动手机验证码通道12月23日重新开通，资费全面，查看详情!</a>
                    </p>

                    <div class="field">
                        <input type="hidden" id="cardId" name="id" value="${card.id}">
                        <label class="lab">商品名称：</label>
                        <strong class="product-name" style="font-size:25"><b>${card.name}</b></strong><strong class="product-name">${card.description}</strong>
                    </div>
                    <div class="field">
                        <label class="lab">选择商品面值：</label>
                        <input type="hidden" id="priceId" name="priceId" value="${card.prices[0].id}">

                        <div class="controls" id="priceList">
                            <c:set var="frist" value='true'/>
                            <c:forEach var="price" items="${card.prices}">
                                <label id="pcid_${price.id}" ref="${price.id}"
                                       <c:if test="${ 'true' == frist }">class="selected"</c:if>>
                                    <a href="javascript:void(0);" class="radio-box">${price.description}
                                        <c:if test="${ 'true' == frist }"><i class="icon-chk"></i></c:if>
                                        <c:if test="${ 'true' != frist }"><i></i></c:if>
                                    </a>
                                </label>
                                <c:set var="frist" value='false'/>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="field">
                        <label class="lab">可选支付方式：</label>

                        <div class="controls pay-channels">
                            <div class="pay-channels-box" id="channelClassList" style="margin-left: 0px;">

                                <c:set var="frist" value='true'/>
                                <c:forEach var="paytype" items="${list}">
                                    <label class="radio-box selected" id="chc_${paytype.id}"
                                           style="display: inline-block;">
                                        <input type="radio" id="rchc_${paytype.id}" name="paytypeId"
                                               value="${paytype.oi}"
                                               <c:if test="${ 'true' == frist }">checked="checked"</c:if>>
                                        <img src="${stx}/card-resources/resources/${paytype.img}" width="16"
                                             height="16">${paytype.op}
                                        <c:if test="${ 'true' == frist }"><i class="icon-chk"></i></c:if>
                                        <c:if test="${ 'true' != frist }"><i></i></c:if>
                                    </label>
                                    <c:set var="frist" value='false'/>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <!--
                    <div class="field">
                        <label class="lab">验证码：</label>
                        <input id="txtCheckCode" name="txtCheckCode" type="text" maxlength="4" style="width:80px; padding: 17px 6px;" class="input_show">
                        <a href="javascript:LoadCheckCode();" title="看不清，换一张"><img id="imgCode" name="imgCode"
                                 src="${stx}/jcaptcha"
                                 alt="" width="85" height="34px"
                                 style="border:none;"></a>

                        <div id="txtCheckCodeTip"
                             style="border-width: 0px; margin: 0px; padding: 0px; background-color: transparent; background-position: initial initial; background-repeat: initial initial;"
                             class="onShow">
                                <div id="txtCheckCodeTip2" class="onShow"></div>
                        </div>
                    </div>
                    -->
                    <div class="btn-con">
                        <input id="btnOK" class="btn" type="submit" value="提交订单">
                        <a href="main.action">重新选择商品</a>
                    </div>
                </fieldset>
            </form>

            <div class="point-list">
                <h3>温馨提示</h3>
                <ol>
                    <li>
                        移动短信购买方式扣费以通道提示为准。<br>
                    </li>
                    <li>
                        为了保护您的利益，请在得卡密后2小时内进行充值，否则此卡将自动失效。
                    </li>
                    <li>
                        如有其他疑问，请咨询中鸿九五客服热线：400-0974-884。
                    </li>
                </ol>
            </div>

        </div>
        <br>
        <br>
        <br>
        <br>
    </div>

    <div id="foot">
        <div style="margin-top: 30px; font-size: 14px; text-align: center;">中鸿九五</div>
    </div>
</div>

<script type="text/javascript">
    var flag = false;
    $(document).ready(function () {
    //LoadCheckCode(); 取验证码

    //初始化面值通道

    <c:forEach var="price" items="${card.prices}">
        var arrayPT = new Array();
        <c:set var="index" value="0"/>

        <c:forEach var="paytype" items="${price.paytypes}">
            arrayPT.push(${paytype.id});
        </c:forEach>
        var arrChannel_${price.id}=arrayPT;
    </c:forEach>

    <%--var arrChannel_104103=[1011,1021];--%>
    <%--var arrChannel_104102=[1011,1021];--%>
    <%--var arrChannel_104106=[1011,1021];--%>
    <%--var arrChannel_104105=[1011,1021,1031];--%>

    $("#channelClassList label").hide();

    //设置默认产品
    var defPriceID = $("#priceId").val();
    $("#pcid_" + defPriceID).addClass("selected").children("a").children("i").addClass("icon-chk");
    //设置通道分类
    var curChannelArray=eval("arrChannel_" +defPriceID);
    $.each(curChannelArray,function(idx,id){
    $("#chc_"+id).show();
    });
    $("#channelClassList label").removeClass("selected").children("i").removeClass("icon-chk");
    $("#chc_"+ curChannelArray[0]).addClass("selected").children("i").addClass("icon-chk");
    $("#rchc_"+ curChannelArray[0]).attr("checked","checked");

    //选择产品操作
    $("#priceList label").click(function () {
        var priceID = $(this).attr("ref");
        $("#priceId").val(priceID);
        $(this).siblings().removeClass("selected").children("a").children("i").removeClass("icon-chk");
        $(this).addClass("selected").children("a").children("i").addClass("icon-chk");

        //设置通道分类
        $("#channelClassList label").hide();
        $.each(eval("arrChannel_" +priceID),function(idx,id){
        $("#chc_"+id).show();

    });
    //选中第一个通道分类
    curChannelArray=eval("arrChannel_" +priceID);
    $("#channelClassList label").removeClass("selected").children("i").removeClass("icon-chk");
    $("#chc_"+ curChannelArray[0]).addClass("selected").children("i").addClass("icon-chk");
    $("#rchc_"+ curChannelArray[0]).attr("checked","checked");
    });

    //选择通道分类操作
    $("#channelClassList label").click(function () {
        $(this).siblings().removeClass("selected").children("i").removeClass("icon-chk");
        $(this).addClass("selected").children("i").addClass("icon-chk");
        $("input[name='paytypeId']").removeAttr("checked");
        $(this).find(":radio").attr("checked","checked");
    });

    });

    //取验证码
    function LoadCheckCode() {
        document.getElementById("imgCode").src = "${stx}/jcaptcha?now=" + new Date().getTime();
    }

</script>

<script type="text/javascript">
    $(document).ready(function () {
        $("#form1").submit(function(){
            if($("#priceId").val() == undefined){
                return false;
            }
            if($("input[name='paytypeId']:checked").val() == undefined){
                return false;
            }
        });

    });

    function codeCheck(code){
        $.ajax({
            type: "GET",
            url: "${stx}/jcaptchaCheck",
            data: {checkCode:code},
            dataType: "html",
            success: function (data) {
                if (data == "true") {
                    flag = true;
                    $("#txtCheckCodeTip2").removeClass("onFocus").addClass("onCorrect").html("OK!");
                }else if (data == "false") {
                    codeError();
                }
            },
            onWait: "正在校验验证码，请稍候..."
        });
    }
</script>

</body>
</html>
