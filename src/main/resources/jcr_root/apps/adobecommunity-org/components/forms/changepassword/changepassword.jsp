<%@include file="/libs/sling-cms/global.jsp"%>
<c:choose>
	<c:when test="${param.res == 'changepasswd'}">
		<div class="alert alert-success" role="alert">
			Password changed successfully!
		</div>
	</c:when>
	<c:when test="${param.err == 'pwerr' || param.err == 'pwdne'}">
		<div class="alert alert-danger" role="alert">
			An unexpected error occurred, please try again and <a href="/content/agc/adobecommunity-org/contact.html">contact us</a> if this error reoccurs.
		</div>
	</c:when>
</c:choose>
<form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Change Password">
	<div class="form-group">
        <label for="oldPassword">Old Password <span class="text-danger">*</span></label>
        <input type="password" class="form-control" required="required" name="oldPassword" />
    </div>
    <div class="form-group">
        <label for="password">New Password <span class="text-danger">*</span></label>
        <input type="password" class="form-control" required="required" name="password" />
    </div>
    <div class="form-group">
        <button class="btn btn-success">Change Password</button>
        <a href="/" class="btn btn-default">Cancel</a>
    </div>
</form>