<%@include file="/libs/sling-cms/global.jsp"%>
<c:set var="query" value="SELECT * FROM [sling:UGC] WHERE ISDESCENDANTNODE([/etc/usergenerated/agc/adobecommunity-org/dispatcherscan]) AND [user]='${resourceResolver.userID}' ORDER BY [jcr:content/requested] DESC" scope="request" />
<c:set var="limit" value="10" scope="request" />
<c:set var="includePagination" value="false" scope="request" />

<c:set var="tag" value="ul" scope="request" />
<c:set var="clazz" value="overflow-scroll list-group list-group-flush" scope="request" />
<c:set var="listConfig" value="${pageMgr.page.template.componentConfigs['reference/components/general/list']}" scope="request" />
<c:if test="${not empty limit}">
	<c:set var="list" value="${sling:adaptTo(slingRequest, 'org.apache.sling.cms.reference.models.ItemList')}" scope="request"  />
</c:if>