<%@include file="/libs/sling-cms/global.jsp"%>
<c:choose>
    <c:when test="${param.err == 'email'}">
        <div class="alert alert-danger" role="alert">
            Please complete all required fields.
        </div>
    </c:when>
</c:choose>
<form class="px-lg-5 m-lg-5 my-xs-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Group Membership">
    <fieldset>
        <legend>Your Information</legend>
        <div class="form-group">
            <label for="name">Name <span class="text-danger">*</span></label>
            <input type="text" class="form-control" required="required" name="name" />
        </div>
        <div class="form-group">
            <label for="company">Company <span class="text-danger">*</span></label>
            <input type="text" class="form-control" required="required" name="company" />
        </div>
        <div class="form-group">
            <label for="email">Email <span class="text-danger">*</span></label>
            <input type="email" class="form-control" required="required" name="email" />
        </div>
        <div class="form-group">
            <label for="phone">Phone <span class="text-danger">*</span></label>
            <input type="tel" class="form-control" required="required" name="phone" />
        </div>
    </fieldset>
    <fieldset>
        <legend>Message</legend>
        <div class="form-group">
            <label for="members">Expected Members <span class="text-danger">*</span></label>
            <input type="number" class="form-control" required="required" name="members" />
        </div>
        <div class="form-group">
            <label for="comments">Comments <span class="text-danger">*</span></label>
            <textarea name="comments" class="form-control"></textarea>
        </div>
    </fieldset>
    <fieldset>
        <div class="form-group">
            <button class="btn btn-success">Contact Us</button>
            <a href="/" class="btn btn-default">Cancel</a>
        </div>
    </fieldset>
</form>