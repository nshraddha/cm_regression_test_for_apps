<%@ include file="../common/taglib.jsp" %>
<META CONTENT="report-registration1-data" name="menu">
<h3><s:text name="label.operator.registration.data"/></h3>

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
<s:if test='#operatorField.fieldIdentifier=="register"'>
<s:if test='#operatorField.opRegCommonFieldList!=null'>
    <div class="yui3-u-1 row">
        <h4><s:text name="label.common.field"/>:</h4>
    </div>
    <div class="yui3-u-1">
        <table class="row" cellspacing="0" cellpadding="5" width="100%">
            <tr class="title">
                <th>
                    Param
                </th>
                <th>
                    Key
                </th>
                <th>
                    ParamFormat
                </th>
            </tr>
            <s:iterator value="#operatorField.opRegCommonFieldList" var="opRegCommonField">
                <tr>
                    <td>
                        <s:property value="#opRegCommonField.param"/>
                    </td>
                    <td>
                        <s:property value="#opRegCommonField.key"/>
                    </td>
                    <td>
                        <s:property value="#opRegCommonField.paramFormat"/>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </div>
</s:if>
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
<s:if test='#operatorField.opRegHtmlFieldList!=null'>
    <div class="yui3-u-1 row">
        <h4>Operator Html Field:</h4>
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
                    Title
                </th>
                <th>
                    Key
                </th>
                <th>
                    Param Format
                </th>
                <th>
                    Required
                </th>
            </tr>
            <s:iterator value="#operatorField.opRegHtmlFieldList" var="opRegHtmlField">
                <tr>
                    <td>
                        <s:property value="#opRegHtmlField.param"/>
                    </td>
                    <td>
                        <s:property value="#opRegHtmlField.tag"/>
                    </td>
                    <td>
                        <s:property value="#opRegHtmlField.attr"/>
                    </td>
                    <td>
                        <s:property value="#opRegHtmlField.attrValue"/>
                    </td>
                    <td>
                        <s:property value="#opRegHtmlField.title"/>
                    </td>
                    <td>
                        <s:property value="#opRegHtmlField.key"/>
                    </td>
                    <td>
                        <s:property value="#opRegHtmlField.paramFormat"/>
                    </td>
                    <td>
                        <s:property value="#opRegHtmlField.required"/>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </div>
</s:if>
<s:if test='#operatorField.opRegValidatorFieldList!=null'>
    <div class="yui3-u-1 row">
        <h4>Operator Validator Field:</h4>
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
                    Key
                </th>
                <th>
                    Required
                </th>
            </tr>
            <s:iterator value="#operatorField.opRegValidatorFieldList" var="opRegValidatorField">
                <tr>
                    <td>
                        <s:property value="#opRegValidatorField.param"/>
                    </td>
                    <td>
                        <s:property value="#opRegValidatorField.tag"/>
                    </td>
                    <td>
                        <s:property value="#opRegValidatorField.attr"/>
                    </td>
                    <td>
                        <s:property value="#opRegValidatorField.attrValue"/>
                    </td>
                    <td>
                        <s:property value="#opRegValidatorField.key"/>
                    </td>
                    <td>
                        <s:property value="#opRegValidatorField.required"/>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </div>
</s:if>
<s:if test='#operatorField.opRegSuccessFieldList!=null'>
    <div class="yui3-u-1 row">
        <h4>Operator Success Field:</h4>
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
            <s:iterator value="#operatorField.opRegSuccessFieldList" var="opRegSuccessField">
                <tr>
                    <td>
                        <s:property value="#opRegSuccessField.tag"/>
                    </td>
                    <td>
                        <s:property value="#opRegSuccessField.attr"/>
                    </td>
                    <td>
                        <s:property value="#opRegSuccessField.attrValue"/>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </div>
</s:if>
<s:if test='#operatorField.opRegErrorFieldList!=null'>
    <div class="yui3-u-1 row">
        <h4>Operator Error Field:</h4>
        <h4>Operator Error Field:</h4>
        <h4>Operator Error Field:</h4>
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
        
        <h4>Operator Error Field:</h4>
        <h4>Operator Error Field:</h4>
    </div>
</div>

<h4>Operator Dynamic Field:</h4>
<th>
</th>