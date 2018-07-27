<%@include file="/libs/sling-cms/global.jsp"%>
<c:choose>
	<c:when test="${param.err == 'req'}">
		<div class="alert alert-danger" role="alert">
			Please complete all required fields.
		</div>
	</c:when>
	<c:when test="${param.err == 'err'}">
		<div class="alert alert-danger" role="alert">
			An unexpected error has occurred. Please try again.
		</div>
	</c:when>
	<c:when test="${param.res == 'started'}">
		<div class="alert alert-success" role="alert">
			Your discussion has been submitted successfully and should appear after being reviewed!
		</div>
	</c:when>
</c:choose>
<form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Start Discussion">
    <fieldset>
        <div class="form-group">
            <label for="title">Title <span class="text-danger">*</span></label>
            <input class="form-control" required="required" name="title" type="text">
        </div>
        <div class="form-group">
            <label for="summary">Summary <span class="text-danger">*</span></label>
            <textarea class="form-control" name="summary"></textarea>
        </div>
    </fieldset>
    <fieldset>
        <div class="form-group">
            <input name="created" type="hidden">
            <input name="layout" value="event" type="hidden">
            <button class="btn btn-success"><span class="fa fa-plus-circle"></span> Start Discussion</button>
            <a href="/content/agc/adobecommunity-org/members/discussions.html" class="btn btn-default">Cancel</a>
        </div>
    </fieldset>
</form>