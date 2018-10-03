<%@include file="/libs/sling-cms/global.jsp"%>
<c:set var="site" value="${sling:adaptTo(item,'org.apache.sling.cms.SiteManager').site}" />
<fmt:parseDate value="${item.valueMap['jcr:content/publishDate']}" var="publishDate" pattern="yyyy-MM-dd" />
<div class="col-sm-6">
    <div class="card m-2">
		<c:choose>
			<c:when test="${not empty item.valueMap['jcr:content/sling:thumbnail']}">
				<c:set var="image" value="${fn:replace(item.valueMap['jcr:content/sling:thumbnail'],'/content/agc/adobecommunity-org','')}" />
			</c:when>
			<c:otherwise>
				<c:set var="image" value="//placehold.it/300x300?text=${sling:encode(item.valueMap['jcr:title'],'HTML_ATTR')}" />
			</c:otherwise>
		</c:choose>
		<a href="${item.path}.html">
			<img class="card-img-top" src="${image}" alt="${sling:encode(item.valueMap['jcr:content/jcr:title'],'HTML')}">
		</a>
        <div class="card-body">
            <h4 class="card-title">
                <a href="${item.path}.html">
                    <sling:encode value="${item.valueMap['jcr:content/jcr:title']}" mode="HTML" />
                </a>
            </h4>
            <fmt:parseDate value="${item.valueMap['jcr:content/publishDate']}" var="publishDate" pattern="yyyy-MM-dd" />
            <p class="card-text">By <sling:encode value="${item.valueMap['jcr:content/author']}" mode="HTML" /> on <fmt:formatDate value="${publishDate}" pattern="MMM d, yyyy" /></p>
            <a href="${item.path}.html" class="btn btn-primary">Read More</a>
        </div>
    </div>
</div>