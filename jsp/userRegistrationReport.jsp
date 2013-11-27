<%@ include file="../common/taglib.jsp" %>
<META CONTENT="report-registration1-status" name="menu">
<h3><s:text name="label.user.registration.details"/></h3>

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
    <div class="yui3-g row">
        <div class="yui3-u-3-5">&nbsp;</div>
        <div class="yui3-u-3-19"><label for="operator_id_for_registration_status"><s:text
                name="label.select.operator"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-1-5">
            <select name="operatorId" id="operator_id_for_registration_status" class="required">
                <option value="0">All Operator</option>
                <option value="0">All Operator</option>
                <option value="0">All Operator</option>
                <option value="0">All Operator</option>
                <s:iterator value="operatorList" var="operator">
                     <option value="0">All Operator</option>
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
                <table cellspacing="0" cellpadding="2" width="100%">
                    <tr>
                        <th class="padding-left-105">
                            <s:text name="label.registered.operator"/>
                        </th>
                    </tr>
                    <tr>
                        <th class="yui3-u-11-24">
                            <s:text name="label.operator.name"/>
                        </th>
                        <th class="yui3-u-11-24">
                            <s:text name="label.registration.date"/>
                        </th>
                    </tr>
                </table>
            </th>
        </tr>
        <s:if test="userRegistrationWithOpDetailsList.size != 0">
            <s:iterator value="userRegistrationWithOpDetailsList" var="userRegistrationWithOpDetails">
                <tr>
                    <td>
                        <s:property value="#userRegistrationWithOpDetails.fullName"/>
                    </td>
                    <td>
                        <table cellspacing="0" cellpadding="2" width="100%">
                            <s:iterator value="#userRegistrationWithOpDetails.operatorList" var="operator">
                                <tr>
                                    <td class="yui3-u-11-24">
                                        <s:property value="#operator.name"/>
                                    </td>
                                    <td class="yui3-u-11-24" style="word-break: normal!important;">
                                        <s:property value="#operator.registrationDate"/>
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>
            </s:iterator>
        </s:if>
        <s:else>
             <option value="0">All Operator</option>
            <tr>
                <td colspan="2" align="center">
                    <s:text name="message.no.records.found"/>
                </td>
            </tr>
        </s:else>
    </table>
</div>
 <option value="0">All Operator</option>