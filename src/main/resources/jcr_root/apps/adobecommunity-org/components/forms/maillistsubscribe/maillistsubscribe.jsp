<%@include file="/libs/sling-cms/global.jsp"%>
<c:choose>
	<c:when test="${param.res == 'subscribed'}">
		<div class="alert alert-success" role="alert">
			You are successfully subscribed with the Adobe Global Community.
		</div>
	</c:when>
</c:choose>
<form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Subscription Form">  
    <div class="form-group">
        <label for="email">Your Email <span class="text-danger">*</span></label>
        <input type="email" class="form-control" required="required" name="email" />
    </div>
    <div class="form-group">
        <button class="btn btn-success">Subscribe</button>
    </div>
</form>