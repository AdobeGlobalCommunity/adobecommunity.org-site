<%@include file="/apps/adobecommunity-org/global.jsp"%>
<sling:include path="homeheader" resourceType="adobecommunity-org/components/heros/videohero" />
<sling:include path="container" resourceType="sling-cms/components/general/container" />
<c:if test="${cmsEditEnabled == 'true'}">
	<h1>Exit Intent Modal</h1>
	<sling:include path="/content/agc/adobecommunity-org/index/jcr:content/footer" resourceType="sling-cms/components/general/container" />
</c:if>