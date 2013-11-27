<%@ include file="../common/taglib.jsp" %>
<META CONTENT="report-favorite-data1" name="menu">
<h3><s:text name="label.user.favorite.details.title"/></h3>

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
    <s:if test='userFavoriteList!=null'>
        <div class="yui3-g row">
            <div class="yui3-u-3-5">&nbsp;</div>
            <div class="yui3-u-3-19"><label for="user_id_for_favorite"><s:text
                    name="label.select.user"/></label></div>
            <div class="yui3-u-1-24">:</div>
            <div class="yui3-u-1-5">
                <select name="operatorId" id="user_id_for_favorite" class="required">
                    <option value="0">All User</option>
                    <s:iterator value="userList" var="user">
                        <option value="<s:property value="#user.id"/>"
                                <s:if test="#user.id == userId">selected</s:if>><s:property value="#user.firstName"/> &nbsp; <s:property value="#user.lastName"/></option>
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
                            <th colspan="3" align="center" style="text-align: center!important;">
                                <s:text name="label.user.favorite.details"/>
                            </th>
                        </tr>
                        <tr>
                            <th class="yui3-u-7-24">
                                <s:text name="label.txOdds.name"/>
                            </th>
                            <th class="yui3-u-7-24">
                                <s:text name="label.locale"/>
                            </th>
                            <th class="yui3-u-7-24">
                                <s:text name="label.is.deleted"/>
                            </th>
                        </tr>
                    </table>
                </th>
            </tr>
            <s:if test="userFavoriteList.size != 0">
                <s:iterator value="userFavoriteList" var="userFavorite" status="itStatus">
                    <tr>
                        <td>
                            <s:property value="#userFavorite.fullName"/>
                        </td>
                        <td>
                            <table cellspacing="0" cellpadding="2" width="100%">
                                <s:iterator value="#userFavorite.favoriteList"
                                            var="favorite">
                                    <tr>
                                        <td class="yui3-u-7-24" style="word-break: normal!important;">
                                            <s:property value="#favorite.txOddsName"/>
                                        </td>
                                        <td class="yui3-u-7-24" style="word-break: normal!important;">
                                            <s:property value="#favorite.locale"/>
                                        </td>
                                        <td class="yui3-u-7-24" style="word-break: normal!important;">
                                            <s:property value="#favorite.status"/>
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