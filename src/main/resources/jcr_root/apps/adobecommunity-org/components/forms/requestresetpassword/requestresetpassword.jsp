<%@include file="/libs/sling-cms/global.jsp"%>
<c:choose>
	<c:when test="${param.res == 'passwordreset'}">
		<div class="alert alert-success" role="alert">
			Password reset sent successfully, please check your email inbox.
		</div>
	</c:when>
</c:choose>
<form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Login Form">  
    <div class="form-group">
        <label for="email">Your Email <span class="text-danger">*</span></label>
        <input type="email" class="form-control" required="required" name="email" />
    </div>
    <input type="hidden" name="resource" value="/content/agc/adobecommunity-org/members.html" />
    <div class="form-group">
        <button class="btn btn-success">Request Password Reset</button>
    </div>
</form>