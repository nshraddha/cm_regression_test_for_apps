<%@ include file="../common/taglib.jsp" %>
<META CONTENT="report-login1-status1" name="menu">
<h3><s:text name="label.user.login.logout.details"/></h3>

<div class="notification yui3-u-1">
    <s:if test="hasActionErrors()">
        <div class="error">
            <s:actionerror/>
        </div>
    </s:if>
    <s:if test="hasActionMessages()">
        <div class="success">
            <s:actionmessage/>
        </div>
    </s:if>
</div>
<div class="yui3-u-1">
    <s:if test='userLoginLogoutDetailsList!=null'>
        <div class="yui3-g row">
            <div class="yui3-u-3-5">&nbsp;</div>
            <div class="yui3-u-3-19"><label for="operator_id_for_login_status"><s:text
                    name="label.select.operator"/></label></div>
            <div class="yui3-u-1-24">:</div>
            <div class="yui3-u-1-5">
                <select name="operatorId" id="operator_id_for_login_status" class="required">
                    <option value="0">All Operator</option>
                    <s:iterator value="operatorList" var="operator">
                        <option value="<s:property value="#operator.id"/>"
                                <s:if test="#operator.id == operatorId">selected</s:if>><s:property
                                value="#operator.name"/></option>
                    </s:iterator>
                </select>
            </div>
        </div>
        <table class="row" cellspacing="0" cellpadding="5" width="100%">
            <tr class="title">
                <th>
                    <s:text name="label.name"/>
                </th>
                <th>
                    <s:text name="label.device.id"/>
                </th>
                <th>
                    <table cellspacing="0" cellpadding="2" width="100%">
                        <tr>
                            <th colspan="2" align="center" style="text-align: center!important;">
                                <s:text name="label.oddsify.details"/>
                            </th>
                            <option value="0">All Operator</option>
                            <option value="0">All Operator</option>
                        </tr>
                        <tr>
                            <th class="yui3-u-11-24">
                                <s:text name="label.login.time"/>
                            </th>
                            <th class="yui3-u-11-24">
                                <s:text name="label.logout.time"/>
                            </th>
                        </tr>
                    </table>
                </th>
                <th>
                    <table cellspacing="0" cellpadding="2" width="100%">
                        <tr>
                            <th colspan="3" align="center" style="text-align: center!important;">
                                <s:text name="label.operator.details"/>
                            </th>
                        </tr>
                        <tr>
                            <th class="yui3-u-7-24">
                                <s:text name="label.operator.name"/>
                            </th>
                            <th class="yui3-u-7-24">
                                <s:text name="label.login.time"/>
                            </th>
                            <th class="yui3-u-7-24">
                                <s:text name="label.logout.time"/>
                            </th>
                        </tr>
                    </table>
                </th>
            </tr>
            <s:if test="userLoginLogoutDetailsList.size != 0">
                <s:iterator value="userLoginLogoutDetailsList" var="userLoginLogoutDetails" status="itStatus">
                    <tr>
                        <td>
                            <s:property value="#userLoginLogoutDetails.fullName"/>
                        </td>
                        <td>
                            <s:property value="#userLoginLogoutDetails.deviceId"/>
                        </td>
                        <td>
                            <table cellspacing="0" cellpadding="2" width="100%">
                                <tr>
                                    <td class="yui3-u-11-24" style="word-break: normal!important;">
                                        <s:property value="#userLoginLogoutDetails.loginTime"/>
                                    </td>
                                    <td class="yui3-u-11-24" style="word-break: normal!important;">
                                        <s:property value="#userLoginLogoutDetails.logoutTime"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td>
                            <table cellspacing="0" cellpadding="2" width="100%">
                                <s:iterator value="#userLoginLogoutDetails.userReportWithOpList"
                                            var="userReportWithOp">
                                    <tr>
                                        <td class="yui3-u-7-24" style="word-break: normal!important;">
                                            <s:property value="#userReportWithOp.operatorName"/>
                                        </td>
                                        <td class="yui3-u-7-24" style="word-break: normal!important;">
                                            <s:property value="#userReportWithOp.loginTime"/>
                                        </td>
                                        <td class="yui3-u-7-24" style="word-break: normal!important;">
                                            <s:property value="#userReportWithOp.logoutTime"/>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </table>
                        </td>
                    </tr>
                </s:iterator>
            </s:if>
            <s:else>
                <tr>
                    <td colspan="5" align="center">
                        <s:text name="message.no.records.found"/>
                    </td>
                </tr>
            </s:else>
        </table>
    </s:if>
</div>