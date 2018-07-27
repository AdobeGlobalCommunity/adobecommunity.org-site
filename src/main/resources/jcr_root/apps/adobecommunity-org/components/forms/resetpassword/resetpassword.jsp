<%@include file="/libs/sling-cms/global.jsp"%>
<c:choose>
	<c:when test="${param.res == 'resetpasswd'}">
		<div class="alert alert-success" role="alert">
			Password reset, you can now <a href="/content/agc/adobecommunity-org/login.html">login</a> with your new password.
		</div>
	</c:when>
	<c:when test="${param.err == 'invalid'}">
		<div class="alert alert-danger" role="alert">
			Request token not valid, please <a href="/content/agc/adobecommunity-org/login.html">request a new token</a>.
		</div>
	</c:when>
	<c:when test="${not empty param.err}">
		<div class="alert alert-danger" role="alert">
			An unexpected error occurred, please try again and <a href="/content/agc/adobecommunity-org/contact.html">contact us</a> if this error re-occurs.
		</div>
	</c:when>
</c:choose>
<form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Reset Password">
	<input type="hidden" name="resettoken" value="${sling:encode(param.resettoken,'HTML_ATTR')}" />
	<input type="hidden" name="email" value="${sling:encode(param.email,'HTML_ATTR')}" />
    <div class="form-group">
        <label for="password">New Password <span class="text-danger">*</span></label>
        <input type="password" class="form-control" required="required" name="password" />
    </div>
    <div class="form-group">
        <button class="btn btn-success">Reset Password</button>
        <a href="/content/agc/adobecommunity-org/login.html" class="btn btn-default">Cancel</a>
    </div>
</form>