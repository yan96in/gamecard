<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
    <link href="${stx}/css/skin.css" rel="stylesheet" type="text/css"/>
    <script src="${stx}/js/jquery-1.7.1.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
</head>

<body class="body">
<table width="100%" height="95%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td width="17" height="31" valign="top" background="${stx}/images/mail_leftbg.gif">
            <img src="${stx}/images/left-top-right.gif" width="17" height="29"/>
        </td>
        <td height="31" valign="top" background="${stx}/images/content-bg.gif">
            <table width="100%" height="31" border="0" cellpadding="0" cellspacing="0" class="left_topbg" id="table2">
                <tr>
                    <td height="31" width="100">
                        <div class="titlebt">录入点卡</div>
                    </td>
                    <td class="left_txt" align="left" style="color: red; padding-right: 5px;">
                        <a href="javascript:history.back(-1);" style="color: red;">返回上一级</a>
                    </td>
                </tr>
            </table>
        </td>
        <td height="31" width="16" valign="top" background="${stx}/images/mail_rightbg.gif">
            <img src="${stx}/images/nav-right-bg.gif" width="16" height="29"/>
        </td>
    </tr>
    <tr>
        <td height="200"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td height="200" valign="top" bgcolor="#F7F8F9">

            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="2" valign="top">&nbsp;</td>
                    <td><font color="red">${message}</font>&nbsp;</td>
                    <td valign="top">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" colspan="4">
                        <s:form action="doUpload.action" id="form1" method="POST" enctype="multipart/form-data">
                            <table width="60%" cellpadd
                                   ing="0" class="Table1" border="1" cellspacing="0">
                                <tr height="20">
                                    <td width="8%" class="left_bt2">游戏点卡:
                                        <s:select theme="simple" id="id" name="id" list="list" listKey="id"
                                                  listValue="name" headerKey="0" headerValue="请选择" onchange="getPrice(this);">
                                        </s:select>
                                    </td>
                                    <td width="8%" class="left_bt2">
                                        <div id="pricediv" style="display: none">选择面值:
                                            <select name="priceId" id="priceId">
                                                <option>请选择
                                                </option>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr height="20">
                                    <td width="8%" class="left_bt2">选择文件:
                                        <s:file name="myFile" label="" />
                                    </td>
                                    <td width="8%" class="left_bt2" >
                                        <input type="submit" value="导入"> &nbsp; <input type="reset">
                                    </td>
                                </tr>
                            </table>
                        </s:form>
                    </td>
                </tr>
            </table>
        </td>
        <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
    </tr>
    <tr>
        <td height="220"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td height="220" valign="center" bgcolor="#F7F8F9">&nbsp;</td>
        <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
    </tr>
    <jsp:include page="/down.jsp"/>
</table>
</body>
</html>

<script src="${stx}/js/jquery-1.7.1.min.js"></script>
<script src="${stx}/js/My97DatePicker/WdatePicker.js"></script>


<script type="text/javascript">
    $(document).ready(function () {
        $("#form1").submit(function(){
            if($("#id").val() == 0){
                return false;
            }
            if($("#priceId").val() == 0){
                return false;
            }
        });
    });

    function getPrice(cardid){
        var cardid = $(cardid).val();
        var options = "<option value='0'>请选择</option>";
        if(cardid > 0){
            $.ajax({
                type: "GET",
                url: "${stx}/card/getPrices.action",
                data: {id:cardid},
                dataType: "json",
                success: function (data) {
                    if (data.flag) {
                        $(data.result).each(function(index,price){
                            options = options + "<option value='" + price.id + "'>" + price.description + "</option>";
                        });
                        $("#priceId").html(options);
                        $("#pricediv").show();
                    }else{
                        alert(data.msg);
                    }
                },
                onWait: "正在校验验证码，请稍候..."
            });
        } else {
            $("#priceId").html(options);
            $("#pricediv").hide();
        }
    }
</script>
