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
                            <div class="titlebt">分配指令</div>
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
                <form id="cpForm" name="cpForm" method="post" action="cpnum!doAdd.action" onsubmit="return check();">
                    <input type="hidden" name="pageView.id" value="${pageView.id}">
                    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                        <tr>
                            <td colspan="2" valign="top">&nbsp;</td>
                            <td>&nbsp;</td>
                            <td valign="top">&nbsp;</td>
                        </tr>
                        <tr>
                            <td align="center" colspan="4">
                                <table width="60%" cellpadd
                                       ing="0" class="Table1" border="1" cellspacing="0">
                                    <tr class='t2' height="20">
                                        <td>渠道：</td>
                                        <td>
                                            <div align="left">
                                                <div align="left">
                                                    <s:select theme="simple" id="cpid" name="pageView.cpid" list="list2" listKey="id"
                                                              listValue="showname" headerKey="0" headerValue="请选择" >
                                                    </s:select>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20">
                                        <td>长号码：</td>
                                        <td>
                                            <div align="left">
                                                <s:select theme="simple" id="called" name="pageView.called" list="list" listKey="called"
                                                          listValue="called" headerKey="" headerValue="请选择" >
                                                </s:select> <font color='red'>${pageView.message}</font>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20" style="display:none">
                                        <td>日上限：</td>
                                        <td>
                                            <div align="left">
                                                <input size="20" id="daylimit" name="pageView.daylimit" value="${pageView.daylimit}" />
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20" style="display:none">
                                        <td>月上限：</td>
                                        <td>
                                            <div align="left">
                                                <input size="20" id="monthlimit" name="pageView.monthlimit" value="${pageView.monthlimit}" />
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20" style="display:none">
                                        <td>扣量：</td>
                                        <td>
                                            <div align="left">
                                                <input size="20" id="deductRate" name="pageView.deductRate" value="${pageView.deductRate}" />%
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class='t2' height="20" style="display:none">
                                        <td>地市屏蔽信息：</td>
                                        <td>
                                            <div align="left">
                                                <input size="20" id="blackinfo" name="pageView.memo" value="${pageView.memo}" />
                                            </div>
                                        </td>
                                    </tr>

                                    <tr class='t2' height="25">
                                        <td></td>
                                        <td>
                                            <div align="left">
                                                <input type="submit" value="提交"/>&nbsp;<input type="reset" value="重置"/>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                    </table>
                </form>
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
<script type="text/javascript">
    function check(){
        var cpid = $("#cpid").val();
        if (cpid <= 1) {
            alert("请选择渠道");
            $("#cpid").focus();
            return false;
        }
        var called = $("#called").val();
        if (called == "") {
            alert("请选择长号码");
            $("#called").focus();
            return false;
        }

        $("#cpForm").submit();
    }
</script>
