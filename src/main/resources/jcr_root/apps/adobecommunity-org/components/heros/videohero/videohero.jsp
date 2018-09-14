<%@include file="/apps/adobecommunity-org/global.jsp"%>
<c:choose>
	<c:when test="${cmsEditEnabled == 'true'}">
		<c:set var="image" value="${properties.backgroundimage}" />
	</c:when>
	<c:otherwise>
		<c:set var="image" value="${fn:replace(properties.backgroundimage,'/content/agc/adobecommunity-org','')}" />
	</c:otherwise>
</c:choose>
<div class="container-fluid">
    <div class="row">
        <div class="col-12 home-video" style="background-image: url('${image}');">
            <div class="home-video__container">
                <video autoplay loop muted class="home-video__video">
                    <source src="${fn:replace(properties.backgroundvideo,'/content/agc/adobecommunity-org','')}" type="video/mp4" />
                    Your browser does not support the video tag.
                </video>
            </div>
            <div class="home-video__header-background">
                <div class="container home-video__header-container py-5">
                    <h1 class="display-3">
						<sling:encode value="${properties.header}" mode="HTML" />
                    </h1>
                    <sling:include path="container" resourceType="sling-cms/components/general/container" />
                </div>
            </div>
        </div>
    </div>
</div>