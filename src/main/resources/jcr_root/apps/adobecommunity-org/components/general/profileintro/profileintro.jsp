<%@include file="/libs/sling-cms/global.jsp"%>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.adobecommunity.site.models.UserProfileRequest" var="profileRequest"/>
<c:set var="profile" value="${profileRequest.jobProfile}" />
<c:set var="profileprops" value="${profile.resource.valueMap}" />

<div class="jumbotron">
    <img src="${profile.image}?s=100" class="float-left pb-2 pr-2" />
    <h1>${sling:encode(profile.name,'HTML')}</h1>
    <div style="clear:both"></div>
</div>
<hr/>
<dl>
    <c:if test="${not empty profile.interested}">
        <dt>
            Interested In
        </dt>
        <dd>
            ${profile.interested}
        </dd>
    </c:if>
    <c:if test="${not empty profileprops.pay}">
        <dt>
            Pay Expectation
        </dt>
        <dd>
            ${profileprops.pay}
        </dd>
    </c:if>
    <c:if test="${not empty profileprops.location}">
        <dt>
            Location
        </dt>
        <dd>
            ${sling:encode(profileprops.location,'HTML')} ${profileprops.relocation == 'yes' ? ' Open to relocation' : '.'}
        </dd>
    </c:if>
    <dt>
        Available to work
    </dt>
    <dd>
         ${profile.availability}
    </dd>
    <c:if test="${not empty profileprops.authorized}">
        <dt>
            Authorized to work in the Unites States
        </dt>
        <dd>
             ${profileprops.authorized}
        </dd>
    </c:if>
    <c:if test="${not empty profileprops.status}">
        <dt>
            Requires sponsorship for employment visa status
        </dt>
        <dd>
             ${profileprops.status}
        </dd>
    </c:if>
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