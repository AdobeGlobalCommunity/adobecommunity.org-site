<%@include file="/apps/adobecommunity-org/global.jsp"%>
<div class="contentblock contentblock-leftright row">
	<div class="col-12">
		<div class="row background--white">		
			<c:choose>
				<c:when test="${properties.layout == 'left'}">
					<div class="col-md-6 leftright--image background--img" style="background-image: url(${fn:replace(properties.backgroundimage,'/content/agc/adobecommunity-org','')})"></div>
					<div class="col-md-6 leftright--content text-center p-5">
						<h4>
							<sling:encode value="${properties.header}" mode="HTML" />
						</h4>
						<sling:include path="container" resourceType="sling-cms/components/general/container" />
					</div>
				</c:when>
				<c:otherwise>				
					<div class="col-md-6 d-xs-inline-block d-md-none leftright--image background--img" style="background-image: url(${fn:replace(properties.backgroundimage,'/content/agc/adobecommunity-org','')})"></div>
					<div class="col-md-6 leftright--content text-center p-5">
						<h4>
							<sling:encode value="${properties.header}" mode="HTML" />
						</h4>
						<sling:include path="container" resourceType="sling-cms/components/general/container" />
					</div>
					<div class="col-md-6 d-none d-md-inline-block leftright--image background--img" style="background-image: url(${fn:replace(properties.backgroundimage,'/content/agc/adobecommunity-org','')})"></div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>