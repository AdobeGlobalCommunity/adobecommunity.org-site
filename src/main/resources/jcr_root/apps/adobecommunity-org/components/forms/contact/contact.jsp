<%@include file="/libs/sling-cms/global.jsp"%>
<form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Contact Us">
    <fieldset>
        <legend>Your Information</legend>
        <div class="form-group">
            <label for="firstName">First Name <span class="text-danger">*</span></label>
            <input type="text" class="form-control" required="required" name="firstName" />
        </div>
        <div class="form-group">
            <label for="lastName">Last Name <span class="text-danger">*</span></label>
            <input type="text" class="form-control" required="required" name="lastName" />
        </div>
        <div class="form-group">
            <label for="email">Your Email <span class="text-danger">*</span></label>
            <input type="email" class="form-control" required="required" name="email" />
        </div>
    </fieldset>
    <fieldset>
        <legend>Message</legend>
        <div class="form-group">
            <label for="subject">Subject <span class="text-danger">*</span></label>
            <input type="text" class="form-control" required="required" name="subject" />
        </div>
        <div class="form-group">
            <label for="message">Message <span class="text-danger">*</span></label>
            <textarea name="message" class="form-control"></textarea>
        </div>
    </fieldset>
    <fieldset>
        <div class="form-group">
            <button class="btn btn-success">Contact Us</button>
            <a href="/" class="btn btn-default">Cancel</a>
        </div>
    </fieldset>
</form>