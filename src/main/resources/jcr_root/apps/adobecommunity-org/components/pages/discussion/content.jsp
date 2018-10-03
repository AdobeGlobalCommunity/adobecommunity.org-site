<%@include file="/libs/sling-cms/global.jsp"%>
<c:set var="suffixResource" value="${slingRequest.requestPathInfo.suffixResource}" />
<fmt:parseDate value="${suffixResource.valueMap['jcr:content/publishDate']}" var="publishDate" pattern="yyyy-MM-dd" />
<div class="container">
	<sling:call script="breadcrumb.jsp" />
</div>
<div class="container padded-content">
	<div class="row">
		<div class="col-md-12">
			<h2 class="my-0">
				<sling:encode value="${suffixResource.valueMap['jcr:content/jcr:title']}" mode="html" />
			</h2>
			<small>By <sling:encode value="${suffixResource.valueMap['jcr:content/username']}" mode="HTML" /> on <fmt:formatDate value="${publishDate}" pattern="MMM d, yyyy" /></small>
			<div class="my-3">
				<sling:adaptTo adaptable="${suffixResource}" adaptTo="org.adobecommunity.site.models.DiscussionMarkdownModel" var="comment" />
				${comment.html}
			</div>
			<sling:include path="share" resourceType="adobecommunity-org/components/general/share" />
			<sling:call script="comments.jsp" />
		</div>
	</div>
</div>