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
        <div class="row">
			<div class="col-md-6 my-1">
				<div class="m-1 membership__box membership--free h-100" data-level="Free">
					<strong>
						Basic Membership
						<span class="fa fa-check float-right text-success"></span>
					</strong>
					<sling:include path="freecontainer" resourceType="sling-cms/components/general/container" />
                    <a class="btn btn-primary" href="#join-form">
                        Join
                    </a>
				</div>
			</div>
			<div class="col-md-6 my-1">
				<div class="m-1 membership__box membership--pro h-100" data-level="Pro">
					<strong>
						Premium Membership
						<span class="fa fa-check float-right text-success"></span>
					</strong>
					<sling:include path="procontainer" resourceType="sling-cms/components/general/container" />
                    <a class="btn btn-primary" href="#join-form">
                        Become a Member
                    </a>
				</div>
			</div>
		</div>
		<input type="hidden" name="level" value="Free" />
    </fieldset>
    <div id="join-form" class="d-none px-lg-5 m-lg-5 my-xs-4 pt-5">
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
    		<div class="form-group">
    			<label for="coupon">
    			  Member Company Coupon Code
    			</label>
                <input type="text" class="form-control" name="coupon" />
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
            <div class="form-group">
                <button class="btn btn-success">Join AGC</button>
                <a href="/" class="btn btn-default">Cancel</a>
            </div>
        </fieldset>
    </div>
</form>