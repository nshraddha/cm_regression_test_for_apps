<%@ include file="../common/taglib.jsp" %>
<META CONTENT="report-login1-data1" name="menu">
<h3><s:text name="label.operator.login.data"/></h3>

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
<div class="yui3-g" id="operator_report_data">
    <s:iterator value="operatorList" var="operator" status="itStatus">
        <div class="display_none yui3-u-1" id="operator_<s:property value="#operator.id"/>">
            <div class="yui3-u-1 row">
                <div class="yui3-u-1 row">
                    <div class="yui3-u-1-4"><label><s:text name="label.operator"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8"><s:property value="#itStatus.count"/></div>
                </div>
                <div class="yui3-u-1 row">
                    <div class="yui3-u-1-4"><label><s:text name="operator.name"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:property value="#operator.name"/>
                    </div>
                </div>
                <div class="yui3-u-1 row">
                    <div class="yui3-u-1-4"><label><s:text name="label.registration.page.url"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:property value="#operator.registrationPageUrl"/>
                    </div>
                </div>
                <div class="yui3-u-1 row">
                    <div class="yui3-u-1-4"><label><s:text name="label.registration.url"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:property value="#operator.registrationUrl"/>
                    </div>
                </div>
                <div class="yui3-u-1 row">
                    <div class="yui3-u-1-4"><label><s:text name="label.login.page.url"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:property value="#operator.LoginPageUrl"/>
                    </div>
                </div>
                <div class="yui3-u-1 row">
                    <div class="yui3-u-1-4"><label><s:text name="label.login.url"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:property value="#operator.LoginUrl"/>
                    </div>
                </div>
                <div class="yui3-u-1 row">
                    <div class="yui3-u-1-4"><label><s:text name="label.date.format"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:property value="#operator.dateFormat"/>
                    </div>
                </div>
                <div class="yui3-u-1 row">
                    <div class="yui3-u-1-4"><label><s:text name="label.method.type"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:property value="#operator.metaData"/>
                    </div>
                </div>
            </div>
            <div class="yui3-u-1 row">
                <s:iterator value="#operator.operatorFieldList" var="operatorField">
                    <s:if test='#operatorField.fieldIdentifier=="login"'>
                        <s:if test='#operatorField.opRegDynamicFieldList!=null'>
                            <div class="yui3-u-1 row">
                                <h4>Operator Dynamic Field:</h4>
                            </div>
                            <div class="yui3-u-1">
                                <table class="row" cellspacing="0" cellpadding="5" width="100%">
                                    <tr class="title">
                                        <th>
                                            Param
                                        </th>
                                        <th>
                                            Type
                                        </th>
                                        <th>
                                            Title
                                        </th>
                                        <th>
                                            Required
                                        </th>
                                    </tr>
                                    <s:iterator value="#operatorField.opRegDynamicFieldList" var="opRegDynamicField">
                                        <tr>
                                            <td>
                                                <s:property value="#opRegDynamicField.param"/>
                                            </td>
                                            <td>
                                                <s:property value="#opRegDynamicField.type"/>
                                            </td>
                                            <td>
                                                <s:property value="#opRegDynamicField.title"/>
                                            </td>
                                            <td>
                                                <s:property value="#opRegDynamicField.required"/>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </table>
                            </div>
                        </s:if>
                        <s:if test='#operatorField.opRegHiddenFieldList!=null'>
                            <div class="yui3-u-1 row">
                                <h4>Operator Hidden Field:</h4>
                            </div>
                            <div class="yui3-u-1">
                                <table class="row" cellspacing="0" cellpadding="5" width="100%">
                                    <tr class="title">
                                        <th>
                                            Param
                                        </th>
                                        <th>
                                            Tag
                                        </th>
                                        <th>
                                            Attribute
                                        </th>
                                        <th>
                                            Attribute Value
                                        </th>
                                        <th>
                                            Required
                                        </th>
                                    </tr>
                                    <s:iterator value="#operatorField.opRegHiddenFieldList" var="opRegHiddenField">
                                        <tr>
                                            <td>
                                                <s:property value="#opRegHiddenField.param"/>
                                            </td>
                                            <td>
                                                <s:property value="#opRegHiddenField.tag"/>
                                            </td>
                                            <td>
                                                <s:property value="#opRegHiddenField.attr"/>
                                            </td>
                                            <td>
                                                <s:property value="#opRegHiddenField.attrValue"/>
                                            </td>
                                            <td>
                                                <s:property value="#opRegHiddenField.required"/>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </table>
                            </div>
                        </s:if>
                        <s:if test='#operatorField.opRegErrorFieldList!=null'>
                            <div class="yui3-u-1 row">
                                <h4>Operator Error Field:</h4>
                            </div>
                            <div class="yui3-u-1">
                                <table class="row" cellspacing="0" cellpadding="5" width="100%">
                                    <tr class="title">
                                        <th>
                                            Tag
                                        </th>
                                        <th>
                                            Attribute
                                        </th>
                                        <th>
                                            Attribute Value
                                        </th>
                                    </tr>
                                    <s:iterator value="#operatorField.opRegErrorFieldList" var="opRegErrorField">
                                        <tr>
                                            <td>
                                                <s:property value="#opRegErrorField.tag"/>
                                            </td>
                                            <td>
                                                <s:property value="#opRegErrorField.attr"/>
                                            </td>
                                            <td>
                                                <s:property value="#opRegErrorField.attrValue"/>
                                            </td>
                                        </tr>
                                    </s:iterator>
                                </table>
                            </div>
                        </s:if>
                    </s:if>
                </s:iterator>
            </div>
        </div>
    </s:iterator>
</div>
<div class="yui3-g">
    <div class="yui3-u-1-2">&nbsp;</div>
    <div class="yui3-u-1-2">
        <div class="yui3-u-7-8">&nbsp;</div>
        <div id="prev" class="yui3-u-1-24 ui-icon ui-icon-prev" title="Previous">&nbsp;</div>
        <div id="next" class="yui3-u-1-24 ui-icon ui-icon-next" title="Next">&nbsp;</div>
    </div>
</div>
