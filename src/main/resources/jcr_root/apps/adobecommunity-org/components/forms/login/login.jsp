<%@include file="/apps/adobecommunity-org/global.jsp"%>
<c:choose>
	<c:when test="${param.j_reason == 'INVALID_CREDENTIALS'}">
		<div class="alert alert-warning" role="alert">
			Username or password incorrect
		</div>
	</c:when>
	<c:when test="${param.j_reason == 'TIMEOUT'}">
		<div class="alert alert-warning" role="alert">
			Login timed out
		</div>
	</c:when>
</c:choose>
<form class="my-4" action="${page.path}.allowpost.html/j_security_check" method="post" data-analytics-id="Login Form">  
    <div class="form-group">
        <label for="j_username">Your Email <span class="text-danger">*</span></label>
        <input type="email" class="form-control" required="required" name="j_username" />
    </div>
    <div class="form-group">
        <label for="j_password">Password <span class="text-danger">*</span></label>
        <input type="password" class="form-control" required="required" name="j_password" />
    </div>
    <input type="hidden" name="resource" value="/members.html" />
    <div class="form-group">
        <button class="btn btn-success">Login</button>
    </div>
</form>