<%@include file="/libs/sling-cms/global.jsp"%>
<c:choose>
	<c:when test="${param.err == 'exists'}">
		<div class="alert alert-warning" role="alert">
			A user already exists with this address, would you like to <a href="/content/agc/adobecommunity-org/login.html">login</a>?
		</div>
	</c:when>
	<c:when test="${param.err == 'req'}">
		<div class="alert alert-danger" role="alert">
			Please complete all required fields.
		</div>
	</c:when>
	<c:when test="${param.err == 'err'}">
		<div class="alert alert-danger" role="alert">
			An unexpected error occurred, please try again and <a href="/content/agc/adobecommunity-org/contact.html">contact us</a> if this error re-occurs.
		</div>
	</c:when>
</c:choose>
<form class="my-4 payment-form" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Join AGC">
	<fieldset>
        <legend>Membership Level</legend>
        <div class="row">
			<div class="col-md-6">
				<div class="m-1 membership__box membership--active membership--free h-100" data-level="Free">
					<strong>
						Free
						<span class="fa fa-check float-right text-success"></span>
					</strong>
					<sling:include path="freecontainer" resourceType="sling-cms/components/general/container" />
				</div>
			</div>
			<div class="col-md-6">
				<div class="m-1 membership__box membership--pro h-100" data-level="Pro">
					<strong>
						Pro
						<span class="fa fa-check float-right text-success"></span>
					</strong>
					<sling:include path="procontainer" resourceType="sling-cms/components/general/container" />
				</div>
			</div>
		</div>
		<input type="hidden" name="level" value="Free" />
    </fieldset>
    <fieldset class="d-none card-container">
        <legend>Membership Payment</legend>
		<div class="form-group">
			<label for="card-element">
			  Credit or debit card
			</label>
			<div id="card-element">
			  <!-- A Stripe Element will be inserted here. -->
			</div>
			<div id="card-errors" role="alert"></div>
		</div>
    </fieldset>
    <fieldset>
        <legend>Login</legend>
        <div class="form-group">
            <label for="email">Your Email <span class="text-danger">*</span></label>
            <input type="email" class="form-control" required="required" name="email" />
        </div>
        <div class="form-group">
            <label for="password">Password <span class="text-danger">*</span></label>
            <input type="password" class="form-control" required="required" name="password" />
        </div>
    </fieldset>
    <fieldset>
        <legend>Your Information</legend>
        <div class="form-group">
            <label for="firstName">Name <span class="text-danger">*</span></label>
            <input type="text" class="form-control" required="required" name="name" />
        </div>
        <div class="form-group">
            <label for="phone">Your Phone <span class="text-danger">*</span></label>
            <input type="tel" class="form-control" required="required" name="phone" />
        </div>
        <div class="form-group">
            <label for="company">Current Company <span class="text-danger">*</span></label>
            <input type="text" class="form-control" required="required" name="company" />
        </div>
        <div class="form-group">
            <label for="role">Current Role</label>
            <select class="form-control" name="role">
                <option>Architect</option>
                <option>Business Analyst</option>
                <option>Content Developer</option>
                <option>Data Scientist</option>
                <option>Designer / UX Architect</option>
                <option>Digital Analyst</option>
                <option>Executive</option>
                <option>Marketing Manager</option>
                <option>Sales Director / Account Manager</option>
                <option>Web Developer</option>
            </select>
        </div>
        <div class="form-group">
            <label for="products">Product Focus</label>
            <select class="form-control" name="products" multiple="multiple">
                <option>Adobe Analytics</option>
                <option>Adobe Audience Manager</option>
                <option>Adobe Campaign</option>
                <option>Adobe Experience Manager</option>
                <option>Adobe Media Optimizer</option>
                <option>Adobe Target</option>
            </select>
        </div>
    </fieldset>
    <fieldset>
        <legend>Supporting the AGC</legend>
        <div class="form-group">
            <label for="topics">What sort of Adobe related topics are most interesting to you? What would you like to know/learn from this community?</label>
            <textarea name="topics" class="form-control"></textarea>
        </div>
        <div class="form-group">
            <label for="presentation">Are you available to speak/present and/or join on a panel discussion? If so, what subject/s are you most comfortable talking about?</label>
            <textarea name="presentation" class="form-control"></textarea>
        </div>
        <div class="form-group">
            <label for="organizing">Would you be interested in organizing, co-organizing, moderate or volunteer some events?</label>
            <textarea name="organizing" class="form-control"></textarea>
        </div>
        <div class="form-group">
            <label for="hosting">Do you have access to a venue? Would you be interested in hosting an event?</label>
            <textarea name="hosting" class="form-control"></textarea>
        </div>
    </fieldset>
    <fieldset>
        <div class="form-group">
            <button class="btn btn-success">Join AGC</button>
            <a href="/" class="btn btn-default">Cancel</a>
        </div>
    </fieldset>
</form>