<%@include file="/apps/adobecommunity-org/global.jsp"%>
<c:choose>
	<c:when test="${cmsEditEnabled == 'true'}">
		<h3>Path begins with: ${properties.enabledPath}</h3>
		<sling:include path="when" resourceType="sling-cms/components/general/container" />
		<h3>Otherwise</h3>
		<sling:include path="otherwise" resourceType="sling-cms/components/general/container" />
	</c:when>
	<c:when test="${fn:startsWith(resource.path,${properties.enabledPath})}">
		<sling:include path="when" resourceType="sling-cms/components/general/container" />
	</c:when>
	<c:otherwise>
		<sling:include path="otherwise" resourceType="sling-cms/components/general/container" />
	</c:otherwise>
</c:choose>