<%@include file="/libs/sling-cms/global.jsp"%>
<c:set var="site" value="${sling:adaptTo(item,'org.apache.sling.cms.core.models.SiteManager').site}" />
<fmt:parseDate value="${item.valueMap['jcr:content/publishDate']}" var="publishDate" pattern="yyyy-MM-dd" />
<div class="col-sm-6">
    <div class="card m-2">
        <div class="card-img-top background--img" style="height:200px; background-image: url(${item.valueMap['jcr:content/sling:thumbnail']})">
        </div>
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