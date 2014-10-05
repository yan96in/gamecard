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
                        <div class="titlebt">话单统计</div>
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
        <form action="bill!list.action" name="snumForm" method="post">

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
                        <td class="left_txt" height="31" width="15%">
                            通道：
                            <s:select theme="simple" id="spid" name="pageView.spid" list="list2" listKey="id"
                                      listValue="name" headerKey="0" headerValue="全部" >
                            </s:select>
                        </td>
                        <td class="left_txt" height="31" width="15%">
                            渠道：
                            <s:select theme="simple" id="cpid" name="pageView.cpid" list="list3" listKey="id"
                                      listValue="showname" headerKey="0" headerValue="全部" >
                            </s:select>
                        </td>
                        <td class="left_txt" height="31" width="15%">
                            分组类型：
                            <s:select list="#{3:'通道',1:'日期',4:'渠道主帐号'}"
                                      name="pageView.group" label="abc" listKey="key" listValue="value" />
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
                                <th width="8%" class="left_bt2">类型(${pageView.message})</th>
                                <th width="8%" class="left_bt2">拨打次数</th>
                                <th width="8%" class="left_bt2">拨打人数</th>
                                <th width="8%" class="left_bt2">呼叫时长(秒)</th>
                                <th width="8%" class="left_bt2">信息费(元)</th>
                                <th width="8%" class="left_bt2">扣量费用(元)</th>
                                <th width="8%" class="left_bt2">结算费用(元)</th>
                                <th width="8%" class="left_bt2">扣量比</th>
                                <th width="8%" class="left_bt2">ARPU</th>
                            </tr>
                            <s:iterator value="list" id="bean">
                                <tr class='t2'>
                                    <td>${bean.group}</td>
                                    <td>${bean.counts}</td>
                                    <td>${bean.users}</td>
                                    <td>${bean.times}</td>
                                    <td>${bean.fees}</td>
                                    <td>${bean.limitfee}</td>
                                    <td>${bean.salefee}</td>
                                    <td>${bean.limitrate}</td>
                                    <td>${bean.arpu}</td>
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
<script type="text/javascript">
    function confirmdel(id) {
        if (confirm("确认删除?")) {
            location.href = "cpnum!delete.action?pageView.id=" + id;
        }
    }
    function confirmUpdate(id) {
        location.href = "cpnum!add.action?pageView.id=" + id ;
    }
</script>
