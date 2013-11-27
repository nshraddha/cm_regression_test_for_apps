<%@ include file="../common/taglib.jsp" %>
<META CONTENT="add_field-registration" name="menu">
<h3><s:text name="label.add.operator.registration.field"/></h3>

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
<div class="form_content yui3-u-1-2">
<s:form action="/admin/saveOperatorRegistrationField" theme="simple"
        id="save_operator_registration_field">
<div class="yui3-g row">
    <div class="yui3-u-1-3"><label for="registration_operator_id"><s:text name="label.select.operator"/></label></div>
    <div class="yui3-u-1-24">:</div>
    <div class="yui3-u-5-8">
        <select name="operatorId" id="registration_operator_id" class="required">
            <s:iterator value="operatorList" var="operator">
                <option value="<s:property value="#operator.id"/>"
                        <s:if test="#operator.id == operatorId">selected</s:if>><s:property
                        value="#operator.name"/></option>
            </s:iterator>
        </select>
    </div>
</div>
<div class="common_field update_field">
    <div class="yui3-g">
        <div class="yui3-u-23-24">
            <h4><s:text name="label.common.field"/></h4>
        </div>
        <div id="field-common" class="yui3-u-1-24 ui-icon ui-icon-add add_field" title='<s:text name="button.add"/>'>
            &nbsp;</div>
    </div>
    <s:if test='operatorField.opRegCommonFieldList!=null'>
        <s:iterator value="operatorField.opRegCommonFieldList" var="opRegCommonField" status="itStatus">
            <div id="common-field_<s:property value="#itStatus.index"/>" class="field input_block">
                <div class="yui3-g row">
                    <div class="yui3-u-23-24">
                        <div class="sub_beader"><s:text name="label.common.field"/> <span class="no"><s:property
                                value="#itStatus.count"/></span>:
                        </div>
                    </div>
                    <div id="delete-common-image_<s:property value="#opRegCommonField.id"/>"
                         class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>
                        &nbsp;</div>
                </div>
                <s:hidden name="opRegCommonField[%{#itStatus.index}].id" value="%{#opRegCommonField.id}"
                          class="common_hidden"></s:hidden>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="common_field_param-%{#itStatus.index}"><s:text
                            name="label.request.parameter"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegCommonField[%{#itStatus.index}].param"
                                     cssClass="required"
                                     id="common_field_param-%{#itStatus.index}"
                                     value="%{#opRegCommonField.param}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="common_field_key-%{#itStatus.index}"><s:text
                            name="label.table.column.name"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:select headerKey="" headerValue="Select Key"
                                  list="commonFieldKey"
                                  name="opRegCommonField[%{#itStatus.index}].key"
                                  cssClass="required"
                                  id="common_field_key-%{#itStatus.index}"
                                  value="%{#opRegCommonField.key}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="common_field_paramFormat-%{#itStatus.index}"><s:text
                            name="label.checkBox.value"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegCommonField[%{#itStatus.index}].paramFormat"
                                     id="common_field_paramFormat-%{#itStatus.index}"
                                     value="%{#opRegCommonField.paramFormat}"/>
                    </div>
                </div>
            </div>
        </s:iterator>
    </s:if>
</div>
<div class="dynamic_field update_field">
    <div class="yui3-g">
        <div class="yui3-u-23-24">
            <h4><s:text name="label.dynamic.field"/></h4>
        </div>
        <div id="field-dynamic" class="yui3-u-1-24 ui-icon ui-icon-add add_field" title='<s:text name="button.add"/>'>
            &nbsp;</div>
    </div>
    <s:if test='operatorField.opRegDynamicFieldList!=null'>
        <s:iterator value="operatorField.opRegDynamicFieldList" var="opRegDynamicField" status="itStatus">
            <div id="dynamic-field_<s:property value="#itStatus.index"/>" class="field input_block">
                <div class="yui3-g row">
                    <div class="yui3-u-23-24">
                        <div class="sub_beader"><s:text name="label.dynamic.field"/> <span class="no"><s:property
                                value="#itStatus.count"/></span>:
                        </div>
                    </div>
                    <div id="delete-dynamic-image_<s:property value="#opRegDynamicField.id"/>"
                         class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>
                        &nbsp;</div>
                        Required</label></div>
                </div>
                <s:hidden name="opRegDynamicField[%{#itStatus.index}].id" value="%{#opRegDynamicField.id}"
                          class="dynamic_hidden"></s:hidden>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="dynamic_field_param-%{#itStatus.index}"><s:text
                            name="label.request.parameter"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegDynamicField[%{#itStatus.index}].param"
                                     cssClass="required"
                                     id="dynamic_field_param-%{#itStatus.index}"
                                     value="%{#opRegDynamicField.param}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="dynamic_field_type-%{#itStatus.index}"><s:text
                            name="label.text.field.type"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegDynamicField[%{#itStatus.index}].type"
                                     cssClass="required"
                                     id="dynamic_field_type-%{#itStatus.index}"
                                     value="%{#opRegDynamicField.type}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="dynamic_field_title-%{#itStatus.index}"><s:text
                            name="label.place.holder"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegDynamicField[%{#itStatus.index}].title"
                                     cssClass="required"
                                     id="dynamic_field_title-%{#itStatus.index}"
                                     value="%{#opRegDynamicField.title}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="dynamic_field_required-%{#itStatus.index}">Is
                        Required</label></div>
                    <div class="yui3-u-1-24">:</div>
                    Required</label></div>
                    <div class="yui3-u-5-8">
                        <s:select headerKey="" headerValue="Select Value" list="booleanValue"
                                  name="opRegDynamicField[%{#itStatus.index}].required"
                                  cssClass="required"
                                  id="dynamic_field_required-%{#itStatus.index}"
                                  value="%{#opRegDynamicField.required}"/>
                                  Required</label></div>
                                  Required</label></div>
                                  
                    </div>
                </div>
            </div>
        </s:iterator>
    </s:if>
</div>
<div class="hidden_field update_field">
    <div class="yui3-g">
        <div class="yui3-u-23-24">
            <h4><s:text name="label.hidden.field"/></h4>
        </div>
        <div id="field-hidden" class="yui3-u-1-24 ui-icon ui-icon-add add_field" title='<s:text name="button.add"/>'>
            &nbsp;</div>
    </div>
    <s:if test='operatorField.opRegHiddenFieldList!=null'>
        <s:iterator value="operatorField.opRegHiddenFieldList" var="opRegHiddenField" status="itStatus">
            <div id="hidden-field_<s:property value="#itStatus.index"/>" class="field input_block">
                <div class="yui3-g row">
                    <div class="yui3-u-23-24">
                        <div class="sub_beader"><s:text name="label.hidden.field"/> <span class="no"><s:property
                                value="#itStatus.count"/></span>:
                        </div>
                    </div>
                    <div id="delete-hidden-image_<s:property value="#opRegHiddenField.id"/>"
                         class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>
                        &nbsp;</div>
                </div>
                <s:hidden name="opRegHiddenField[%{#itStatus.index}].id" value="%{#opRegHiddenField.id}"
                          class="hidden_hidden"></s:hidden>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="hidden_field_param-%{#itStatus.index}"><s:text
                            name="label.request.parameter"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHiddenField[%{#itStatus.index}].param"
                                     cssClass="required"
                                     id="hidden_field_param-%{#itStatus.index}"
                                     value="%{#opRegHiddenField.param}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="hidden_field_tag-%{#itStatus.index}"><s:text
                            name="label.html.tag.name"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHiddenField[%{#itStatus.index}].tag"
                                     cssClass="required"
                                     id="hidden_field_tag-%{#itStatus.index}" value="%{#opRegHiddenField.tag}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="hidden_field_attr-%{#itStatus.index}"><s:text
                            name="label.html.tag.attribute"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHiddenField[%{#itStatus.index}].attr"
                                     cssClass="required"
                                     id="hidden_field_attr-%{#itStatus.index}"
                                     value="%{#opRegHiddenField.attr}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="hidden_field_attrVal-%{#itStatus.index}"><s:text
                            name="label.html.attribute.value"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHiddenField[%{#itStatus.index}].attrValue"
                                     cssClass="required"
                                     id="hidden_field_attrVal-%{#itStatus.index}"
                                     value="%{#opRegHiddenField.attrValue}"/>
                    </div>
                </div>

            </div>
        </s:iterator>
    </s:if>
</div>
<div class="html_field update_field">
    <div class="yui3-g">
        <div class="yui3-u-23-24">
            <h4><s:text name="label.html.field"/></h4>
        </div>
        <div id="field-html" class="yui3-u-1-24 ui-icon ui-icon-add add_field" title='<s:text name="button.add"/>'>
            &nbsp;</div>
    </div>
    <s:if test='operatorField.opRegHtmlFieldList!=null'>
        <s:iterator value="operatorField.opRegHtmlFieldList" var="opRegHtmlField" status="itStatus">
            <div id="html-field_<s:property value="#itStatus.index"/>" class="field input_block">
                <div class="yui3-g row">
                    <div class="yui3-u-23-24">
                        <div class="sub_beader"><s:text name="label.html.field"/> <span class="no"><s:property
                                value="#itStatus.count"/></span>:
                        </div>
                    </div>
                    <div id="delete-html-image_<s:property value="#opRegHtmlField.id"/>"
                         class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>
                        &nbsp;</div>
                </div>
                <s:hidden name="opRegHtmlField[%{#itStatus.index}].id" value="%{#opRegHtmlField.id}"
                          class="html_hidden"></s:hidden>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="html_field_param-%{#itStatus.index}"><s:text
                            name="label.request.parameter"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHtmlField[%{#itStatus.index}].param"
                                     cssClass="required"
                                     id="html_field_param-%{#itStatus.index}" value="%{#opRegHtmlField.param}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="html_field_tag-%{#itStatus.index}"><s:text
                            name="label.html.tag.name"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHtmlField[%{#itStatus.index}].tag" cssClass="required"
                                     id="html_field_tag-%{#itStatus.index}"
                                     value="%{#opRegHtmlField.tag}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="html_field_attr-%{#itStatus.index}"><s:text
                            name="label.html.tag.attribute"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHtmlField[%{#itStatus.index}].attr" cssClass="required"
                                     id="html_field_attr-%{#itStatus.index}"
                                     value="%{#opRegHtmlField.attr}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="html_field_attrVal-%{#itStatus.index}"><s:text
                            name="label.html.attribute.value"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHtmlField[%{#itStatus.index}].attrValue"
                                     cssClass="required"
                                     id="html_field_attrVal-%{#itStatus.index}" value="%{#opRegHtmlField.attrValue}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="html_field_title-%{#itStatus.index}"><s:text
                            name="label.place.holder"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHtmlField[%{#itStatus.index}].title"
                                     cssClass="required"
                                     id="html_field_title-%{#itStatus.index}" value="%{#opRegHtmlField.title}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="html_field_key-%{#itStatus.index}">Type</label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:select headerKey="" headerValue="Select Value" list="type"
                                  name="opRegHtmlField[%{#itStatus.index}].key" cssClass="required"
                                  id="html_field_key-%{#itStatus.index}" value="%{#opRegHtmlField.key}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="html_field_paramFormat-%{#itStatus.index}"><s:text
                            name="label.checkBox.value"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegHtmlField[%{#itStatus.index}].paramFormat"
                                     cssClass="required"
                                     id="html_field_paramFormat-%{#itStatus.index}"
                                     value="%{#opRegHtmlField.paramFormat}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="html_field_required-%{#itStatus.index}"><s:text
                            name="label.is.required"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:select headerKey="" headerValue="Select Value" list="booleanValue"
                                  name="opRegHtmlField[%{#itStatus.index}].required"
                                  cssClass="required"
                                  id="html_field_required-%{#itStatus.index}" value="%{#opRegHtmlField.required}"/>
                    </div>
                </div>
            </div>
        </s:iterator>
    </s:if>
</div>
<div class="success_field update_field">
    <div class="yui3-g">
        <div class="yui3-u-23-24">
            <h4><s:text name="label.success.field"/></h4>
        </div>
        <div id="field-success" class="yui3-u-1-24 ui-icon ui-icon-add add_field" title='<s:text name="button.add"/>'>
            &nbsp;</div>
    </div>
    <s:if test='operatorField.opRegSuccessFieldList!=null'>
        <s:iterator value="operatorField.opRegSuccessFieldList" var="opRegSuccessField" status="itStatus">
            <div id="success-field_<s:property value="#itStatus.index"/>" class="field input_block">
                <div class="yui3-g row">
                    <div class="yui3-u-23-24">
                        <div class="sub_beader"><s:text name="label.success.field"/> <span class="no"><s:property
                                value="#itStatus.count"/></span>:
                        </div>
                    </div>
                    <div id="delete-success-image_<s:property value="#opRegSuccessField.id"/>"
                         class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>
                        &nbsp;</div>
                </div>
                <s:hidden name="opRegSuccessField[%{#itStatus.index}].id" value="%{#opRegSuccessField.id}"
                          class="success_hidden"></s:hidden>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="success_field_tag-%{#itStatus.index}"><s:text
                            name="label.html.tag.name"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegSuccessField[%{#itStatus.index}].tag" cssClass="required"
                                     id="success_field_tag-%{#itStatus.index}" value="%{#opRegSuccessField.tag}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="success_field_attr-%{#itStatus.index}"><s:text
                            name="label.html.tag.attribute"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegSuccessField[%{#itStatus.index}].attr"
                                     cssClass="required"
                                     id="success_field_attr-%{#itStatus.index}" value="%{#opRegSuccessField.attr}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="success_field_attrVal-%{#itStatus.index}"><s:text
                            name="label.html.attribute.value"/></label></div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegSuccessField[%{#itStatus.index}].attrValue"
                                     cssClass="required"
                                     id="success_field_attrVal-%{#itStatus.index}"
                                     value="%{#opRegSuccessField.attrValue}"/>
                    </div>
                </div>
            </div>
        </s:iterator>
    </s:if>
</div>
<div class="error_field update_field">
    <div class="yui3-g">
        <div class="yui3-u-23-24">
            <h4><s:text name="label.error.field"/></h4>
        </div>
        <div id="field-error" class="yui3-u-1-24 ui-icon ui-icon-add add_field" title='<s:text name="button.add"/>'>
            &nbsp;</div>
    </div>
    <s:if test='operatorField.opRegErrorFieldList!=null'>
        <s:iterator value="operatorField.opRegErrorFieldList" var="opRegErrorField" status="itStatus">
            <div id="error-field_<s:property value="#itStatus.index"/>" class="field input_block">
                <div class="yui3-g row">
                    <div class="yui3-u-23-24">
                        <div class="sub_beader"><s:text name="label.error.field"/> <span class="no"><s:property
                                value="#itStatus.count"/></span>:
                        </div>
                    </div>
                    <div id="delete-error-image_<s:property value="#opRegErrorField.id"/>"
                         class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>
                        &nbsp;</div>
                </div>
                <s:hidden name="opRegErrorField[%{#itStatus.index}].id" value="%{#opRegErrorField.id}"
                          class="error_hidden"></s:hidden>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="error_field_tag-%{#itStatus.index}"><s:text
                            name="label.html.tag.name"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegErrorField[%{#itStatus.index}].tag" cssClass="required"
                                     id="error_field_tag-%{#itStatus.index}"
                                     value="%{#opRegErrorField.tag}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="error_field_attr-%{#itStatus.index}"><s:text
                            name="label.html.tag.attribute"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegErrorField[%{#itStatus.index}].attr" cssClass="required"
                                     id="error_field_attr-%{#itStatus.index}" value="%{#opRegErrorField.attr}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="error_field_attrVal-%{#itStatus.index}"><s:text
                            name="label.html.attribute.value"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegErrorField[%{#itStatus.index}].attrValue"
                                     cssClass="text_field required attrValue"
                                     id="error_field_attrVal-%{#itStatus.index}" value="%{#opRegErrorField.attrValue}"/>
                    </div>
                </div>
            </div>
        </s:iterator>
    </s:if>
</div>
<div class="validator_field update_field">
    <div class="yui3-g">
        <div class="yui3-u-23-24">
            <h4><s:text name="label.validator.field"/></h4>
        </div>
        <div id="field-validator" class="yui3-u-1-24 ui-icon ui-icon-add add_field" title='<s:text name="button.add"/>'>
            &nbsp;</div>
    </div>
    <s:if test='operatorField.opRegValidatorFieldList!=null'>
        <s:iterator value="operatorField.opRegValidatorFieldList" var="opRegValidatorField" status="itStatus">
            <div id="validator-field_<s:property value="#itStatus.index"/>" class="field input_block">
                <div class="yui3-g row">
                    <div class="yui3-u-23-24">
                        <div class="sub_beader"><s:text name="label.validator.field"/> <span class="no"><s:property
                                value="#itStatus.count"/></span>:
                        </div>
                    </div>
                    <div id="delete-validator-image_<s:property value="#opRegValidatorField.id"/>"
                         class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>
                        &nbsp;</div>
                </div>
                <s:hidden name="opRegValidatorField[%{#itStatus.index}].id" value="%{#opRegValidatorField.id}"
                          class="validator_hidden"></s:hidden>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="validator_field_param-%{#itStatus.index}"><s:text
                            name="label.request.parameter"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegValidatorField[%{#itStatus.index}].param"
                                     cssClass="required"
                                     id="validator_field_param-%{#itStatus.index}"
                                     value="%{#opRegValidatorField.param}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="validator_field_tag-%{#itStatus.index}"><s:text
                            name="label.html.tag.name"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegValidatorField[%{#itStatus.index}].tag"
                                     cssClass="required"
                                     id="validator_field_tag-%{#itStatus.index}" value="%{#opRegValidatorField.tag}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="validator_field_attr-%{#itStatus.index}"><s:text
                            name="label.html.tag.attribute"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegValidatorField[%{#itStatus.index}].attr"
                                     cssClass="required"
                                     id="validator_field_attr-%{#itStatus.index}" value="%{#opRegValidatorField.attr}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="validator_field_attrVal-%{#itStatus.index}"><s:text
                            name="label.html.attribute.value"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegValidatorField[%{#itStatus.index}].attrValue"
                                     cssClass="required"
                                     id="validator_field_attrVal-%{#itStatus.index}"
                                     value="%{#opRegValidatorField.attrValue}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="validator_field_key-%{#itStatus.index}"><s:text
                            name="label.table.column.name"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegValidatorField[%{#itStatus.index}].key"
                                     cssClass="required"
                                     id="validator_field_key-%{#itStatus.index}" value="%{#opRegValidatorField.key}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="validator_field_title-%{#itStatus.index}"><s:text
                            name="label.place.holder"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegValidatorField[%{#itStatus.index}].title"
                                     id="validator_field_title-%{#itStatus.index}"
                                     value="%{#opRegValidatorField.title}"/>
                    </div>
                </div>
            </div>
        </s:iterator>
    </s:if>
</div>
<div class="amount_field update_field">
    <div class="yui3-g">
        <div class="yui3-u-23-24">
            <h4><s:text name="label.amount.field"/></h4>
        </div>
        <div id="field-amount" class="yui3-u-1-24 ui-icon ui-icon-add add_field" title='<s:text name="button.add"/>'>
            &nbsp;</div>
    </div>
    <s:if test='operatorField.opRegAvailFundList!=null'>
        <s:iterator value="operatorField.opRegAvailFundList" var="opRegAvailFund" status="itStatus">
            <div id="amount-field_<s:property value="#itStatus.index"/>" class="field input_block">
                <div class="yui3-g row">
                    <div class="yui3-u-23-24">
                        <div class="sub_beader"><s:text name="label.amount.field"/> <span class="no"><s:property
                                value="#itStatus.count"/></span>:
                        </div>
                    </div>
                    <div id="delete-amount-image_<s:property value="#opRegAvailFund.id"/>"
                         class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>
                        &nbsp;</div>
                </div>
                <s:hidden name="opRegAvailFund[%{#itStatus.index}].id" value="%{#opRegAvailFund.id}"
                          class="amount_hidden"></s:hidden>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="error_field_tag-%{#itStatus.index}"><s:text
                            name="label.html.tag.name"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegAvailFund[%{#itStatus.index}].tag" cssClass="required"
                                     id="error_field_tag-%{#itStatus.index}"
                                     value="%{#opRegAvailFund.tag}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="amount_field_attr-%{#itStatus.index}"><s:text
                            name="label.html.tag.attribute"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegAvailFund[%{#itStatus.index}].attr" cssClass="required"
                                     id="amount_field_attr-%{#itStatus.index}" value="%{#opRegAvailFund.attr}"/>
                    </div>
                </div>
                <div class="yui3-g row">
                    <div class="yui3-u-1-3"><label for="amount_field_attrVal-%{#itStatus.index}"><s:text
                            name="label.html.attribute.value"/></label>
                    </div>
                    <div class="yui3-u-1-24">:</div>
                    <div class="yui3-u-5-8">
                        <s:textfield name="opRegAvailFund[%{#itStatus.index}].attrValue"
                                     cssClass="text_field required attrValue"
                                     id="amount_field_attrVal-%{#itStatus.index}" value="%{#opRegAvailFund.attrValue}"/>
                    </div>
                </div>
            </div>
        </s:iterator>
    </s:if>
</div>
<div class="yui3-g row">
    <div class="yui3-u-3-8">&nbsp;</div>
    <div class="yui3-u-5-8">
        <input type="submit" name="submit" id="submit_button" class="button" value='<s:text name="button.save"/>'
               title='<s:text name="button.save"/>'>
    </div>
</div>
</s:form>
</div>
<div id="field-block_common" class="field input_block display_none">
    <div class="yui3-g row">
        <div class="yui3-u-23-24">
            <div class="sub_beader"> Common Field <span class="no"></span>:
            </div>
        </div>
        <div id="delete-common-image_0"
             class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>&nbsp;</div>
    </div>
    <s:hidden name="opRegCommonField[0].id" value="" class="common_hidden"></s:hidden>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="common_field_param-%{#itStatus.index}"><s:text
                name="label.request.parameter"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegCommonField[0].param"
                         cssClass="required"
                         id="common_field_param-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="common_field_key-0"><s:text name="label.table.column.name"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:select headerKey="" headerValue="Select Key"
                      list="commonFieldKey"
                      name="opRegCommonField[0].key"
                      cssClass="text_field required key"
                      id="common_field_key-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="common_field_paramFormat-0"><s:text name="label.checkBox.value"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegCommonField[0].paramFormat"
                         cssClass="text_field param_format"
                         id="common_field_paramFormat-0"/>
        </div>
    </div>
</div>
<div id="field-block_dynamic" class="field input_block display_none">
    <div class="yui3-g row">
        <div class="yui3-u-23-24">
            <div class="sub_beader"> Dynamic Field <span class="no"></span>:
            </div>
        </div>
        <div id="delete-dynamic-image_0"
             class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>&nbsp;</div>
    </div>
    <s:hidden name="opRegDynamicField[0].id" value="" class="dynamic_hidden"></s:hidden>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="dynamic_field_param-0"><s:text name="label.request.parameter"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegDynamicField[0].param"
                         cssClass="required"
                         id="dynamic_field_param-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="dynamic_field_type-0"><s:text name="label.text.field.type"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegDynamicField[0].type"
                         cssClass="required"
                         id="dynamic_field_type-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="dynamic_field_title-0"><s:text name="label.place.holder"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegDynamicField[0].title"
                         cssClass="required"
                         id="dynamic_field_title-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="dynamic_field_required-0"><s:text name="label.is.required"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:select headerKey="" headerValue="Select Value" list="booleanValue"
                      name="opRegDynamicField[0].required"
                      cssClass="required"
                      id="dynamic_field_required-0"/>
        </div>
    </div>
</div>
<div id="field-block_hidden" class="field input_block display_none">
    <div class="yui3-g row">
        <div class="yui3-u-23-24">
            <div class="sub_beader"><s:text name="label.hidden.field"/> <span class="no"></span>:
            </div>
        </div>
        <div id="delete-hidden-image_0"
             class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>&nbsp;</div>
    </div>
    <s:hidden name="opRegHiddenField[0].id" value=""
              class="hidden_hidden"></s:hidden>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="hidden_field_param-0"><s:text name="label.request.parameter"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHiddenField[0].param"
                         cssClass="required"
                         id="hidden_field_param-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="hidden_field_tag-0"><s:text name="label.html.tag.name"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHiddenField[0].tag"
                         cssClass="required"
                         id="hidden_field_tag-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="hidden_field_attr-0"><s:text name="label.html.tag.attribute"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHiddenField[0].attr"
                         cssClass="required"
                         id="hidden_field_attr-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="hidden_field_attrVal-0"><s:text name="label.html.attribute.value"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHiddenField[0].attrValue"
                         cssClass="required"
                         id="hidden_field_attrVal-0"/>
        </div>
    </div>

</div>
<div id="field-block_html" class="field input_block display_none">
    <div class="yui3-g row">
        <div class="yui3-u-23-24">
            <div class="sub_beader"><s:text name="label.html.field"/> <span class="no"></span>:
            </div>
        </div>
        <div id="delete-html-image_0"
             class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>&nbsp;</div>
    </div>
    <s:hidden name="opRegHtmlField[0].id" value="" class="html_hidden"></s:hidden>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="html_field_param-0"><s:text name="label.request.parameter"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHtmlField[0].param"
                         cssClass="required"
                         id="html_field_param-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="html_field_tag-0"><s:text name="label.html.tag.name"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHtmlField[0].tag" cssClass="required"
                         id="html_field_tag-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="html_field_attr-0"><s:text name="label.html.tag.attribute"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHtmlField[0].attr" cssClass="required"
                         id="html_field_attr-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="html_field_attrVal-0"><s:text name="label.html.attribute.value"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHtmlField[0].attrValue"
                         cssClass="required"
                         id="html_field_attrVal-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="html_field_title-0"><s:text name="label.place.holder"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHtmlField[0].title"
                         cssClass="required"
                         id="html_field_title-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="html_field_key-0">Type</label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:select headerKey="" headerValue="Select Value" list="type"
                      name="opRegHtmlField[0].key" cssClass="required"
                      id="html_field_key-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="html_field_paramFormat-0"><s:text name="label.checkBox.value"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegHtmlField[0].paramFormat"
                         cssClass="required"
                         id="html_field_paramFormat-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="html_field_required-0"><s:text name="label.is.required"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:select headerKey="" headerValue="Select Value" list="booleanValue"
                      name="opRegHtmlField[0].required"
                      cssClass="required"
                      id="html_field_required-0"/>
        </div>
    </div>
</div>
<div id="field-block_success" class="field input_block display_none">
    <div class="yui3-g row">
        <div class="yui3-u-23-24">
            <div class="sub_beader"><s:text name="label.success.field"/> <span class="no"></span>:
            </div>
        </div>
        <div id="delete-success-image_0"
             class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>&nbsp;</div>
    </div>
    <s:hidden name="opRegSuccessField[0].id" value="" class="success_hidden"></s:hidden>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="success_field_tag-0"><s:text name="label.html.tag.name"/></label></div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegSuccessField[0].tag" cssClass="required"
                         id="success_field_tag-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="success_field_attr-0"><s:text name="label.html.tag.attribute"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegSuccessField[0].attr"
                         cssClass="required"
                         id="success_field_attr-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="success_field_attrVal-0"><s:text name="label.html.attribute.value"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegSuccessField[0].attrValue"
                         cssClass="required"
                         id="success_field_attrVal-0"/>
        </div>
    </div>
</div>
<div id="field-block_error" class="field input_block display_none">
    <div class="yui3-g row">
        <div class="yui3-u-23-24">
            <div class="sub_beader"><s:text name="label.error.field"/> <span class="no"></span>:
            </div>
        </div>
        <div id="delete-error-image_0"
             class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>&nbsp;</div>
    </div>
    <s:hidden name="opRegErrorField[0].id" value=""
              class="error_hidden"></s:hidden>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="error_field_tag-0"><s:text name="label.html.tag.name"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegErrorField[0].tag" cssClass="required"
                         id="error_field_tag-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="error_field_attr-0"><s:text name="label.html.tag.attribute"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegErrorField[0].attr" cssClass="required"
                         id="error_field_attr-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="error_field_attrVal-0"><s:text name="label.html.attribute.value"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegErrorField[0].attrValue"
                         cssClass="text_field required attrValue"
                         id="error_field_attrVal-0"/>
        </div>
    </div>
</div>
<div id="field-block_validator" class="field input_block display_none">
    <div class="yui3-g row">
        <div class="yui3-u-23-24">
            <div class="sub_beader"><s:text name="label.validator.field"/> <span class="no"></span>:
            </div>
        </div>
        <div id="delete-validator-image_0"
             class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>&nbsp;</div>
    </div>
    <s:hidden name="opRegValidatorField[0].id" value=""
              class="validator_hidden"></s:hidden>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="validator_field_param-0"><s:text name="label.request.parameter"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegValidatorField[0].param"
                         cssClass="required"
                         id="validator_field_param-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="validator_field_tag-0"><s:text name="label.html.tag.name"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegValidatorField[0].tag"
                         cssClass="required"
                         id="validator_field_tag-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="validator_field_attr-0"><s:text name="label.html.tag.attribute"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegValidatorField[0].attr"
                         cssClass="required"
                         id="validator_field_attr-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="validator_field_attrVal-0"><s:text
                name="label.html.attribute.value"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegValidatorField[0].attrValue"
                         cssClass="required"
                         id="validator_field_attrVal-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="validator_field_key-0"><s:text name="label.table.column.name"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegValidatorField[0].key"
                         cssClass="required"
                         id="validator_field_key-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="validator_field_title-0"><s:text name="label.place.holder"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegValidatorField[0].title"
                         id="validator_field_title-0"/>
        </div>
    </div>
</div>
<div id="field-block_amount" class="field input_block display_none">
    <div class="yui3-g row">
        <div class="yui3-u-23-24">
            <div class="sub_beader"><s:text name="label.amount.field"/> <span class="no"></span>:
            </div>
        </div>
        <div id="delete-amount-image_0"
             class="yui3-u-1-24 ui-icon ui-icon-delete delete-field" title='<s:text name="button.delete"/>'>&nbsp;</div>
    </div>
    <s:hidden name="opRegAvailFund[0].id" value=""
              class="amount_hidden"></s:hidden>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="amount_field_tag-0"><s:text name="label.html.tag.name"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegAvailFund[0].tag" cssClass="required"
                         id="amount_field_tag-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="amount_field_attr-0"><s:text name="label.html.tag.attribute"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegAvailFund[0].attr" cssClass="required"
                         id="amount_field_attr-0"/>
        </div>
    </div>
    <div class="yui3-g row">
        <div class="yui3-u-1-3"><label for="amount_field_attrVal-0"><s:text name="label.html.attribute.value"/></label>
        </div>
        <div class="yui3-u-1-24">:</div>
        <div class="yui3-u-5-8">
            <s:textfield name="opRegAvailFund[0].attrValue"
                         cssClass="text_field required attrValue"
                         id="amount_field_attrVal-0"/>
        </div>
    </div>
</div>
<style type="text/css">
</style>
