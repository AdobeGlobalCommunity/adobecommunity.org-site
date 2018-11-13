<%@include file="/apps/adobecommunity-org/global.jsp"%>
<c:choose>
	<c:when test="${cmsEditEnabled == 'true'}">
		<c:set var="image" value="${properties.backgroundimage}" />
	</c:when>
	<c:otherwise>
		<c:set var="image" value="${fn:replace(properties.backgroundimage,'/content/agc/adobecommunity-org','')}" />
	</c:otherwise>
</c:choose>
<div class="col-12 background--img ${properties.fullheight ? 'background--img-full' : ''}" style="background-image: url(${image})">
    <div class="background--opaque">
        <div class="jumbotron jumbotron--light container">    
            <h1 class="display-3">
                <sling:encode value="${properties.header}" mode="HTML" />
            </h1>
            <sling:include path="container" resourceType="sling-cms/components/general/container" />
        </div>
    </div>
</div>