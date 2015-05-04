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
                        <div class="titlebt">PC点卡记录</div>
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
        <form action="pccard!userCardList.action" name="snumForm" method="post">

            <td width="17" height="31" valign="top" background="${stx}/images/mail_leftbg.gif">
                <img src="${stx}/images/left-top-right.gif" width="17" height="29"/>
            </td>
            <td height="31" valign="top" background="${stx}/images/content-bg.gif">
                <table width="100%" height="31" border="0" cellpadding="0" cellspacing="0" class="left_topbg" id="table3">
                    <tr>
                        <td class="left_txt" height="31">
                            用户号码：
                            <input type="text"
                                   name='pageView.caller'
                                   value="${pageView.caller}"/>
                        </td>
                        <td class="left_txt" height="31">
                            开始：
                            <input type="text"
                                   name='pageView.btime'
                                   onFocus="WdatePicker({startDate:'${pageView.btime}',
                                           dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})"
                                   value="${pageView.btime}"/>
                        </td>
                        <td class="left_txt" height="31">
                            结束：
                            <input type="text"
                                   name='pageView.etime'
                                   onFocus="WdatePicker({startDate:'${pageView.etime}',
                                           dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})"
                                   value="${pageView.etime}" />
                        </td>
                        <td class="left_txt" height="31">
                            状态：
                            <s:select list="#{0:'全部', 2:'取卡成功'}"
                                      name="pageView.type" label="abc" listKey="key" listValue="value" />
                        </td>
                        <td class="left_txt" height="31">
                            渠道类型：
                            <s:select list="#{0:'全部', 19:'移动', 20:'联通'}"
                                      name="pageView.spid" label="abc" listKey="key" listValue="value" />
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
            <b>PC 取卡信息：</b>
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
                                <th width="5%" class="left_bt2">渠道类型</th>
                                <th width="8%" class="left_bt2">用户号码</th>
                                <th width="5%" class="left_bt2">省市</th>
                                <th width="5%" class="left_bt2">信息费(分)</th>
                                <th width="5%" class="left_bt2">点卡类型</th>
                                <th width="8%" class="left_bt2">卡号-密码</th>
                                <th width="12%" class="left_bt2">时间</th>
                                <th width="12%" class="left_bt2">错误码</th>
                                <th width="3%" class="left_bt2">状态</th>
                            </tr>
                            <s:iterator value="list" id="bean">
                                <tr class='t2'>
                                    <td>${bean.id}</td>
                                    <td><s:if test="#bean.channelid==19"><font color="red">移动</font></s:if><s:else><font color="green">联通</font></s:else></td>
                                    <td>${bean.mobile}</td>
                                    <td>${bean.province}-${bean.city}</td>
                                    <td>${bean.fee}</td>
                                    <td>${bean.cardShowName}</td>
                                    <td>${bean.cardno}-${bean.cardpwd}</td>
                                    <td>
                                        ${bean.btime}<br>
                                        ${bean.btime}
                                    </td>
                                    <td>${bean.resultcode}</td>
                                    <td>${bean.status}</td>
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
            <b>状态：（1:申请指令 2:成功获得点卡 3:计费成功取卡失败）</b>
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
