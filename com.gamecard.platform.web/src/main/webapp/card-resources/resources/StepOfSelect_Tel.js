/// <reference path="../../js/jquery.js" />
/// <reference path="../../js/artDialog/jquery.artDialog.source.js" />

(function (config) {
    config['opacity'] = 0.1
})(art.dialog.defaults);

$(document).ready(function () {


    //表单验证初始化
    $.formValidator.initConfig({ formID: "form1", theme: "User", onSuccess: function () {
        var channelID = $("input:radio[name=payChannel]:checked").val();
        if (channelID == undefined) {
            $.dialog.alert("<font style=\"font-weight: bold; color: #990000; font-size: 14px;\">请先选择支付通道！</font>");
            return false;
        }
        var orderID = $("#curOrderID").val();
        //Ajax提交支付订单
        $("#btnOK").val("提交中...");
        $("#btnOK").attr("disabled", true);
        $.ajax({
            url: "/ajax/SubmitPayOrder.ashx",
            dataType: "json",
            type: "post",
            async: false,
            data: $("#form1").serialize(),
            success: function (data) {
                if (data.status == "0") {
                    $("#btnOK").val("提交支付");
                    $("#btnOK").removeAttr("disabled");

                    var bno = data.message;
                    var defChannelID = data.channelid;  //默认通道
                    window.location.href = "StepOfConfirm_Tel.shtml?bno=" + bno + "&chid=" + defChannelID;
                }
                else {
                    $.dialog.alert(data.message);
                    $("#btnOK").val("提交支付");
                    $("#btnOK").removeAttr("disabled");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $.dialog.alert("<font style=\"font-weight: bold; color: #990000; font-size: 14px;\">订单提交支付失败，可能服务器忙，请重试！</font><br />" + errorThrown);
                $("#btnOK").val("提交支付");
                $("#btnOK").removeAttr("disabled");
            }
        });
        return false;
    }
    });

    var operStr = " <h3>操作必读</h3>"
                    + "<ol>"
                        + "<li>输入支付号码：如号码归属地“未知”，则此号码不能完成支付，请与客服联系；</li>"
                        + "<li>选择支付方式：如选择的支付方式下无可用支付通道，请选择其它支付方式；</li>"
                        + "<li>选择支付通道：通常会有一个或多个支付通道，请根据每个支付通道标称的支付信息（支付费用、支付次数等），选择合适的支付通道进行提交；</li>"
                        + "<li>提交支付：系统会根据你提交的商品、支付号码、支付通道等信息处理支付请求，如您提交的支付请求受系统限制而不能支付，请选择其它支付方式或隔日重试；</li>"
                        + "<li>提交支付支付请求成功后，请按页面提示完成支付确认操作；</li>"
                        + "<li>其它任务疑问，请咨询客服热线：400-0909-571。</li>"
                    + "</ol>";

    //当页面加载完成后，绑定表单验证代码
    var channelClassID = $("#curChannelClassID").val();
    switch (channelClassID) {
        case "1011":
            CheckMobilePhone_yd();     //移动手机验证
            break;
        case "1021":
            CheckMobilePhone_dx();     //电信手机验证
            break;
        case "1022":
            CheckTelePhone_dx();       //电信固话验证
            break;
        case "1031":
        case "1036":
            CheckMobilePhone_lt();     //联通手机验证
            break;
        default:
            break;
    }


    //---------------------------------------
    //点击切换切换支付方式标签后操作
    //---------------------------------------
    $("#divPayTypeList>label").click(function () {
        if ($(this).attr("class") == "selected") return;    //如当前为选中状态则不处理
        //取当前支付方式值
        var curPayType = $(this).attr("ref");
        $("#curPayType").val(curPayType);
        //选中效果
        $(this).siblings().removeClass("selected").children("a").children("i").removeClass("icon-chk");
        $(this).addClass("selected").children("a").children("i:eq(1)").addClass("icon-chk");

        //从支付方式标签缓存数据中取扣费类型数据，并切换到当前扣费类型下
        var obj = $("#pt_" + $("#curPayType").val()).data("feeTypeData");   //取缓存数据
        if (obj == undefined) {
            $("#divFeeTypeList").html("");          //清空扣费类型信息
            $("#curFeeType").val("");               //清空当前扣费类型值
            $("#divChannelLimitInfo").html(operStr);     //清空当前通道限制信息
            $("#divNoChannelTip").children("div").html("当前支付方式下无可用支付通道，请选择其他方式！");    //替换提示信息
            $("#divNoChannelTip").show();   //显示提示信息
        }
        else {
            $("#divNoChannelTip").hide();                               //隐藏提示信息
            $("#payTypeIntro").html(obj.Type_Tip);                     //替换当前支付方式说明
            $("#divFeeTypeList").html(obj.FeeTypes[0].FeeType_Html);   //默认取第1个扣费类型信息
            $("#divFeeTypeList").show();                                //显示扣费类型
            var curChannelID = $("input:radio[name=payChannel]:checked").val();
            GetChannelLimitInfo(curChannelID);                          //取通道限制信息
        }

    });

    //---------------------------------------
    //点击切换扣费类型标签后的操作
    //---------------------------------------
    $("#divFeeTypeList>label").live("click", function () {
        if ($(this).attr("class") == "selected") return;     //如当前为选中状态则不处理
        //取当前扣费类型值
        var curFeeType = $(this).attr("ref");
        $("#curFeeType").val(curFeeType);
        //选中效果
        $(this).siblings().removeClass("selected");
        $(this).addClass("selected");

        //从对应的支付方式标签缓存数据中取扣费类型数据，并切换到当前扣费类型下
        var curPayTypeID = $("#curPayType").val();              //当前支付方式
        var curFeeTypeID = $("#curFeeType").val();              //当前扣费类型
        //alert(curFeeTypeID);
        var obj = $("#pt_" + curPayTypeID).data("feeTypeData");    //取缓存数据
        if (obj == undefined) {
            $("#divFeeTypeList").html("");          //清空扣费类型信息
            $("#curFeeType").val("");               //清空当前扣费类型值
            $("#divChannelLimitInfo").html(operStr);     //清空当前通道限制信息
            $("#divNoChannelTip").children("div").html("当前支付方式下无可用支付通道，请选择其他方式！");    //替换提示信息
            $("#divNoChannelTip").show();   //显示提示信息
        }
        else {
            $.each(obj.FeeTypes, function (index, item) {
                if (item.Channel_Flag == curFeeTypeID) {
                    $("#divFeeTypeList").html(item.FeeType_Html);   //替换当前扣费类型信息
                    //alert(obj.FeeTypes[0].FeeType_Html);
                    var curChannelID = $("input:radio[name=payChannel]:checked").val();
                    GetChannelLimitInfo(curChannelID);  //取通道限制信息
                    return false;
                }
            });
        }
    });


    //---------------------------------------
    //点击切换切换通道radio后操作
    //---------------------------------------
    $("#divPayChannelList>ul>li").live("click", function () {

        if ($(this).attr("class") == "selected") return;    //如当前为选中状态则不处理

        //选中效果
        $(this).siblings().removeClass("selected");
        $(this).addClass("selected");

        //选中通道
        var curChannelID = $(this).attr("ref");
        //取通道限制信息
        GetChannelLimitInfo(curChannelID);

    });


    //---------------------------------------
    //当更改号码时，初始化各种参数
    //---------------------------------------
    $("#txtPayPhone").keydown(function () {

        $("label[id^='pt_']").removeData("feeTypeData");        //清除扣费信息缓存数据
        $("label[id^='chc_']").removeData("channelLimitData");   //清除通道限制信息缓存数据

        $("#pPayPhoneArea").hide();             //隐藏号码归属地
        $("#curPhoneAreaID").val("");           //清空当前地区ID值
        $("#divFeeTypeList").html("");          //清空扣费类型信息
        $("#curFeeType").val("");               //清空当前扣费类型值
        $("#divChannelLimitInfo").html(operStr);     //清空当前通道限制信息

        $("#divNoChannelTip").children("div").html("请先输入支付号码，然后再获取可用支付通道！");    //替换提示信息
        $("#divNoChannelTip").show();           //显示提示信息

    });

    //---------------------------------------
    //当支付号码框失去焦点时取可支付通道信息
    //---------------------------------------
    $("#txtPayPhone").blur(function () {

        if ($.formValidator.isOneValid("txtPayPhone") == false) return;  //如果号码没有验证通道，则返回

        //取通道信息
        var phoneNo = $(this).val();
        var orderID = $("#curOrderID").val();
        var channelClassID = $("#curChannelClassID").val();
        var payTypeID = $("#curPayType").val();

        $.ajax({
            url: "/ajax/GetPayTypeInfo.ashx",
            dataType: "json",
            type: "post",
            async: false,
            data: { phone: phoneNo, oid: orderID, chcid: channelClassID },
            success: function (data) {
                payTypeDataObj = data;
                //号码归属地
                $("#curPhoneAreaID").val(data.PhoneInfo.City_ID);
                $("#pPayPhoneArea").show().html("号码所属：<strong>" + data.PhoneInfo.AreaName + "</strong>");

                if (data.Status == 0) {
                    //取通道成功
                    $("#divNoChannelTip").hide();       //隐藏提示信息
                    var isIncludeChannel = false;
                    $.each(data.PayTypes, function (index, item) {
                        $("#pt_" + item.Channel_Flag).data("feeTypeData", item);        //将扣费信息缓存到对应的支付方式标签上，以便后面直接使用
                        //显示当前支付方式下的信息
                        if (item.Channel_Flag == payTypeID) {
                            $("#payTypeIntro").html(item.Type_Tip);                         //替换当前支付方式说明
                            isIncludeChannel = true;
                            $("#divFeeTypeList").html(item.FeeTypes[0].FeeType_Html);   //默认取第1个扣费类型信息
                            $("#divFeeTypeList").show();                                //显示扣费类型  
                        }
                    });
                    //如果当前支付方式下没有可用通道，则提示
                    if (isIncludeChannel == false) {
                        $("#divNoChannelTip").children("div").html("当前支付方式下无可用支付通道，请选择其他方式！");    //替换提示信息
                        $("#divNoChannelTip").show();   //显示提示信息
                    }

                    $("#chc_" + channelClassID).data("channelLimitData", data.Channels);  //将通道限制信息缓存到对的通道分类标签上，以便全面直接使用
                    var curChannelID = $("input:radio[name=payChannel]:checked").val();
                    GetChannelLimitInfo(curChannelID);  //取通道限制信息
                }
                else {
                    //取通道失败的处理
                    $("#divFeeTypeList").html("");          //清空扣费类型信息
                    $("#curFeeType").val("");               //清空当前扣费类型值
                    $("#divChannelLimitInfo").html(operStr);     //清空当前通道限制信息
                    $("#divNoChannelTip").children("div").html(data.Status_Msg);    //替换提示说明
                    $("#divNoChannelTip").show();       //显示提示信息
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $.dialog.alert("<font style=\"font-weight: bold; color: #990000; font-size: 14px;\">获取支付通道信息失败，可能服务器忙，请重试！</font><br />" + errorThrown);
            }
        });

    });


    if ($("#txtPayPhone").val() != "") {
        $("#txtPayPhone").trigger("blur");
    }

});


//----------------------------
//移动手机号码验证
//----------------------------
function CheckMobilePhone_yd() {

    //取通道信息
    $("#txtPayPhone").formValidator({ onShow: " ", onFocus: "请输入移动手机号码", onCorrect: "OK!" })
                    .inputValidator({ min: 11, max: 11, onError: "请输入必须11位手机号码", empty: { leftEmpty: false, rightEmpty: false, emptyError: "手机号码不能为空!"} })
                    .regexValidator({ regExp: "mobile_yd", dataType: "enum", onError: "输入的不是移动手机号码" });
}

//----------------------------
//电信手机号码验证
//----------------------------
function CheckMobilePhone_dx() {
    $("#txtPayPhone").formValidator({ onShow: " ", onFocus: "请输入电信手机号码", onCorrect: "OK!" })
                    .inputValidator({ min: 11, max: 11, onError: "请输入必须11位手机号码", empty: { leftEmpty: false, rightEmpty: false, emptyError: "手机号码不能为空!"} })
                    .regexValidator({ regExp: "mobile_dx", dataType: "enum", onError: "输入的不是电信手机号码" }); ;
}
//----------------------------
//固定电话号码验证
//----------------------------
function CheckTelePhone_dx() {
    $("#txtPayPhone").formValidator({ onShow: " ", onFocus: "请输入电话号码（须带区号）", onCorrect: "OK!" })
                    .inputValidator({ min: 11, max: 12, onError: "请输入正确的电话号码（区号+号码）", empty: { leftEmpty: false, rightEmpty: false, emptyError: "电话号码不能为空!"} })
                    .regexValidator({ regExp: "tel_dx", dataType: "enum", onError: "电话号码格式不正确（区号+号码）" }) ;
}
//----------------------------
//联通手机号码验证
//----------------------------
function CheckMobilePhone_lt() {
    $("#txtPayPhone").formValidator({ onShow: " ", onFocus: "请输入联通手机号码", onCorrect: "OK!" })
                    .inputValidator({ min: 11, max: 11, onError: "请输入必须11位手机号码", empty: { leftEmpty: false, rightEmpty: false, emptyError: "手机号码不能为空!"} })
                    .regexValidator({ regExp: "mobile_lt", dataType: "enum", onError: "输入的不是联通手机号码" }); ;
}

//----------------------------
//从缓存中取对应的通道限制信息
//----------------------------
function GetChannelLimitInfo(curChannelID) {
    var channelClassID = $("#curChannelClassID").val();
    var obj = $("#chc_" + channelClassID).data("channelLimitData");    //取缓存数据
    if (obj == undefined) return;

    $.each(obj, function (index, item) {
        if (item.Channel_ID == curChannelID) {
            $("#divChannelLimitInfo").html(item.Channel_Limit_Html);       //取对应的通道限制信息
            $("#divChannelLimitInfo").show();       //显示通道限制信息
            return false;
        }
    });

}