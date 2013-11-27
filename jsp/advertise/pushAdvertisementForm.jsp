<%@ include file="../common/taglib.jsp" %>
<META CONTENT="add_advertisement" name="menu">
<h3><s:text name="label.push.advertisement"/></h3>

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
    <form action="sendAdvertisementPushByCriteria" method="post" enctype="multipart/form-data"
          id="send_advertisement_push_form">
        <s:hidden name="id"/>
        <div class="yui3-g row">
            <div class="yui3-u-1-4"><label for="country_name"><s:text name="label.country"/></label>
            </div>
            <div class="yui3-u-1-24">:</div>
            <div class="yui3-u-5-8">
                <select name="countryCode" id="country_name" class="required">
                    <s:if test="countryList !=null && countryList.size>0">
                        <option value="">Select Country</option>
                        <s:iterator value="countryList" var="country">
                            <option value="<s:property value="#country.countryCode"/>"><s:property
                                    value="#country.countryName"/></option>
                        </s:iterator>
                        <s:else>
                            <option value="">No Country Found</option>
                        </s:else>
                    </s:if>
                </select>
            </div>
        </div>
        <div class="yui3-g row">
            <div class="yui3-u-1-4"><label><s:text name="label.advertisement"/></label>
            </div>
            <div class="yui3-u-1-24">:</div>
            <div class="yui3-u-5-8 ">
                <div class="yui3-g multiDropDown">
                    <s:if test="advertisementList !=null && advertisementList.size>0">
                        <s:iterator value="advertisementList" var="advertisement">
                            <div class="yui3-g">
                                <div class="yui3-u-1-12">
                                    <input type="checkbox" name="advertisementId"
                                           value='<s:property value="#advertisement.id"/>'
                                           <s:if test="advertisementId !=null && advertisementId.length >0
                                  && #advertisement.id == advertisementId[0]">checked="true"</s:if>/>
                                </div>
                                <div class="yui3-u-11-12"><s:property value="#advertisement.name"/></div>
                            </div>
                        </s:iterator>
                        <s:else>
                            <div class="yui3-g">
                                <div class="yui3-u-1-12">&nbsp;</div>
                                <div class="yui3-u-11-12">No Advertisement Found</div>
                            </div>
                        </s:else>
                    </s:if>

                </div>
                <%--<s:select list="advertisementList" listKey="id" listValue="name" headerKey=""
            headerValue="Select advertisement" id="advertisement_name" name="advertisementId" multiple="true"/>--%>
            </div>
        </div>
        <div class="yui3-g row">
            <div class="yui3-u-7-24">&nbsp;</div>
            <div class="yui3-u-5-24">
                <input type="button" title="<s:text name="button.push"/>" value="<s:text name="button.push"/>"
                       class="button" id="submit_button" name="submit"
                       onclick="formSubmit('send_advertisement_push_form');">
            </div>
            <div class="yui3-u-5-24">
                <input type="button" title='<s:text name="button.cancel"/>' value='<s:text name="button.cancel"/>'
                       class="button" id="cancel_button" name="cancel"
                       onclick="redirectToURL('showAdvertisement');">
            </div>
        </div>

    </form>
</div>
<div style="display: none;">
    <label id="field_required_error" generated="true" class="error"><s:text name="error.required"/></label>
</div>