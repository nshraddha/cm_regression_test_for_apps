<%@ include file="../common/taglib.jsp" %>
<META CONTENT="advertisement-list_advertisement" name="menu">
<h3><s:text name="label.advertisement"/></h3>

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
    <table class="row" cellspacing="0" cellpadding="5" width="100%">
        <tr class="title">
            <th>
                <s:text name="label.name"/>
            </th>
            <th>
                <s:text name="label.description"/>
            </th>
            <th>
                <s:text name="label.action.url"/>
            </th>
            <th>
                <s:text name="label.file"/>
            </th>
            <th>
                <s:text name="label.retina.file"/>
            </th>
            <th>
                <s:text name="label.action"/>
            </th>
        </tr>
        <s:if test="advertisementList !=null && advertisementList.size>0">
            <s:iterator value="advertisementList" var="advertisement">
                <tr>
                    <td>
                        <s:property value="#advertisement.name"/>
                    </td>
                    <td>
                        <s:property value="#advertisement.description"/>
                    </td>
                    <td>
                        <s:property value="#advertisement.actionUrl"/>
                    </td>
                    <td>
                        <a href="<s:property value="#advertisement.imageUrl"/>"
                           style="text-decoration: underline"><s:property value="#advertisement.imageUrl"/></a>
                            <%--<s:property value="#advertisement.imageUrl"/>--%>
                    </td>
                    <td>
                        <a href="<s:property value="#advertisement.retinaFileUrl"/>" style="text-decoration: underline"><s:property
                                value="#advertisement.retinaFileUrl"/></a>
                    </td>
                    <td>
                        <div id="send_advertisement_push-<s:property value="#advertisement.id"/>"
                             class="yui3-u-11-24 ui-icon ui-icon-push"
                             style="margin-bottom: 5px;" title='<s:text name="button.push"/>'
                             onclick="redirectToURL('showAdvertisementPushByCriteriaForm?advertisementId='+<s:property value="#advertisement.id"/>);">
                            &nbsp;</div>

                        <a href="showAdvertisementForm?id=<s:property value="#advertisement.id"/>">
                            <div class="yui3-u-11-24 ui-icon ui-icon-edit edit_advertisement"
                                 style="margin-bottom: 5px;"
                                 title='<s:text name="button.edit"/>'>&nbsp;</div>
                        </a>

                        <div id="delete_advertisement-<s:property value="#advertisement.id"/>"
                             class="yui3-u-11-24 ui-icon ui-icon-delete delete_advertisement"
                             style="margin-bottom: 5px;" title='<s:text name="button.delete"/>'
                             onclick="deleteData(this, 'deleteAdvertisement');">&nbsp;</div>
                    </td>
                </tr>
            </s:iterator>
            <s:else>
                <tr>
                    <td colspan="5">
                        <s:text name="message.no.records.found"/>
                    </td>
                </tr>
            </s:else>
        </s:if>
    </table>
</div>