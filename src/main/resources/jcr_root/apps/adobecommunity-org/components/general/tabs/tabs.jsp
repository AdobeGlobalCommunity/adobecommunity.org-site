<%@include file="/libs/sling-cms/global.jsp"%>
<ul class="nav nav-tabs" id="${resource.name}" role="tablist">
    <c:forEach var="tabName" items="${properties.tabs}" varStatus="status">
        <li class="nav-item">
            <a class="nav-link ${status.first ? 'active' : ''}" id="${resource.name}-tab-link-${status.index}" data-toggle="tab" href="#${resource.name}-tab-${status.index}" role="tab" aria-controls="home" aria-selected="true">
                <sling:encode value="${tabName}" mode="HTML" />
            </a>
        </li>
    </c:forEach>
</ul>
<div class="tab-content">
    <c:forEach var="tabName" items="${properties.tabs}" varStatus="status">
        <div id="${resource.name}-tab-${status.index}" class="tab-pane fade ${status.first ? 'show active' : ''}">
            <sling:include path="tab-${status.index}" resourceType="sling-cms/components/general/container" />
        </div>
    </c:forEach>
</div>