<%@include file="/apps/adobecommunity-org/global.jsp"%>
<c:set var="site" value="${sling:adaptTo(item,'org.apache.sling.cms.core.models.SiteManager').site}" />
<fmt:parseDate value="${item.valueMap['jcr:content/publishDate']}" var="publishDate" pattern="yyyy-MM-dd" />
<a href="${page.path}/view.html${item.path}" class="list-group-item list-group-item-action my-2">
	<span class="d-flex w-100 justify-content-between">
		<strong class="mb-1">
			<sling:encode value="${item.valueMap['jcr:content/jcr:title']}" mode="HTML" />
		</strong>
		<small><fmt:formatDate value="${publishDate}" pattern="MMM d, yyyy" /></small>
	</span>
	<span class="mb-1">By <sling:encode value="${item.valueMap['jcr:content/username']}" mode="HTML" /></span>
</a>