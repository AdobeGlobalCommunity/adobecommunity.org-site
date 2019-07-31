<%@include file="/libs/sling-cms/global.jsp"%>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.adobecommunity.site.models.ProfileTags" var="profileTags"/>
<h3>${sling:encode(properties.title,'HTML')}</h3>
<ul>
    <c:forEach var="tag" items="${profileTags.tags}">
        <li>
            <sling:encode value="${tag}" mode="HTML" />
        </li>
    </c:forEach>
</ul>