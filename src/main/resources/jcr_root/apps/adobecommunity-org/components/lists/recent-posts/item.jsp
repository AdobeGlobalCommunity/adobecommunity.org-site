<%@include file="/libs/sling-cms/global.jsp"%>
<div class="mt-3 pb-3 articlesummary">
	<fmt:parseDate value="${item.valueMap['jcr:content/publishDate']}" var="publishDate" pattern="yyyy-MM-dd" />
	<div class="lead">
		<a href="${item.path}.html">${item.valueMap['jcr:content/jcr:title']}</a>
	</div>
	<small class="text-muted">
		By <sling:encode value="${item.valueMap['jcr:content/author']}" mode="HTML" /> on 
		<em itemprop="datePublished"><fmt:formatDate value="${publishDate}" pattern="MMM d, yyyy" /></em>
	</small>
	<br>
</div>