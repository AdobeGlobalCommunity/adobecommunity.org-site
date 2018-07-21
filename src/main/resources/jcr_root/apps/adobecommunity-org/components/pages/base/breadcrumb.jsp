<%@include file="/apps/adobecommunity-org/global.jsp"%>
<c:set var="home" value="${sling:getAbsoluteParent(resource,'3')}" />
<nav aria-label="breadcrumb">
	<ol class="breadcrumb">	
		<li class="breadcrumb-item">
			<a href="${home.path}">
				<em class="fa fa-home"></em>
			</a>
		</li>
		<c:set var="parents" value="${sling:getParents(page.resource,'4')}" />
		<c:forEach var="parent" items="${parents}">
			<c:if test="${parent.valueMap['jcr:primaryType'] == 'sling:Page' && parent.valueMap['jcr:content/published'] == true && (parent.valueMap['jcr:content/hideInSitemap'] == null || parent.valueMap['jcr:content/hideInSitemap'] == false)}">
				<li class="breadcrumb-item"><a href="${parent.path}.html">${sling:encode(parent.valueMap['jcr:content/jcr:title'],'HTML')}</a></li>
			</c:if>
		</c:forEach>
		<li class="breadcrumb-item active" aria-current="page">${sling:encode(page.title,'HTML')}</li>
  </ol>
</nav>