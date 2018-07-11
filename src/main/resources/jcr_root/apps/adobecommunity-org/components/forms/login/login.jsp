<%@include file="/apps/adobecommunity-org/global.jsp"%>
<c:if test="${param.res == 'created'}">
	<div class="d-none alert alert-success" role="alert">
		User created successfully! Please login with your email and password.
	</div>
</c:if>
<div class="d-none alert alert-warning" role="alert">
	Username or password incorrect
</div>
<form class="my-4 login-form" action="${page.path}.allowpost.html/j_security_check" method="post" data-analytics-id="Login Form">  
    <div class="form-group">
        <label for="j_username">Your Email <span class="text-danger">*</span></label>
        <input type="email" class="form-control" required="required" name="j_username" />
    </div>
    <div class="form-group">
        <label for="j_password">Password <span class="text-danger">*</span></label>
        <input type="password" class="form-control" required="required" name="j_password" />
    </div>
    <input type="hidden" name="j_validate" value="true" />
    <div class="form-group">
        <button class="btn btn-success">Login</button>
    </div>
</form>