<%@include file="/libs/sling-cms/global.jsp"%>
<fmt:parseDate value="${item.valueMap['jcr:content/publishDate']}" var="publishDate" pattern="yyyy-MM-dd" />
<c:choose>
	<c:when test="${not empty item.valueMap['jcr:content/sling:thumbnail']}">
		<c:set var="image" value="${fn:replace(item.valueMap['jcr:content/sling:thumbnail'],'/content/agc/adobecommunity-org','')}" />
	</c:when>
	<c:otherwise>
		<c:set var="image" value="//placehold.it/300x300?text=${sling:encode(item.valueMap['jcr:title'],'HTML_ATTR')}" />
	</c:otherwise>
</c:choose>
<div class="card my-4">
	<a href="${item.path}.html">
		<img class="card-img-top" src="${image}" alt="${sling:encode(item.valueMap['jcr:content/jcr:title'],'HTML')}">
	</a>
	<div class="card-body">
		<h5 class="card-title">
			<a href="${item.path}.html">${sling:encode(item.valueMap['jcr:content/jcr:title'],'HTML')}</a>
		</h5>
		<p class="card-text">
			<small class="text-muted">
				<span class="fa fa-calendar" aria-hidden="true"></span>&nbsp;
				<em itemprop="datePublished"><fmt:formatDate value="${publishDate}" pattern="MMM d, yyyy" /></em><br>
			</small>
		</p>
  </div>
</div>