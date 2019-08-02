<%@include file="/libs/sling-cms/global.jsp"%>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.adobecommunity.site.models.UserProfileRequest" var="profileRequest"/>
<c:set var="profile" value="${profileRequest.companyProfile}" />
<c:set var="profileprops" value="${profile.resource.valueMap}" />

<div class="jumbotron">
    <img src="${profile.image}" class="img-fluid pb-2" />
    <h1>${sling:encode(profile.name,'HTML')}</h1>
</div>
<hr/>
<p>${sling:encode(profile.about,'HTML')}</p>
<c:if test="${not empty profileprops.other}">
    <h3>Other Skills</h3>
    <p>${sling:encode(profileprops.about,'HTML')}</p>
</c:if>
<c:if test="${not empty profileprops.spp}">
    <a href="${sling:encode(profileprops.spp,'HTML_ATTR')}" target="_blank">Adobe Solution Partner Portal Profile</a>
</c:if>
<hr/><dl>
    <dt>
        Resources Available in
    </dt>
    <dd>
         ${profile.availability}
    </dd>
</dl>
<c:set var="closed" value="${profileRequest.closed}" />