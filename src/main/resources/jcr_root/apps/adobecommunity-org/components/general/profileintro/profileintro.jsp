<%@include file="/libs/sling-cms/global.jsp"%>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.adobecommunity.site.models.UserProfileRequest" var="profileRequest"/>
<c:set var="profile" value="${profileRequest.jobProfile}" />
<c:set var="profileprops" value="${profile.resource.valueMap}" />
<h1>${sling:encode(profile.name,'HTML')}</h1>
<hr/>
<dl>
    <dt>
        Interested In
    </dt>
    <dd>
        ${profile.interested}
    </dd>
    <dt>
        Pay Expectation
    </dt>
    <dd>
        $ ${profileprops.pay} per ${profile.period}
    </dd>
    <dt>
        Location
    </dt>
    <dd>
        ${sling:encode(profileprops.location,'HTML')} ${profileprops.relocation == 'yes' ? ' Open to relocation' : '.'}
    </dd>
    <dt>
        Available to work
    </dt>
    <dd>
         ${profile.availability}
    </dd>
    <dt>
        Authorized to work in the Unites States
    </dt>
    <dd>
         ${profileprops.authorized}
    </dd>
    <dt>
        Requires sponsorship for employment visa status
    </dt>
    <dd>
         ${profileprops.status}
    </dd>
</dl>
<hr/>
<p>${sling:encode(profile.about,'HTML')}</p>
<c:if test="${not empty profileprops.other}">
    <h3>Other Skills</h3>
    <p>${sling:encode(profileprops.about,'HTML')}</p>
</c:if>
<c:if test="${not empty profileprops.linkedin}">
    <a href="${sling:encode(profileprops.linkedin,'HTML_ATTR')}" target="_blank">LinkedIn Profile</a>
</c:if>
<c:if test="${not empty profileprops.acclaim}">
    <a href="${sling:encode(profileprops.acclaim,'HTML_ATTR')}" target="_blank">Acclaim Profile</a>
</c:if>
<c:set var="closed" value="${profileRequest.closed}" />
<hr/>