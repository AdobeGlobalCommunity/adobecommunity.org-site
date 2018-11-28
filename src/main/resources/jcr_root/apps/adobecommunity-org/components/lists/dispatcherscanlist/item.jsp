<%@include file="/libs/sling-cms/global.jsp"%>
<c:set var="currentPage" value="${sling:adaptTo(resource,'org.apache.sling.cms.PageManager').page}" />
<li class="list-group-item">
    <a href="${currentPage.path}.html${item.path}">
        <c:set var="itemProps" value="${sling:getRelativeResource(item,'jcr:content').valueMap}" />
        ${sling:encode(itemProps.domain,'HTML')}<br/>
        <small><fmt:formatDate value="${itemProps.requested.time}" type="BOTH" /></small>
    </a>
</li>