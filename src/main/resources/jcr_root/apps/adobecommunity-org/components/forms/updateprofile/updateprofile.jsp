<%@include file="/libs/sling-cms/global.jsp"%>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.adobecommunity.site.models.UserData" var="userData" />
<c:choose>
	<c:when test="${param.res == 'profileupd'}">
		<div class="alert alert-success" role="alert">
			Profile updated successfully!
		</div>
	</c:when>
	<c:when test="${param.res == 'req'}">
		<div class="alert alert-danger" role="alert">
			Please provide all required fields!
		</div>
	</c:when>
	<c:when test="${param.err == 'err' || param.err == 'dne'}">
		<div class="alert alert-danger" role="alert">
			An unexpected error occurred, please try again and <a href="/content/agc/adobecommunity-org/contact.html">contact us</a> if this error reoccurs.
		</div>
	</c:when>
</c:choose>
<form class="my-4" action="${resource.path}.allowpost.html" method="post" data-analytics-id="Update Profile">
    <fieldset>
        <legend>Your Information</legend>
        <div class="form-group">
            <label for="firstName">Name <span class="text-danger">*</span></label>
            <input type="text" class="form-control" required="required" name="name" value="${sling:encode(userData.profile.name,'HTML_ATTR')}" />
        </div>
        <div class="form-group">
            <label for="level">Membership Level <span class="text-danger">*</span></label>
            <input type="text" class="form-control" readonly="readonly" name="level" value="${sling:encode(userData.profile.level,'HTML_ATTR')}"  />
            <p><a href="/content/agc/adobecommunity-org/contact.html">Contact Us</a> to update your membership level.
        </div>
        <div class="form-group">
            <label for="email">Your Email <span class="text-danger">*</span></label>
            <input type="email" class="form-control" readonly="readonly" name="email" value="${sling:encode(userData.profile.email,'HTML_ATTR')}"  />
        </div>
        <div class="form-group">
            <label for="phone">Your Phone <span class="text-danger">*</span></label>
            <input type="tel" class="form-control" required="required" name="phone" value="${sling:encode(userData.profile.phone,'HTML_ATTR')}"  />
        </div>
        <div class="form-group">
            <label for="company">Current Company <span class="text-danger">*</span></label>
            <input type="text" class="form-control" required="required" name="company" value="${sling:encode(userData.profile.company,'HTML_ATTR')}"  />
        </div>
        <div class="form-group">
            <label for="role">Current Role</label>
            <select class="form-control" name="role">
                <option ${userData.profile.role == 'Architect' ? 'selected' : ''}>Architect</option>
                <option ${userData.profile.role == 'Business Analyst' ? 'selected' : ''}>Business Analyst</option>
                <option ${userData.profile.role == 'Content Developer' ? 'selected' : ''}>Content Developer</option>
                <option ${userData.profile.role == 'Data Scientist' ? 'selected' : ''}>Data Scientist</option>
                <option ${userData.profile.role == 'Designer / UX Architect' ? 'selected' : ''}>Designer / UX Architect</option>
                <option ${userData.profile.role == 'Digital Analyst' ? 'selected' : ''}>Digital Analyst</option>
                <option ${userData.profile.role == 'Executive' ? 'selected' : ''}>Executive</option>
                <option ${userData.profile.role == 'Marketing Manager' ? 'selected' : ''}>Marketing Manager</option>
                <option ${userData.profile.role == 'Sales Director / Account Manager' ? 'selected' : ''}>Sales Director / Account Manager</option>
                <option ${userData.profile.role == 'Web Developer' ? 'selected' : ''}>Web Developer</option>
            </select>
        </div>
        <c:catch var="exc">
        	<c:set var="allProducts" value="${fn:join(userData.profile.products,'|')}" />
        </c:catch>
        <c:if test="${exc != null}">
        	<c:set var="allProducts" value="${userData.profile.products}" />
        </c:if>
        <div class="form-group">
            <label for="products">Product Focus</label>
            <select class="form-control" name="products" multiple="multiple">
                <option ${fn:contains(allProducts,'Adobe Analytics') ? 'selected' : ''}>Adobe Analytics</option>
                <option ${fn:contains(allProducts,'Adobe Audience Manager') ? 'selected' : ''}>Adobe Audience Manager</option>
                <option ${fn:contains(allProducts,'Adobe Campaign') ? 'selected' : ''}>Adobe Campaign</option>
                <option ${fn:contains(allProducts,'Adobe Experience Manager') ? 'selected' : ''}>Adobe Experience Manager</option>
                <option ${fn:contains(allProducts,'Adobe Media Optimizer') ? 'selected' : ''}>Adobe Media Optimizer</option>
                <option ${fn:contains(allProducts,'Adobe Target') ? 'selected' : ''}>Adobe Target</option>
            </select>
        </div>
    </fieldset>
    <fieldset>
        <legend>Supporting the AGC</legend>
        <div class="form-group">
            <label for="topics">What sort of AEM / CQ related topics are most interesting to you? What would you like to know/learn from this community?</label>
            <textarea name="topics" class="form-control">${sling:encode(userData.profile.topics,'HTML')}</textarea>
        </div>
        <div class="form-group">
            <label for="presentation">Are you available to speak/present and/or join on a panel discussion? If so, what subject/s are you most comfortable talking about?</label>
            <textarea name="presentation" class="form-control">${sling:encode(userData.profile.presentation,'HTML')}</textarea>
        </div>
        <div class="form-group">
            <label for="organizing">Would you be interested in organizing, co-organizing, moderate or volunteer some events?</label>
            <textarea name="organizing" class="form-control">${sling:encode(userData.profile.organizing,'HTML')}</textarea>
        </div>
        <div class="form-group">
            <label for="hosting">Do you have access to a venue? Would you be interested in hosting an event?</label>
            <textarea name="hosting" class="form-control">${sling:encode(userData.profile.hosting,'HTML')}</textarea>
        </div>
    </fieldset>
    <fieldset>
        <div class="form-group">
            <button class="btn btn-success">Update Profile</button>
            <a href="/" class="btn btn-default">Cancel</a>
        </div>
    </fieldset>
</form>