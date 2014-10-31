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
                        <div class="titlebt">短信记录</div>
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
        <form action="cardbill!userBillList.action" name="snumForm" method="post">

            <td width="17" height="31" valign="top" background="${stx}/images/mail_leftbg.gif">
                <img src="${stx}/images/left-top-right.gif" width="17" height="29"/>
            </td>
            <td height="31" valign="top" background="${stx}/images/content-bg.gif">
                <table width="100%" height="31" border="0" cellpadding="0" cellspacing="0" class="left_topbg" id="table3">
                    <tr>
                        <td class="left_txt" height="31" width="20%">
                            开始：
                            <input type="text"
                                   name='pageView.btime'
                                   onFocus="WdatePicker({startDate:'${pageView.btime}',
                                           dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})"
                                   value="${pageView.btime}"/>
                        </td>
                        <td class="left_txt" height="31" width="20%">
                            结束：
                            <input type="text"
                                   name='pageView.etime'
                                   onFocus="WdatePicker({startDate:'${pageView.etime}',
                                           dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})"
                                   value="${pageView.etime}" />
                        </td>
                        <td class="left_txt" align="left" style="color: red; padding-right: 5px;">
                            <input type="submit" value="查询" />
                        </td>
                        <td class="left_txt" align="right">
                        </td>
                    </tr>
                </table>
            </td>
            <td height="31" width="16" valign="top" background="${stx}/images/mail_rightbg.gif">
                <img src="${stx}/images/nav-right-bg.gif" width="16" height="29"/>
            </td>

        </form>
    </tr>
    <tr>
        <td height="20"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td height="20" valign="top" bgcolor="#F7F8F9">
            <b>短信信息：</b>
        </td>
        <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
    </tr>

    <tr>
        <td height="200"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td height="200" valign="top" bgcolor="#F7F8F9">

            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="2" valign="top">&nbsp;</td>
                    <td>&nbsp;</td>
                    <td valign="top">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" colspan="4">
                        <table width="100%" cellpadd
                               ing="0" class="Table1" border="1" cellspacing="0">
                            <tr height="20">
                                <th width="3%" class="left_bt2">ID</th>
                                <th width="8%" class="left_bt2">用户号码</th>
                                <th width="8%" class="left_bt2">长号码</th>
                                <th width="8%" class="left_bt2">指令</th>
                                <th width="8%" class="left_bt2">上行时间</th>
                                <th width="8%" class="left_bt2">状态报告时间</th>
                                <th width="8%" class="left_bt2">通道</th>
                                <th width="8%" class="left_bt2">状态</th>
                            </tr>
                            <s:iterator value="list" id="bean">
                                <tr class='t2'>
                                    <td>${bean.id}</td>
                                    <td>${bean.mobile}</td>
                                    <td>${bean.spnum}</td>
                                    <td>${bean.msg}</td>
                                    <td>${bean.btime}</td>
                                    <td>${bean.etime}</td>
                                    <td>${bean.sfid}</td>
                                    <td>${bean.flag}</td>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td colspan="15" align="right" ><div class="PageGoTo">${pageGoto}</div></td>
                </tr>
            </table>
        </td>
        <td background="${stx}/images/mail_rightbg.gif">&nbsp;</td>
    </tr>

    <tr>
        <td height="20"  valign="middle" background="${stx}/images/mail_leftbg.gif">&nbsp;</td>
        <td height="20" valign="top" bgcolor="#F7F8F9">
            <b>状态：（1:上行 2:状态报告 3:计费成功 4:预约外呼 5:外呼中 6:外呼成功 7:下发成功）</b>
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
