<%@ include file="../common/taglib.jsp" %>
<META CONTENT="operator-list_operator" name="menu">
<h3><s:text name="label.operator.list"/></h3>

<div class="notification yui3-u-1">
    <s:if test="hasActionErrors()">
        <div class="error">
            <s:actionerror/>
        </div>
        <div class="error">
            <s:actionerror/>
        </div>
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
    <s:if test='operatorList!=null'>
        <table class="row" cellspacing="0" cellpadding="5" width="100%">
            <tr class="title">
                <th>
                    <s:text name="label.name"/>
                </th>
                <th>
                    <s:text name="label.registration.page.url"/>
                </th>
                <th>
                    <s:text name="label.registration.url"/>
                </th>
                <th>
                    <s:text name="label.login.page.url"/>
                </th>
                <th>
                    <s:text name="label.login.url"/>
                </th>
                <th>
                    <s:text name="label.betting.url"/>
                </th>
                <th>
                    <s:text name="label.date.format"/>
                </th>
                <th>
                    <s:text name="label.method.type"/>
                </th>
                <th>
                    <s:text name="label.action"/>
                </th>

            </tr>
            <s:iterator value="operatorList" var="operator">
                <tr>
                    <td>
                        <s:property value="#operator.name"/>
                    </td>
                    <td>
                        <s:property value="#operator.registrationPageUrl"/>
                    </td>
                    <td>
                        <s:property value="#operator.registrationUrl"/>
                    </td>
                    <td>
                        <s:property value="#operator.loginPageUrl"/>
                    </td>
                    <td>
                        <s:property value="#operator.loginUrl"/>
                    </td>
                    <td>
                        <s:property value="#operator.bettingUrl"/>
                    </td>
                    <td>
                        <s:property value="#operator.dateFormat"/>
                    </td>
                    <td>
                        <s:property value="#operator.metaData"/>
                    </td>
                    <td>
                        <a href="editOperator?operatorId=<s:property value="#operator.id"/>">
                            <div class="yui3-u-11-24 ui-icon ui-icon-edit edit_operator" style="margin-bottom: 5px;"
                                 title='<s:text name="button.edit"/>'>&nbsp;</div>
                        </a>

                        <div id="delete-operator_<s:property value="#operator.id"/>"
                             class="yui3-u-11-24 ui-icon ui-icon-delete delete_operator" style="margin-bottom: 5px;"
                             title='<s:text name="button.delete"/>'>&nbsp;</div>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </s:if>
</div>