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
                        <div class="titlebt">分省统计</div>
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
                    <td>&nbsp;</td>
                    <td valign="top">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" colspan="4">
                        <table width="100%" cellpadd
                               ing="0" class="Table1" border="1" cellspacing="0">
                            <tr height="20">
                                <th width="8%" class="left_bt2">省份</th>
                                <th width="8%" class="left_bt2">数量</th>
                                <th width="8%" class="left_bt2">等待外呼</th>
                                <th width="8%" class="left_bt2">外呼中</th>
                                <th width="8%" class="left_bt2">外呼成功</th>
                                <th width="8%" class="left_bt2">下发成功</th>
                            </tr>
                            <s:iterator value="list" id="bean">
                                <tr class='t2'>
                                    <td>${bean.province}</td>
                                    <td>${bean.count}</td>
                                    <td>${bean.count4}</td>
                                    <td>${bean.count5}</td>
                                    <td>${bean.count6}</td>
                                    <td>${bean.count7}</td>
                                </tr>
                            </s:iterator>
                        </table>
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
