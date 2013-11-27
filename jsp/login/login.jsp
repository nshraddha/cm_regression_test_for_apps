<%@ include file="../common/taglib.jsp" %>
<META CONTENT="login" name="menu">
<h3><s:text name="label.login"/></h3>

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
    <form action="login" method="post" id="login_form">
        <div class="yui3-g row">
            <div class="yui3-u-1-4"><label for="user_name"><s:text name="label.userName"/></label></div>
            <div class="yui3-u-1-24">:</div>
            <div class="yui3-u-5-8">
                <input class="required" type="text" name="username" id="user_name"/>
            </div>
        </div>
        <div class="yui3-g row">
            <div class="yui3-u-1-4"><label for="password"><s:text name="label.password"/></label></div>
            <div class="yui3-u-1-24">:</div>
            
<div class="yui3-u-5-8">
                <input class="required" type="password" name="password" id="password"/>
            </div>
        </div>
        <div class="yui3-g row">
            <div class="yui3-u-7-24">&nbsp;</div>
            <div class="yui3-u-5-8">
                <input type="submit" name="submit" id="submit_button" class="button"
                       value='<s:text name="button.login"/>'
                       title='<s:text name="button.login"/>'>
            </div>
        </div>
    </form>
</div>
<style type="text/css">
</style>

