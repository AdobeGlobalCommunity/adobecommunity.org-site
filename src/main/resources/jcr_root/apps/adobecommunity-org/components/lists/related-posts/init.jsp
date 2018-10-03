<%@include file="/libs/sling-cms/global.jsp"%>
<c:set var="page" value="${sling:adaptTo(resource,'org.apache.sling.cms.PageManager').page}" />
<c:catch var="ex">
	<c:set var="params" value="${fn:join(page.contentResource.valueMap['sling:taxonomy'],'\\' OR [jcr:content/sling:taxonomy]=\\']')}" />
</c:catch>
<c:set var="query" value="SELECT * FROM [sling:Page] WHERE ISDESCENDANTNODE([/content/agc/adobecommunity-org/learn/articles]) AND [jcr:content/publishDate] IS NOT NULL AND NOT [jcr:path] = '${page.path}' AND ([jcr:content/sling:taxonomy]='${params}')" scope="request" />
<c:set var="limit" value="3" scope="request" />
<c:set var="includePagination" value="false" scope="request" />
<c:set var="tag" value="div" scope="request" />
<c:set var="clazz" value="" scope="request" />
<c:set var="listConfig" value="${pageMgr.page.template.componentConfigs['reference/components/general/list']}" scope="request" />
<c:if test="${not empty limit}">
	<c:set var="list" value="${sling:adaptTo(slingRequest, 'org.apache.sling.cms.reference.models.ItemList')}" scope="request"  />
</c:if>